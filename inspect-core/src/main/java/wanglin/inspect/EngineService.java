package wanglin.inspect;

import wanglin.inspect.exception.ExpressionExecuteException;

import java.util.Set;

public interface EngineService {
    /**
     * 解析规则中需要的变量
     *
     * @param rule
     * @return
     */
    Set<String> analyze(Rule rule);

    /**
     * 执行规则
     *
     * @param rule
     * @return
     */
    Object execute(Rule rule, InspectContext context) throws ExpressionExecuteException;

    EngineEnum name();
}
