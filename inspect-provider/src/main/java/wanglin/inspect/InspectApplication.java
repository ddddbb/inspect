package wanglin.inspect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.jdbc.core.JdbcTemplate;
import wanglin.inspect.config.DbConfigProvider;
import wanglin.inspect.context.RedisContextService;
import wanglin.inspect.factory.EngineFactory;
import wanglin.inspect.factory.DataServiceFactory;
import wanglin.inspect.mq.redis.RedisMessageTemplate;

@SpringBootApplication
@Configuration
@ImportResource("classpath:inspect-config.xml")
public class InspectApplication {
    public static void main(String[] args) {
        SpringApplication.run(InspectApplication.class, args);
    }

    @Bean
    DbConfigProvider loader(JdbcTemplate jdbcTemplate) {
        return new DbConfigProvider(jdbcTemplate);
    }

    @Bean
    RedisResultSubscriber redisResultSubscriber(RedisMessageListenerContainer container){
        return new RedisResultSubscriber(container);
    }

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

    @Bean
    EngineFactory engineFactory(){
        return new EngineFactory();
    }

    @Bean
    RedisMessageTemplate redisMessageTemplate(StringRedisTemplate template){
        return new RedisMessageTemplate(template);
    }

    @Bean
    RedisContextService redisContextService(StringRedisTemplate template){
        return new RedisContextService(template);
    }

    @Bean
    DataServiceFactory varHandlerFactory(){
        return new DataServiceFactory();
    }

}
