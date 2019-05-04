package wanglin.inspect;

public interface MessageTemplate {

    void publishSequence(String bizType, Long sequence, Object request);

    void publishSequenceResult(String bizType,Long sequence, Object result);

    void publishRuleResult(String bizType, Long sequence, Long rule, Object ruleResult);

    void publishVarResult(String bizType, Long sequence, String var, Object varResult);

    void publishNotify(String bizType, Long sequence, Object result);
}
