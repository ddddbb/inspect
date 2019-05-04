package wanglin.inspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultBizTypeService implements BizTypeService {
    Logger log = LoggerFactory.getLogger(getClass());
    @Override
    public String name() {
        return "*";
    }

    @Override
    public void saveOrUpdateSequence(Long sequence,String bizype, Object request) {
        log.warn("保存流水{}请求{}",sequence,request);
    }

    @Override
    public void saveOrUpdateSequenceResult(Long sequence, Object result) {
        log.warn("保存流水{}结果{}",sequence,result);;
    }

    @Override
    public void saveOrUpdateRuleResult(Long sequence, Long rule, Object result) {
        log.warn("保存流水{}规则{}结果{}",sequence,rule,result);;
    }
}
