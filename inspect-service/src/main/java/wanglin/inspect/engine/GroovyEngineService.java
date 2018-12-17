package wanglin.inspect.engine;

import wanglin.inspect.EngineService;
import wanglin.inspect.InspectContext;
import wanglin.inspect.Rule;
import wanglin.inspect.engine.groovy.GroovyRule;
import wanglin.inspect.engine.groovy.GroovySriptService;

import java.util.Set;

public class GroovyEngineService implements EngineService {
    GroovySriptService groovySriptService;

    @Override
    public Set<String> analyze(Rule rule) {
        return groovySriptService.getObject(rule.getId(), rule.getContext(), GroovyRule.class).analyzeVars();
    }

    @Override
    public Object execute(Rule rule, Object ruleContext) {
        return groovySriptService.getObject(rule.getId(), rule.getContext(), GroovyRule.class).execute(ruleContext);
    }

    @Override
    public Object buildRuleContext(InspectContext context) {
        //todo
        return null;
    }
}
