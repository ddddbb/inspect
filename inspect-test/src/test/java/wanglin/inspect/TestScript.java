package wanglin.inspect;

import wanglin.inspect.engine.groovy.ScriptRule;

import java.util.List;

public class TestScript implements ScriptRule {


    public Object execute(Long sequence, BizType bizType, Object request) {
        return 0.9;
    }
}
