package wanglin.inspect;


import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import wanglin.inspect.factory.RuleResultStrategyFactory;
import wanglin.inspect.factory.DataServiceFactory;
import wanglin.inspect.utils.JsonUtils;


public class InspectService extends BaseService {
    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    MessageTemplate messageTemplate;
    @Autowired
    ContextService  contextService;

    @Autowired
    DataServiceFactory dataServiceFactory;

    RuleResultStrategyFactory ruleResultStrategyFactory = new RuleResultStrategyFactory();


    public void inspect(String bizType, Long sequenceNo, Object request) {
        BizType type = getBizType(bizType);
        Assert.notNull(type, "没有这个业务码定义:" + bizType);

        InspectContext context = new InspectContext(sequenceNo, type, request, getRules(bizType), getVars(bizType));
//        log.info("受理检测请求{}-{},{}", context.sequence, context.bizType.name, request);
        contextService.save(context);
        messageTemplate.publishSequence(bizType, sequenceNo, request);

        for (Data data : context.vars) {
            try {
                DataService dataService = dataServiceFactory.get(data.serviceName);
                if (null == dataService) {
                    log.error("no such DataService :{}", data.serviceName);
                    continue;
                }
                dataService.fetch(context.sequence, data.name, context.bizType, context.request);
            } catch (Exception ee) {
                ee.printStackTrace();
                varNotify(context.sequence, data.name, ee);
            }
        }
    }


    public void varNotify(Long sequence, String varName, Object value) {

//        log.warn("收到{}变量{}回调{} ", sequence, varName, value instanceof Exception?((Exception) value).getMessage(): JSON.toJSONString(value));
        InspectContext context = contextService.get(sequence);

        messageTemplate.publishVarResult(context.bizType.code, sequence, varName, value);

        try {
            context.setVarByName(varName, value);
            context.rules.forEach(rule -> {
                if (context.ruleIsReady(rule)) {
                    try {
                        EngineService engine     = engineFactory.get(rule.engine);
                        Object        ruleResult = engine.execute(rule, context);
                        rule._setResult(ruleResult);
                    } catch (Throwable e) {
                        rule._setResult(e);
                    } finally {
                        messageTemplate.publishRuleResult(context.bizType.code, sequence, rule.id, rule.result);
                        log.info("规则{}结果:{}", rule.id, rule.result);
                        if (ruleResultStrategyFactory.get(rule.resultStrategy).test(rule,context)) {
                            context.buildResult();
                            messageTemplate.publishNotify(context.bizType.code,sequence,context.result);
                        }
                    }
                }
            });
            context.buildResult();
        } finally {
            if(context.resultIsDirty ) {
//                log.info("交易回调{}：{}：{}", context.bizType.code, sequence, JsonUtils.toJSONString(context.result));
                messageTemplate.publishSequenceResult(context.bizType.code, sequence, context.result);
                context.resultIsDirty = false;
            }
            contextService.save(context);
        }

    }

}
