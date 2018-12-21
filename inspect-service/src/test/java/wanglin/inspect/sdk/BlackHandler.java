package wanglin.inspect.sdk;

import org.springframework.beans.factory.annotation.Autowired;
import wanglin.inspect.BizType;
import wanglin.inspect.InspectService;
import wanglin.inspect.VarHandler;
import wanglin.inspect.wrapper.WrapperSet;

import java.util.List;
import java.util.Set;

public class BlackHandler implements VarHandler {
    @Autowired
    InspectService inspectService;

    @Override
    public void handle(Long sequence, String varName, BizType bizType, Object request) {
        Set blacklist = null;//GET BLACKLIST FROM ......
        //then wrapper list for rule operate
        inspectService.varValueNotify(sequence,varName,new WrapperSet(blacklist));
    }
}
