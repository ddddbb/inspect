package wanglin.inspect.mq;

import org.springframework.beans.factory.annotation.Autowired;
import wanglin.inspect.BizType;
import wanglin.inspect.InspectService;
import wanglin.inspect.MessageTemplate;
import wanglin.inspect.utils.HttpUtils;

public class HttpMessageTemplate implements MessageTemplate {
    HttpUtils httpUtils;
    @Autowired
    InspectService inspectService;
    @Override
    public void publishSequence(String code, Long sequence, Object request) {
        BizType type = inspectService.getBizType(code);

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
