package wanglin.inspect.mq.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import wanglin.inspect.MessageTemplate;
import wanglin.inspect.Rule;
import wanglin.inspect.Data;
import wanglin.inspect.mq.*;

public class RocketMQMessageTemplate implements MessageTemplate {

    DefaultMQProducer producer;
    public RocketMQMessageTemplate(DefaultMQProducer producer){
        this.producer = producer;
    }

    @Override
    public void publishSequence(String code, Long sequence, Object request) {
//        push(new SequencePersist(code,sequence,request));
    }


    @Override
    public void publishSequenceResult(String code, Long sequence, Object result) {
//        push(new SequenceResultPersist(code,sequence,result));
    }

    @Override
    public void publishRuleResult(String code, Long sequence, Long rule, Object ruleResult) {
//        push(new RuleResultPersist(code,sequence,rule,ruleResult));
    }

    @Override
    public void publishVarResult(String code, Long sequence, String var, Object varResult) {
//        push(new DataResultPersist(code,sequence,var,varResult));
    }

    @Override
    public void publishNotify(String bizType, Long sequence, Object result) {

    }


    private void publish(Object sequencePersist) {

    }
    private void push(Object sequencePersist) {

    }

}
