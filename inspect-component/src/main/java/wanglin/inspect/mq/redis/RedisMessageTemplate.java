package wanglin.inspect.mq.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import wanglin.inspect.Coasts;
import wanglin.inspect.MessageTemplate;
import wanglin.inspect.Rule;
import wanglin.inspect.Data;
import wanglin.inspect.mq.*;
import wanglin.inspect.utils.JsonUtils;

public class RedisMessageTemplate implements MessageTemplate {
    Logger log = LoggerFactory.getLogger(getClass());

    StringRedisTemplate template;

    public RedisMessageTemplate(StringRedisTemplate template) {
        this.template = template;
    }

    private void publish(String channel, Object obj) {
        String value = JsonUtils.toJSONString(obj);
        template.convertAndSend(channel, value);
    }

    @Override
    public void publishSequence(String bizType, Long sequenceNo, Object request) {
        publish(Coasts.TOPIC.SEQUENCE_PERSIST, new Object[]{bizType, sequenceNo, request});
    }

    @Override
    public void publishSequenceResult(String bizType, Long sequenceNo, Object result) {
        publish(Coasts.TOPIC.SEQUENCE_PERSIST_RESULT, new Object[]{bizType, sequenceNo, result});
    }

    @Override
    public void publishRuleResult(String bizType, Long sequence, Long rule, Object ruleResult) {
        publish(Coasts.TOPIC.RULE_RESULT_PERSIST, new Object[]{bizType, sequence, rule, ruleResult});
    }

    @Override
    public void publishVarResult(String bizType, Long sequence, String var, Object varResult) {
        publish(Coasts.TOPIC.DATA_RESULT_PERSIST, new Object[]{bizType, sequence, var, varResult});
    }

    @Override
    public void publishNotify(String bizType, Long sequence, Object result) {
        publish(Coasts.TOPIC.RESULT_NOTIFY, new Object[]{bizType, sequence, result});
    }


}
