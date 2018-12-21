package wanglin.inspect;

import java.util.concurrent.TimeoutException;

public interface InspectService {
    void inspect(String bizType,Long sequence,Object request) ;
    void varValueNotify(Long sequence,String varName,Object value);
    InspectResult query(Long sequence);
}
