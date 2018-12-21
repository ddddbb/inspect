package wanglin.inspect;

import java.util.concurrent.TimeoutException;

public interface InspectService {
    void inspect(String bizType,long sequence,Object request) ;
    void varValueNotify(long sequence,String varName,Object value);
    InspectResult query(long sequence);
}
