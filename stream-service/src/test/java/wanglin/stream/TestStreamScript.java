package wanglin.stream;

import java.util.concurrent.TimeUnit;

public class TestStreamScript implements StreamScript {
    @Override
    public void on(StreamData type, Object request) {

    }

    @Timing(type= EventMode.EXPIRE,value=5,unit = TimeUnit.SECONDS)
    @Override
    public void expire(StreamData type, Object request) {

    }
}
