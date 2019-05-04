package wanglin.inspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultDataService implements DataService {
    Logger log = LoggerFactory.getLogger(getClass());
    @Override
    public String name() {
        return "*";
    }

    @Override
    public void fetch(Long sequence, String varName, BizType bizType, Object request) {
        log.warn("获取流水{}变量{}",sequence,varName);
    }

}
