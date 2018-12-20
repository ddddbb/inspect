package wanglin.inspect;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.util.concurrent.TimeUnit;

/**
 * 使用redis作为风控消息的MQ服务器
 * 1，Redis比较常见，对不同的系统都有较小的侵入性。
 * 2，同时利用Redission分布式锁
 */
public class RedisMessageListener implements MessageListener, ApplicationContextAware {

    @Autowired
    InspectClient inspectClient;
    @Autowired
    Redisson      redisson;

    Long               lockTimeout;
    ApplicationContext applicationContext;

    /**
     * 订阅消费风控结果回调
     * 1，如果本地syncAdapter有exist，则转为同步返回
     * 2，如果本地syncAdapter没有exist，则获取分布式锁，then调用 AsyncNotifyCallback
     *
     * @param message
     * @param bytes
     */
    public void onMessage(Message message, byte[] bytes) {
        MessageBody messageBody = null;
        assert null != messageBody.sequence;
        if (null != messageBody && inspectClient.isSync(messageBody.sequence)) {
            inspectClient.signal(messageBody.sequence,messageBody.data);
        } else {
            RLock lock = redisson.getLock("/inpsect/result/" + messageBody.sequence);
            try {
                if (lock.tryLock(lockTimeout, TimeUnit.MILLISECONDS)) {
                    applicationContext.getBean(messageBody.bizType, AsyncNotifyCallback.class).signal(messageBody.data);
                }
            } catch (BeansException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
