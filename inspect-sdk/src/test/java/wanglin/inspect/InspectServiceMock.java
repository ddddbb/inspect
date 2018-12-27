package wanglin.inspect;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import wanglin.inspect.sdk.MessageBody;


public class InspectServiceMock implements InspectService, InitializingBean, ApplicationContextAware {
    RedisTemplate redisTemplate;

    private ApplicationContext applicationContext;


    @Override
    public void inspect(String bizType, Long sequence, Object request) {
        redisTemplate.convertAndSend(Coasts.TOPIC.INSPECT_RESULT, new MessageBody(
                "test",
                sequence,
                "ok"
        ));
    }

    @Override
    public void varValueNotify(Long sequence, String varName, Object value) {

    }

    @Override
    public InspectResult query(Long sequence) {
        return null;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        redisTemplate = applicationContext.getBean("redisTemplate",RedisTemplate.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
