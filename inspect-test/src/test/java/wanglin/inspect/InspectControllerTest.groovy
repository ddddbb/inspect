package wanglin.inspect


import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import wanglin.inspect.InspectController
import wanglin.inspect.InspectService

import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeoutException

@RunWith(SpringRunner.class)
@SpringBootTest
public class InspectControllerTest {
    InspectController controller = new InspectController();

    @Autowired
    InspectService inspectService;

    ExecutorService es = Executors.newFixedThreadPool (2 );

    @Before
    public void setup(){
        controller.inspectService = inspectService;
        controller.redisTemplate = redisTemplate;
    }

    @Test
    public void sync() throws InterruptedException, ExecutionException, TimeoutException {
        es.submit(new Runnable() {
            @Override
            void run() {
               println  controller.sync("","simpleRuleTest",10000,[amt:10000] );
            }
        })


//        controller.onMessage(new DefaultMessage("".getBytes(), JSON.toJSONString(body).getBytes()),null)
    }
}