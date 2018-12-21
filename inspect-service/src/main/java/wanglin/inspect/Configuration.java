package wanglin.inspect;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import wanglin.inspect.engine.ELEngineService;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class Configuration implements InitializingBean, ApplicationContextAware {
    ConcurrentMap<String, Set<Rule>> ruleCache = new ConcurrentHashMap<>();
    ConcurrentMap<String, Set<Var>> varCache = new ConcurrentHashMap<>();
    ConcurrentMap<String, BizType> bizTypeCache = new ConcurrentHashMap<>();
    @Autowired
    AnyTrueResultProcessor defaultResultProcessor;
    @Autowired
    RedisCallbackProcessor defaultCallbackProcessor;
    EngineService elEngine = new ELEngineService();
    private ApplicationContext applicationContext;


    public Set<Rule> getRules(String bizType) {
        assert null != bizType : "BizType为空";
        Set<Rule> bean = ruleCache.get(bizType);
        if (null == bean) return Collections.emptySet();
        return bean;
    }

    public Set<Var> getVars(String bizType) {
        assert null != bizType : "BizType为空";
        Set<Var> bean = varCache.get(bizType);
        if (null == bean) return Collections.emptySet();
        return bean;
    }

    public VarHandler getVarHandler(String varHandler)  {
        assert null != varHandler : "varHandler不能为空";
        VarHandler bean = applicationContext.getBean(varHandler,VarHandler.class);
        return bean;
    }

    public EngineService getEngine(EngineEnum engine) {
        assert null != engine : "Engine不能为空";
        if(engine == EngineEnum.EL){
            return elEngine;
        }else{
            return null;
        }
    }

    public RuleResultProcessor getRuleResultProcessor(String resultProcessorName) {
        if (StringUtils.isEmpty(resultProcessorName)) return defaultResultProcessor;
        RuleResultProcessor bean = applicationContext.getBean(resultProcessorName,RuleResultProcessor.class);
        if (null == bean) return defaultResultProcessor;
        return bean;
    }

    public BizType getBizType(String bizType) {
        assert null != bizType : "BizType为空";
        BizType bean = bizTypeCache.get(bizType);
        assert null != bean : "BizType不存在";
        return bean;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        //TODO
        bizTypeCache.put("test", new BizType());
        Set<Rule> rules = new HashSet<>();
        rules.add(new Rule(EngineEnum.EL,"测试规则","req.name == 'wanglin'"));
        ruleCache.put("test",rules);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
