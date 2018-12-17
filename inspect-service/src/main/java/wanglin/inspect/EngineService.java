package wanglin.inspect;

import java.util.Set;

public interface EngineService {
    /**
     * 解析规则中需要的变量
     * @param rule
     * @return
     */
    Set<String> analyze(Rule rule);

    /**
     * 执行规则
     * @param rule
     * @param ruleContext
     * @return
     */
    Object execute(Rule rule, Object ruleContext);

    Object buildRuleContext(InspectContext context);
}
