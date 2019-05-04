package wanglin.inspect.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import wanglin.inspect.*;
import wanglin.inspect.utils.JsonUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RedisPersistListener implements InitializingBean, MessageListener, ApplicationContextAware {
    RedisMessageListenerContainer container;
    RedissonClient                redissonClient;
    ApplicationContext            applicationContext;
    BizTypeService                defaultBizTypeService = new DefaultBizTypeService();
    DataService                   defaultDataService    = new DefaultDataService();


    public RedisPersistListener(RedisMessageListenerContainer container, RedissonClient redissonClient) {
        this.container = container;
        this.redissonClient = redissonClient;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        container.addMessageListener(new MessageListenerAdapter(this, "onMessage"), new ChannelTopic(Coasts.QUEUE.SEQUENCE_PERSIST));
        container.addMessageListener(new MessageListenerAdapter(this, "onMessage"), new ChannelTopic(Coasts.QUEUE.SEQUENCE_PERSIST_RESULT));
        container.addMessageListener(new MessageListenerAdapter(this, "onMessage"), new ChannelTopic(Coasts.QUEUE.RULE_RESULT_PERSIST));
        container.addMessageListener(new MessageListenerAdapter(this, "onMessage"), new ChannelTopic(Coasts.QUEUE.DATA_RESULT_PERSIST));
    }


    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(pattern);
        String json    = new String(message.getBody());
        RLock  lock    = redissonClient.getLock(channel + "_" + new Date().getTime() + Math.random() * 1000);
        try {
            if (lock.tryLock(10, 180, TimeUnit.SECONDS)) {
                JSONArray a        = JSON.parseArray(json);
                String    biztype  = (String) a.get(0);
                Long      sequence = (Long) a.get(1);
                Object    request  = null;
                Object    result   = null;
                switch (channel) {
                    case Coasts.QUEUE.SEQUENCE_PERSIST:
                        request = a.get(2);
                        getBiztypeService(biztype + "BizTypeService").saveOrUpdateSequence(sequence, biztype, request);
                        break;
                    case Coasts.QUEUE.SEQUENCE_PERSIST_RESULT:
                        result = a.get(2);
                        getBiztypeService(biztype + "BizTypeService").saveOrUpdateSequenceResult(sequence, result);
                        break;
                    case Coasts.QUEUE.RULE_RESULT_PERSIST:
                        result = a.get(3);
                        Long rule = Long.valueOf(a.get(2).toString());
                        getBiztypeService(biztype + "BizTypeService").saveOrUpdateRuleResult(sequence, rule, result);
                        break;
                    case Coasts.QUEUE.DATA_RESULT_PERSIST:
                        result = a.get(3);
                        String data = (String) a.get(2);
//                        getDataService(getDataServiceName(biztype,data)).saveOrUpdateData(sequence, biztype, data, result);
                        break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private BizTypeService getBiztypeService(String name) {
        BizTypeService bean = null;
        try {
            bean = applicationContext.getBean(name, BizTypeService.class);
        } catch (Exception e) {
        }
        if (bean == null) bean = defaultBizTypeService;
        return bean;
    }

    private DataService getDataService(String name) {
        DataService bean = null;
        try {
            bean = applicationContext.getBean(name, DataService.class);
        } catch (Exception e) {
        }
        if (bean == null) bean = defaultDataService;
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public String _getBizServiceName(String bizType) {
        return bizType + "BizTypeService";
    }

    public String getDataServiceName(String bizType,String data) {
        if (data.equals(Coasts.CTX.REQUEST)) {
            return "requestDataService";
        }
        if (data.equals(Coasts.CTX.BIZ_TYPE)) {
            return "typeDataService";
        }
        return bizType + "_" + data + "DataService";
    }
}
