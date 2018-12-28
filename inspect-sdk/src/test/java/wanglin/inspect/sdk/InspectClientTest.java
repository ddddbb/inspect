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

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class )
public class InspectClientTest {

    @Autowired
    InspectClient inspectClient;


    @Test
    public void sync() throws InterruptedException {
        try {
            Object obj = inspectClient.sync("test",10000,new HashMap());
            int i = 1;
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Thread.sleep(10000);
    }

    @Test
    public void async() {
        try {
            inspectClient.async("test",new HashMap());
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