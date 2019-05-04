package wanglin.inspect.engine.groovy;

import wanglin.inspect.BizType;
import wanglin.inspect.Data;

import java.util.List;

public interface ScriptRule {

    Object execute(Long sequence, BizType bizType, Object request);
}
