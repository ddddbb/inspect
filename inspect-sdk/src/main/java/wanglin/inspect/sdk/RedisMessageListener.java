package wanglin.inspect.sdk;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import wanglin.inspect.AsyncNotifyCallback;

import java.util.concurrent.TimeUnit;

/**
 * 使用redis作为风控消息的MQ服务器
 * 1，Redis比较常见，对不同的系统都有较小的侵入性。
 * 2，同时利用Redission分布式锁
 */
public class RedisMessageListener implements MessageListener, InitializingBean, ApplicationContextAware {
    private InspectClient      inspectClient;
    private RedissonClient     redissonClient;
    private ApplicationContext applicationContext;
    private RedisTemplate      redisTemplate;
    private Long               lockTimeout;

    /**
     * 订阅消费风控结果回调
     * 1，如果是同步，则转为同步返回
     * 2，如果非同步，则（幂等返回）获取分布式锁，then调用 AsyncNotifyCallback
     *
     * @param message
     * @param bytes
     */
    public void onMessage(Message message, byte[] bytes) {
        Assert.notNull(message.getBody(), "消息为空");
        final MessageBody messageBody = (MessageBody) redisTemplate.getValueSerializer().deserialize(message.getBody());
        Assert.notNull(messageBody.sequence, "消息为空");
        if (inspectClient.isSync(messageBody.sequence)) {
//            直接通知
            inspectClient.signal(messageBody.sequence, messageBody.data);
        } else {
            //冥等& signal
            lock(messageBody, lockTimeout, new Runnable() {
                @Override
                public void run() {
                    applicationContext.getBean(messageBody.bizType, AsyncNotifyCallback.class).signal(messageBody.data);
                }
            });

        }
    }

    private void lock(MessageBody messageBody, Long lockTimeout, Runnable runnable) {
        RLock lock = redissonClient.getLock("/inpsect/result/" + messageBody.sequence);
        try {
            if (lock.tryLock(lockTimeout, TimeUnit.MILLISECONDS)) {
                runnable.run();
            }
        } catch (BeansException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.inspectClient = applicationContext.getBean(InspectClient.class);
        this.redissonClient = applicationContext.getBean(RedissonClient.class);
        this.redisTemplate = applicationContext.getBean("redisTemplate",RedisTemplate.class);
        Assert.notNull(inspectClient, "请初始化InspectClient");
        Assert.notNull(redissonClient, "请初始化RedissonClient");
        if (lockTimeout == null) {
            lockTimeout = 3000L;
        }
    }

    public void setLockTimeout(Long lockTimeout) {
        this.lockTimeout = lockTimeout;
    }
}
