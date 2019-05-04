# inspect

 全世界最快的风控系统，检测一笔风控交易花费时间 0.09毫秒,下面是进行100000次交易1个风控规则下的检测时间9秒。各方法详细执行时间统计
 
 process:200000次，239毫秒
 
 getVars:100000次，15毫秒
 
 executeRules:200000次，3360毫秒
 
 getVarHandler:200000次，64毫秒
 
 getBizType:100000次，22毫秒
 
 createInspectContext:100000次，239毫秒
 
 getCallbackProcessor:200000次，26毫秒
 
 getInspectContext:200000次，40毫秒
 
 getPool:1次，27毫秒
 
 getEngine:200000次，28毫秒
 
 getRuleResultProcessor:200000次，32毫秒
 
 inspect:100000次，8820毫秒
 
 callback:200000次，25毫秒
 
 saveInspectContext:100000次，68毫秒
 
 getRules:100000次，6毫秒
 
 varValueNotify:200000次，4102毫秒
 
 
 
  # 系统特点
 
1，适用于任何行业，任何业务

2，可适用任何规则，模型

3，适用于任何业务数据

4，性能好，单机TPS10000+

5，简单

 # 接入步骤

1，为接入业务配置接入码

2，配置风控规则

比如  user.age >  35   //用户年轻大则触发风险

3，调用

inpsectService.inspect(bizCode,bizRequest)

# 规则引擎扩展
内置简单规则引擎实现表达式类规则

Groovy规则引擎实现脚本类规则

可以自行扩展EngineSerivice来扩展规则引擎的实现

# 数据接入的扩展
可以用过扩展DataService接口来实现数据加入的扩展

比如为业务码test配置数据 user  对应的接入脚本为userHandler
并在数据库表var中配置 test ,user userHanlder

userHander脚本如下

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

 

