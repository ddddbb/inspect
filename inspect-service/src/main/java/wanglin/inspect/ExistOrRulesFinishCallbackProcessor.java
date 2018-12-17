package wanglin.inspect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExistOrRulesFinishCallbackProcessor implements CallbackProcessor {
    @Override
    public void callback(InspectContext context) {
        if(null != context.result){
//            log.info("{}检测结果:{}",context.id, JSON.toJSONString(context.result));
        }
    }
}
