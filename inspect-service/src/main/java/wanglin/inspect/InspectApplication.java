package wanglin.inspect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@Configuration
public class InspectApplication {
    public static void main(String[] args) {
        SpringApplication.run(InspectApplication.class, args);
    }

    @Bean
    public ExecutorService getPool(){
        return Executors.newFixedThreadPool(10);
    }
}
