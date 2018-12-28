package wanglin.inspect.sdk;

import com.alibaba.fastjson.JSON;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.util.Assert;
import wanglin.inspect.AsyncCallback;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 使用redis作为风控消息的MQ服务器
 * 1，Redis比较常见，对不同的系统都有较小的侵入性。
 * 2，同时利用Redission分布式锁
 */
public class RedisMessageListener implements MessageListener, InitializingBean, ApplicationContextAware {
     Logger log = LoggerFactory.getLogger(getClass());
    private InspectClient      inspectClient;
    private ApplicationContext applicationContext;
    private RedissonClient     redisson;
    private Long               lockTimeout;

    private ConcurrentMap<String,AsyncCallback> asyncCallbackMap = new ConcurrentHashMap<String,AsyncCallback>();

    /**
     * 订阅消费风控结果回调
     * 1，如果是同步，则转为同步返回
     * 2，如果非同步，则（幂等返回）获取分布式锁，then调用 AsyncCallback
     *
     * @param message
     * @param bytes
     */
    public void onMessage(Message message, byte[] bytes) {
        Assert.notNull(message.getBody(), "消息为空");
        final MessageBody messageBody = inspectClient.convertMessage(message);
        Assert.notNull(messageBody.sequence, "消息为空");
        if (inspectClient.isLocalSync(messageBody.sequence)) {
            //            唤醒syncMap中的监听器
            log.info("同步回调：{}", JSON.toJSONString(messageBody));
            inspectClient.signal(messageBody.sequence, messageBody.data);
        } else {
            log.info("异步回调：{}", JSON.toJSONString(messageBody));
            if(asyncCallbackMap.get(messageBody.bizType) == null){
                log.error("无异步回调处理器，{}:{}",messageBody.bizType,JSON.toJSONString(messageBody));
            }else{
                asyncCallbackMap.get(messageBody.bizType).notify(messageBody);
            }
        }
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.inspectClient = applicationContext.getBean(InspectClient.class);
        this.redisson = applicationContext.getBean(RedissonClient.class);
        Assert.notNull(inspectClient, "请初始化InspectClient");
        if (lockTimeout == null) {
            lockTimeout = 3000L;
        }
    }

    public void setLockTimeout(Long lockTimeout) {
        this.lockTimeout = lockTimeout;
    }
}
