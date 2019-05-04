package wanglin.inspect.suanfa

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import wanglin.inspect.BizType
import wanglin.inspect.engine.groovy.ScriptRule

/**
 * 某IP（特征值）最近5分钟注册数大于10
 *
 * 某IP（特征值）最近5分钟注册数大于10
 */
class PLScriptRule implements ScriptRule {

    @Autowired
    RedisTemplate redisTemplate;

    Object execute(Long sequence, BizType bizType, Object request) {
        // 某某模型执行
        // xxMode.execute(request,values......)
        if(redisTemplate.opsForValue().get("last5MinRegNumOfIp:"+request.ip) >10){
            return 0.8
        }
        return 1
    }
}
