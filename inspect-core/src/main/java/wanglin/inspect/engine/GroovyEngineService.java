package wanglin.inspect.engine;

import groovy.lang.GroovyClassLoader;
import groovy.lang.MetaClass;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import wanglin.inspect.EngineEnum;
import wanglin.inspect.EngineService;
import wanglin.inspect.InspectContext;
import wanglin.inspect.Rule;
import wanglin.inspect.engine.groovy.ScriptRule;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class GroovyEngineService implements EngineService {

    public GroovyEngineService(ApplicationContext applicationContext, ClassLoader classLoader) {
        this.groovyClassLoader = new GroovyClassLoader(classLoader);
        this.applicationContext = applicationContext;
        if (applicationContext instanceof ConfigurableListableBeanFactory) {
            ((ConfigurableListableBeanFactory) applicationContext).ignoreDependencyType(MetaClass.class);
        }
    }

    @Override
    public Set<String> analyze(Rule rule) {
        //todo
        return new HashSet<>();
    }

    @Override
    public Object execute(Rule rule, InspectContext context) {
        ScriptRule scriptRule = getRule(rule.getId(), rule.getContext());
        return scriptRule.execute(context.sequence,context.bizType,context.request);
    }

    @Override
    public EngineEnum name() {
        return EngineEnum.SCRIPT;
    }


    ///
    ConcurrentMap<Long, ScriptRule> scriptCache = new ConcurrentHashMap<>();
    GroovyClassLoader               groovyClassLoader;
    ApplicationContext              applicationContext;

    public ScriptRule getRule(Long id, Object context) {
        Assert.notNull(context,"context为空");
        return scriptCache.getOrDefault(id, newScriptRule(id, (String) context));
    }

    private ScriptRule newScriptRule(Long id, String context) {
        Class clz = groovyClassLoader.parseClass(context);
        try {
            Object obj = clz.newInstance();
            applicationContext.getAutowireCapableBeanFactory().autowireBean(obj);
            return (ScriptRule) obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
