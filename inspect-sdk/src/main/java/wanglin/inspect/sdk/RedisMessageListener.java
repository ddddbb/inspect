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
    private InspectClient      inspectClient;
    private ApplicationContext applicationContext;


    /**
     * 订阅消费风控结果回调
     * 1，如果是同步，则转为同步返回
     * 2，如果非同步，则（幂等返回）获取分布式锁，then调用 AsyncCallback
     *
     * @param message
     * @param bytes
     */
    public void onMessage(Message message, byte[] bytes) {
        inspectClient.signal(message);
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.inspectClient = applicationContext.getBean(InspectClient.class); ;
    }

}
