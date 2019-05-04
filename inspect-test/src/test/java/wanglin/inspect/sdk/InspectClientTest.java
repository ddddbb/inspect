package wanglin.inspect.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class InspectClientTest {
    Logger        log    = LoggerFactory.getLogger(getClass());
    InspectClient client = new InspectClient("http://localhost:8080/inspect", "utf-8") {

        @Override
        public void onMessage(String biztype, Object result) {
            log.info("{}异步回调结果{}",biztype,result);
        }
    };

    @org.junit.Before
    public void setup() {
    }

    @org.junit.Test
    public void sync() throws IOException {
        log.info("{}",client.sync("","test",""));
        log.info("{}",client.sync("","simpleRuleTest",""));
    }

    @org.junit.Test
    public void async() throws IOException, InterruptedException {
        client.async("","test","");
        client.onMessage("test","11111");
    }

}