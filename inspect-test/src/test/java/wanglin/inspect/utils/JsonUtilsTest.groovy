package wanglin.inspect.utils

import com.alibaba.fastjson.JSON
import org.junit.Test
import wanglin.inspect.ResultNotify

class JsonUtilsTest  {

    @Test
    public void toJsonString(){
        println JsonUtils.toJSONString([name:"wanglin"])
        println JsonUtils.toJSONString(new ResultNotify("test",1l,"ttt"))
    }
    @Test
    public void parseObject(){
        println JsonUtils.parse("{\"source\":\"ttt\",\"timestamp\":1549966199225,\"biztype\":\"test\",\"sequence\":1,\"result\":\"ttt\"}")
    }
    @Test
    public void e1(){
        String json = "{\"@type\":\"wanglin.inspect.mq.DataResultPersist\",\"bizType\":\"yunpian\",\"data\":{\"completed\":false,\"id\":9223372036854775806,\"name\":\"req\",\"serviceName\":\"requestHandler\",\"status\":\"INIT\"},\"result\":{\"@type\":\"java.util.LinkedHashMap\",\"phone\":\"131222222\"},\"sequence\":547840740948967424}";
        Object obj = JSON.parseObject(json,DataResultPersist.class)
        println obj;
    }
}
