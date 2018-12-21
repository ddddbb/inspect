package wanglin.inspect;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import wanglin.inspect.sdk.SnowflakeIdWorker;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InspectServiceImpl implements InspectService {
    static SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0, 0);
    @Autowired
    Configuration   configuration;
    @Autowired
    ContextService  contextService;
    @Autowired
    ExecutorService es;

    @Override
    public void inspect(String bizType, long sequence, Object request) {
        InspectContext context = createInspectContext(bizType, sequence, request);
//        log.info("受理检测请求{},{},{}", context.id, context.bizType, context, request);
        contextService.saveInspectContext(context);
        context.vars.forEach((var, task) -> {
            try {
                configuration.getVarHandler(var.handlerName).handle(context.id, var.name, context.bizType, context.request);
            } catch (Exception ee) {
                varValueNotify(context.id, var.name, ee);
            }
        });
    }

    @Override
    @Async
    public void varValueNotify(long sequence, String varName, Object value) {
//        性能消耗最大的就是这行日志了
        log.debug("收到变量回调{},{},{}", sequence, varName, value instanceof Exception ? ((Exception) value).getMessage() : value);
        InspectContext context = contextService.getInspectContext(sequence);
        context.setVarByName(varName, value);
        executeRules(varName, context);
        //set request result
        if (context.allRuleOver()) {
            configuration.getRuleResultProcessor(context.bizType.resultProcessorName).processResult(context);

            log.info("交易{}:{}检测结果", context.bizType.name, sequence, context.result);
            context.rules.forEach((rule, task) -> {
                log.info("规则{}结果:{}", rule.id, task);
            });
            if (!context.hasCallback()) {
                configuration.getCallbackProcessor(context.bizType.callbackProcessor).callback(context);
            }
        }
    }

    @Override
    public InspectResult query(long sequence) {
        return null;
    }


    public InspectContext createInspectContext(String bizType, long sequence, Object request) {
        BizType   bzType = configuration.getBizType(bizType);
        Set<Rule> rules  = configuration.getRules(bizType);
        Set<Var>  vars   = configuration.getVars(bizType);
        return new InspectContext(sequence, bzType, request, rules, vars);
    }


    public void executeRules(String varName, InspectContext context) {
        context.rules.forEach((rule,task) -> {
            if(rule.containVar(varName) && task.status == Task.TaskStatus.INIT) {
                try {
                    EngineService engine      = configuration.getEngine(rule.engine);
                    Object        ruleContext = engine.buildRuleContext(context);
                    Object        ruleResult  = engine.execute(rule, ruleContext);
                    context.setRule(rule, ruleResult);
                } catch (Throwable e) {
                    context.setRule(rule, e);
                }
            }
        });
    }
}
