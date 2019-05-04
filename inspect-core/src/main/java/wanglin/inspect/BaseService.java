package wanglin.inspect;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.Assert;
import wanglin.inspect.exception.NoSuchVarException;
import wanglin.inspect.factory.EngineFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class BaseService implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {
    Logger log = LoggerFactory.getLogger(getClass());


    ConcurrentMap<String, Set<Rule>> ruleCache    = new ConcurrentHashMap<>();
    ConcurrentMap<String, Set<Data>> varsCache    = new ConcurrentHashMap<>();
    ConcurrentMap<String, BizType>   bizTypeCache = new ConcurrentHashMap<>();


    ApplicationContext applicationContext;
    @Autowired
    ConfigProvider configProvider;
    @Autowired
    EngineFactory  engineFactory;



    public Set<Rule> getRules(String bizType) {
        assert null != bizType : "BizType为空";
        Set<Rule> bean = ruleCache.get(bizType);
        if (null == bean) return Collections.emptySet();
        return bean;
    }

    public Set<Data> getVars(String bizType) {
        assert null != bizType : "bizType为空";
        Set<Data> bean = varsCache.get(bizType);
        if (null == bean) return Collections.emptySet();
        return bean;
    }



    public BizType getBizType(String bizType) {
        Assert.notNull(bizType, "BizType为空");
        BizType bean = bizTypeCache.get(bizType);
        Assert.notNull(bean, "BizType不存在");
        return bean;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            List<BizType> bizTypes = configProvider.getAllBizType();
            List<Data>    vars     = configProvider.getAllVar();
            List<Rule>    rules    = configProvider.getAllRule();


            if (null != bizTypes) {
                bizTypes.forEach(bizType -> {
                    bizTypeCache.put(bizType.code, bizType);
                });
            }

            if (null != rules) {
                rules.forEach(rule -> {
                    rule.setVarNames(engineFactory.get(rule.engine).analyze(rule));
                    ruleCache.computeIfAbsent(rule.bizCode, k -> new HashSet<>()).add(rule);
                    rule.varNames.forEach(varName -> {
                        try {
                            if (!varName.equals("req") && !varName.equals("type")) {
                                varsCache.computeIfAbsent(rule.bizCode, k -> new HashSet<>()).add(getVarObject(vars, rule.bizCode, varName));
                            }
                        } catch (RuntimeException e) {
//                            e.printStackTrace();
                            log.warn("缺少var[{}]定义", varName);
                        }catch (Exception e) {
//                            e.printStackTrace();
                            log.warn("缺少var[{}]定义", varName);
                        }
                    });
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Data getVarObject(List<Data> vars, String bizType, String varName) throws NoSuchVarException {
        Data varObject = null;
        for (Data var : vars) {
            if (varName.equals(var.name) && var.bizCode.equals(bizType)) {
                varObject = var;
            }
        }
        if (null == varObject) throw new NoSuchVarException(bizType, varName);
        return varObject;
    }


}
