package wanglin.inspect.mq.activemq;

import wanglin.inspect.MessageTemplate;
import wanglin.inspect.Rule;
import wanglin.inspect.Data;

public class ActiveMQMessageTemplate implements MessageTemplate {


    @Override
    public void publishSequence(String code, Long sequence, Object request) {

    }

    @Override
    public void publishSequenceResult(String code, Long sequence, Object result) {

    }

    @Override
    public void publishRuleResult(String code, Long sequence, Long rule, Object ruleResult) {

    }

    @Override
    public void publishVarResult(String code, Long sequence, String var, Object varResult) {

    }

    @Override
    public void publishNotify(String bizType, Long sequence, Object result) {

    }


}
