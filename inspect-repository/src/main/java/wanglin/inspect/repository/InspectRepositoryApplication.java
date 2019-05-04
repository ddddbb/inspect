package wanglin.inspect.repository;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@SpringBootApplication
@Configuration
//@ImportResource("classpath*:repository-config.xml")
public class InspectRepositoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(InspectRepositoryApplication.class, args);
    }


    @Bean
    RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://119.23.64.214:6379");
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
    @Bean
    RedisPersistListener redisResultSubscriber(RedisMessageListenerContainer container, RedissonClient redissonClient) {
        return new RedisPersistListener(container, redissonClient);
    }

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }
}
