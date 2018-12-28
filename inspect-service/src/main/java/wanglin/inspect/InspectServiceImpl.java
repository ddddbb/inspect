package wanglin.inspect;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import wanglin.inspect.sdk.MessageBody;
import wanglin.inspect.sdk.SnowflakeIdWorker;

import java.util.Set;

@Service
@Slf4j
public class InspectServiceImpl implements InspectService {
    static SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0, 0);
    @Autowired
    Configuration  configuration;
    @Autowired
    ContextService contextService;
    @Autowired
    RedisTemplate  redisTemplate;

    @Override
    public void inspect(String bizType, Long sequence, Object request) {
        InspectContext context = createInspectContext(bizType, sequence, request);
//        log.info("受理检测请求{},{},{}", context.id, context.bizType, context, request);
        contextService.saveInspectContext(context);
        context.vars.forEach((var, task) -> {
            try {
                configuration.getVarHandler(var.handlerName).handle(context.sequence, var.name, context.bizType, context.request);
            } catch (Exception ee) {
                varValueNotify(context.sequence, var.name, ee);
            }
        });
        waitForResult();
        log.info("{}检测结果:{}", context.sequence, JSON.toJSONString(context.result));
        redisTemplate.convertAndSend(Coasts.TOPIC.INSPECT_RESULT, new MessageBody(
                context.bizType.name,
                context.sequence,
                context.result
        ));
        //todo 这里有问题
    }

    private void waitForResult() {

    }

    @Override
    @Async
    public void varValueNotify(Long sequence, String varName, Object value) {
//        性能消耗最大的就是这行日志了
        log.debug("收到变量回调{},{},{}", sequence, varName, value instanceof Exception ? ((Exception) value).getMessage() : value);
        InspectContext context = contextService.getInspectContext(sequence);
        context.setVarByName(varName, value);
        executeRules(varName, context);
        //set request result
        if (context.allRuleOver()) {
            configuration.getRuleResultProcessor(context.bizType.resultProcessorStrategy).processResult(context);

            log.info("交易{}:{}检测结果", context.bizType.name, sequence, context.result);
            context.rules.forEach((rule, task) -> {
                log.info("规则{}结果:{}", rule.id, task);
            });
            if (!context.hasCallback()) {
//                callbackProcessor.callback(context);
            }
        }
    }

    @Override
    public InspectResult query(Long sequence) {
        Object result = redisTemplate.opsForValue().get(sequence);
//        if (StringUtils.isEmpty(ctx.)) {
//            return InspectResult.NONE;
//        } else {
//            return null;
//        }
        return null;
    }


    public InspectContext createInspectContext(String bizType, long sequence, Object request) {
        BizType   bzType = configuration.getBizType(bizType);
        Set<Rule> rules  = configuration.getRules(bizType);
        Set<Var>  vars   = configuration.getVars(bizType);
        return new InspectContext(sequence, bzType, request, rules, vars);
    }


    public void executeRules(String varName, InspectContext context) {
        context.rules.forEach((rule, task) -> {
            if (rule.containVar(varName) && task.status == Task.TaskStatus.INIT) {
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
