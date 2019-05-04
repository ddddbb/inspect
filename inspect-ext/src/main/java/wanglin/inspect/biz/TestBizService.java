package wanglin.inspect.biz;

import wanglin.inspect.BizTypeService;
import wanglin.inspect.Rule;

public class TestBizService implements BizTypeService {
    @Override
    public String name() {
        return "test";
    }

    @Override
    public void saveOrUpdateSequence(Long sequence,String biztype, Object request) {

    }

    @Override
    public void saveOrUpdateSequenceResult(Long sequence, Object result) {

    }

    @Override
    public void saveOrUpdateRuleResult(Long sequence, Long rule, Object result) {

    }
}
