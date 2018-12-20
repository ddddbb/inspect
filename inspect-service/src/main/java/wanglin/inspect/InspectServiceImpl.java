package wanglin.inspect;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

@Service
@Slf4j
public class InspectServiceImpl implements InspectService {
    static SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0, 0);
    @Autowired
    Configuration configuration;
    @Autowired
    ContextService contextService;
    @Autowired

    InspectServiceImpl inspectService;
    @Autowired
    ExecutorService es;

    @Override
    public void inspect(String bizType,long sequence, Object request) {
        InspectContext context = inspectService.createInspectContext(bizType,sequence, request);
//        log.info("受理检测请求{},{},{}", context.id, context.bizType, context, request);
        contextService.saveInspectContext(context);
        context.vars.forEach((var, task) -> {
            try {
                configuration.getVarHandler(var.handlerName).handle(context.id, var.name, context.bizType, context.request);
            } catch (Exception ee) {
                inspectService.varValueNotify(context.id, var.name, ee);
            }
        });
    }

    @Override
    @Async
    public void varValueNotify(String id, String varName, Object value) {
//        性能消耗最大的就是这行日志了
        log.debug("收到变量回调{},{},{}", id, varName, value instanceof Exception ? ((Exception) value).getMessage() : value);
        InspectContext context = contextService.getInspectContext(id);
        context.setVarByName(varName, value);
        inspectService.executeRules(varName, context);
        //set request result

        configuration.getRuleResultProcessor(context.bizType.resultProcessorName).process(context);
        if (null != context.result) {
            configuration.getCallbackProcessor(context.bizType.callbackProcessor).callback(context);
        }
    }



    public InspectContext createInspectContext(String bizType,long sequence, Object request) {
        BizType bzType = configuration.getBizType(bizType);
        Set<Rule> rules = configuration.getRules(bizType);
        Set<Var> vars = configuration.getVars(bizType);
        return new InspectContext("" + sequence, bzType, request, rules, vars);
    }

    ///////////////////////////////////////////////////////////
    public void executeRules(String varName, InspectContext context) {
        Set<Rule> rules = relationRules(context, varName);
        rules.forEach(rule -> {
            try {
                EngineService engine = configuration.getEngine(rule.engine);
                Object ruleContext = engine.buildRuleContext(context);
                Object ruleResult = engine.execute(rule, ruleContext);
                context.setRule(rule, ruleResult);
            } catch (Exception e) {
                e.printStackTrace();
                context.setRule(rule, e);
            }
        });
    }

    public Set<Rule> relationRules(InspectContext context, String varName) {
        Set<Rule> rules = context.getRules().keySet();
        Set<Rule> rrs = new HashSet<>();
//
//        rules.forEach(rule -> {
//            if (rule.containVar(varName)) {
//                rrs.add(rule);
//            }
//        });
//        return rrs;
        return rules;
    }
}
