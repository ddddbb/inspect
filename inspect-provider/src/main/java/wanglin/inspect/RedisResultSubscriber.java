package wanglin.inspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import wanglin.inspect.utils.JsonUtils;

public class RedisResultSubscriber implements InitializingBean, MessageListener, ApplicationContextAware {
    Logger                        log = LoggerFactory.getLogger(getClass());
    RedisMessageListenerContainer container;
    ApplicationContext            applicationContext;

    public RedisResultSubscriber(RedisMessageListenerContainer container) {
        this.container = container;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        container.addMessageListener(this, new ChannelTopic(Coasts.TOPIC.RESULT_NOTIFY));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msgJson = new String(message.getBody());
        String channel = new String(pattern);

        log.debug("订阅到检测结果:{}", msgJson);
//        String bizType, Long sequenceNo, Object result
        JSONArray a = JSON.parseArray(msgJson);
        applicationContext.publishEvent(new ResultNotify(a.getString(0), a.getLong(1), a.get(2)));
    }
}
