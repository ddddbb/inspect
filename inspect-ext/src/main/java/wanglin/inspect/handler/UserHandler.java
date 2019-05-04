package wanglin.inspect.handler;

import org.springframework.beans.factory.annotation.Autowired;
import wanglin.inspect.BizType;
import wanglin.inspect.DataService;
import wanglin.inspect.InspectService;

import java.util.HashMap;
import java.util.Map;

public class UserHandler implements DataService {

    @Autowired
    InspectService inspectService;


    @Override
    public void fetch(Long sequence, String varName, BizType bizType, Object request) {
        Map<String,Object> map = new HashMap<>();
        map.put("name","zhangsan");
        map.put("age",10);
        inspectService.varNotify(sequence,varName,map);
    }




    @Override
    public String name() {
        return "userHandler";
    }
}
