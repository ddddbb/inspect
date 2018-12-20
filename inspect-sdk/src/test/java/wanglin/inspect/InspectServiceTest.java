package wanglin.inspect;

import org.junit.Test;

import java.util.concurrent.TimeoutException;

public class InspectServiceTest {

    InspectService inspectService;

    @Test
    public void sync() throws TimeoutException {
//        同步检测结果
        inspectService.inspect("",null);
    }
//异步回调风控结果
    @Test
    public void asyncNotify( ) {

    }

    class XXXBizTypeResult {

    }
}