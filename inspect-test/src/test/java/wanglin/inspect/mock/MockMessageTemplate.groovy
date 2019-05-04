package wanglin.inspect.mock

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import wanglin.inspect.MessageTemplate
import wanglin.inspect.Rule
import wanglin.inspect.Data


class MockMessageTemplate  implements MessageTemplate{
    Logger log = LoggerFactory.getLogger(getClass());
    Map<String,Queue> queues = new HashMap<>();
    Map<String,Queue> topics = new HashMap<>();

    void publishSequence(String code, Long sequence, Object request) {
//        log.debug("持久化流水")
    }

    void publishSequenceResult(String code, Long sequence, Object result) {
//        log.debug("持久化流水结果")
    }

    void publishRuleResult(String code, Long sequence, Long rule, Object ruleResult) {
//        log.debug("持久化规则")
    }

    void publishVarResult(String code, Long sequence, String var, Object varResult) {
//        log.debug("持久化数据")
    }

    void publishNotify(String bizType, Long sequence, Object result) {
//        log.debug("发布检测结果")
    }



}
