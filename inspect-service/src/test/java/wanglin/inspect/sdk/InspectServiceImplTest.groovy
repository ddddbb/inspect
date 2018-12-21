package wanglin.inspect.sdk

import com.alibaba.fastjson.JSON
import lombok.Data
import org.junit.After
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.StopWatch
import wanglin.inspect.Configuration
import wanglin.inspect.InspectService

import java.util.concurrent.TimeUnit

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = InspectApplication.class)
public class InspectServiceImplTest {
    Logger log = LoggerFactory.getLogger(getClass())
    @Autowired
    InspectService inspectService;
    @Autowired
    Configuration configuration;
//
//    @Before
//    public void setup() {
//        configuration.bizTypeCache = new ConcurrentHashMap<>([test: new BizType()] as Map);
//    }


    @Test
    public void tt() {
        Tt tt = new Tt();
        KryoUtils.toByte(tt)
    }

    @Data
    class Tt extends Object {
        String t1 = "1";
        String t2;
    }

    @Test
    public void testLog() {
        // 创建一个计时器
        StopWatch watch = new StopWatch();
        // 计时器开始
        watch.start();

        10000.times {
            log.info("打印log{}", JSON.toJSONString(1))
        }

        // 计时器停止
        watch.stop();
        println("花费时间：" + watch.getTotalTimeMillis() + "毫秒")
    }

    @Test
    public void inspect() {
        Long s = System.nanoTime();
        100000.times {
            try {
                inspectService.inspect("test", 1l, [name: 'wanglilin'] as Map)
            } catch (Exception e) {
                e.printStackTrace()
            }
        }

        Long e = System.nanoTime();
        println(TimeUnit.SECONDS.convert((e - s), TimeUnit.NANOSECONDS));
    }

    @Test
    public void varValueNotify() {

    }

    // 测试方法运行完毕后，取出定义的Map集合，取出数据
    @After
    public void testMethodActive() {
        Map<String, Long[]> map = MethodTimeActive.methodTest;
        Set<String> set = map.keySet();
        Long[] x = null;
        for (String s : set) {
            x = map.get(s);
            System.out.println(s + ":" + x[0] + "次，" + x[1] + "毫秒");
        }
    }

}