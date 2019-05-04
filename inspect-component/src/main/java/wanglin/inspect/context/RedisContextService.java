package wanglin.inspect.context;

import org.springframework.data.redis.core.StringRedisTemplate;
import wanglin.inspect.Coasts;
import wanglin.inspect.ContextService;
import wanglin.inspect.InspectContext;
import wanglin.inspect.utils.JsonUtils;

public class RedisContextService implements ContextService {

    StringRedisTemplate redisTemplate;

    public RedisContextService(StringRedisTemplate redisTemplate){
        this.redisTemplate =redisTemplate;
    }

    @Override
    public void save(InspectContext context) {
        String value = JsonUtils.toJSONString(context);
        redisTemplate.opsForValue().set(Coasts.CACHE.SEQUENCE + context.sequence, value);
    }

    @Override
    public InspectContext get(Long sequence) {
        String value = redisTemplate.opsForValue().get(Coasts.CACHE.SEQUENCE + sequence);
        if (null == value) return null;
        return JsonUtils.parseObject(value, InspectContext.class);
    }
}
