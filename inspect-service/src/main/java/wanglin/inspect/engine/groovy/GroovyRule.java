package wanglin.inspect.engine.groovy;

import java.util.Set;

public interface GroovyRule {
    Set<String> analyzeVars();

    Object execute(Object ruleContext);
}
