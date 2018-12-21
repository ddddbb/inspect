package wanglin.inspect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import wanglin.inspect.sdk.MessageBody;

@Slf4j
@Service
public class ExistOrRulesFinishCallbackProcessor implements CallbackProcessor {
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void callback(InspectContext context) {
        if(null != context.result){
//            log.info("{}检测结果:{}",context.id, JSON.toJSONString(context.result));
            redisTemplate.convertAndSend(Coasts.TOPIC.INSPECT_RESULT,new MessageBody(
                    context.bizType.name,
                    context.id,
                    context.result
            ));
        }
    }
}
