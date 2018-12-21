package wanglin.inspect.sdk;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import wanglin.inspect.AsyncNotifyCallback;

import java.util.concurrent.TimeUnit;

/**
 * 使用redis作为风控消息的MQ服务器
 * 1，Redis比较常见，对不同的系统都有较小的侵入性。
 * 2，同时利用Redission分布式锁
 */
@Component
public class RedisMessageListener implements MessageListener, ApplicationContextAware {

    @Autowired
    private InspectClient  inspectClient;
    @Autowired
    private RedissonClient redissonClient;

    private Long               lockTimeout;
    private ApplicationContext applicationContext;

    /**
     * 订阅消费风控结果回调
     * 1，如果是同步，则转为同步返回
     * 2，如果非同步，则（幂等返回）获取分布式锁，then调用 AsyncNotifyCallback
     *
     * @param message
     * @param bytes
     */
    public void onMessage(Message message, byte[] bytes) {
        MessageBody messageBody = null;
        assert null != messageBody.sequence;
        if (null != messageBody && inspectClient.isSync(messageBody.sequence)) {
            inspectClient.signal(messageBody.sequence, messageBody.data);
        } else {
            RLock lock = redissonClient.getLock("/inpsect/result/" + messageBody.sequence);
            try {
                if (lock.tryLock(lockTimeout, TimeUnit.MILLISECONDS)) {
                    applicationContext.getBean(messageBody.bizType, AsyncNotifyCallback.class).signal(messageBody.data);
                }
            } catch (BeansException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }

        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
