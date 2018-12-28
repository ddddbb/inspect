package wanglin.inspect.sdk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import wanglin.inspect.TestApplication;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class InspectClientTest {

    @Autowired
    InspectClient inspectClient;

    ExecutorService es = Executors.newFixedThreadPool(2);


    @Test
    public void sync() throws InterruptedException {
        for (int z = 0; z < 30; z++)

            es.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Object obj = inspectClient.sync("test", 10000, new HashMap());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
            });


        Thread.sleep(10000);
    }

    @Test
    public void async() {
        for (int z = 0; z < 30; z++)
            try {
                inspectClient.async("test", new HashMap());
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Test
    public void querey() {
        try {
            inspectClient.query(1L);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}