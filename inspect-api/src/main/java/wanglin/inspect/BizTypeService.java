package wanglin.inspect;


public interface BizTypeService<R> {

    String name();

    void saveOrUpdateSequence(Long sequence, String biztype,R request);

    void saveOrUpdateSequenceResult(Long sequence, Object result);

    void saveOrUpdateRuleResult(Long sequence, Long rule, Object result);

}
