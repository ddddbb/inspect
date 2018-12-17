package wanglin.inspect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class RequestHandler implements VarHandler {
    @Autowired
    InspectService inspectService;
    @Override
    @Async
    public void handle(String uuid, String varName, BizType bizType, Object request) {
        if(bizType.name.equals(Coasts.Ctx.BIZ_TYPE)) {
            inspectService.varValueNotify(uuid, Coasts.Ctx.BIZ_TYPE, bizType);
        }
        if(bizType.name.equals(Coasts.Ctx.REQUEST)) {
            inspectService.varValueNotify(uuid, Coasts.Ctx.REQUEST, request);
        }
    }
}
