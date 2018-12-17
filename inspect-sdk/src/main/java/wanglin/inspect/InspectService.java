package wanglin.inspect;

import java.util.concurrent.TimeoutException;

public interface InspectService {
    void inspect(String bizType,Object request) ;
    void varValueNotify(String uuid,String varName,Object value);
}
