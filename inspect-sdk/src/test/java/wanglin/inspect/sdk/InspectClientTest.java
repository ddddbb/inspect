package wanglin.inspect.sdk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:inspect-test.xml"})
public class InspectClientTest {

    @Autowired
    InspectClient inspectClient;

    @Test
    public void sync() {
        try {
            Object obj = inspectClient.sync("test",10000,new HashMap());
            int i = 1;
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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