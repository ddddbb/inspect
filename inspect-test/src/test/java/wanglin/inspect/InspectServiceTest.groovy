package wanglin.inspect


import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import java.util.concurrent.TimeUnit

//加载配置文件
@RunWith(SpringJUnit4ClassRunner.class)
//使用junit4进行测试
@ContextConfiguration(locations = "classpath:inspectservice-test.xml")
//加载配置文件
class InspectServiceTest {

    @Autowired
    InspectService impl


    @Test
    public void inspect() {
        impl.inspect("test", 1, "{amt:10000}")
    }

    @Test
    public void perf() {
        long s = System.currentTimeMillis();
        100000.times {
            impl.inspect("test", 1, "{amt:10000}")
        }
        long e = System.currentTimeMillis();
        println(TimeUnit.SECONDS.convert((e - s), TimeUnit.MILLISECONDS))

    }

}
