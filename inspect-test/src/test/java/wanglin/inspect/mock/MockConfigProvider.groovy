package wanglin.inspect.mock

import wanglin.inspect.*

public class MockConfigProvider implements ConfigProvider {
    public List<BizType> getAllBizType() throws IOException {
        return [new BizType(code: "test",name: "",notifyUrl: "",charset: "utf-8")];
    }

    public List<Data> getAllVar() throws IOException {
        return [new Data(id: 1, bizCode: "test", name: "user", serviceName: "userHandler")];
    }

    public List<Rule> getAllRule() throws IOException {
        return [
                new Rule(id: 1,bizCode: "test",engine: EngineEnum.EXPRESSION,context: "user.name == 'zhangsan'",name: "用户黑名单",
                        resultStrategy:"IfTrueReject",resultField: "reject")
        ];
    }
}
