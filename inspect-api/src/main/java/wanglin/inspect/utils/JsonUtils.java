package wanglin.inspect.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    public static String toJSONString(Object obj) {
        return JSON.toJSONString(obj);
    }

    public static <T> T parseObject(String text, Class<T> clz) {
        return JSON.parseObject(text, clz);
    }

    public static <T> List<T> parseArray(String text, Class<T> clz) {
        return JSON.parseArray(text, clz);
    }

    public static Map parse(String text) {
        return JSON.parseObject(text, HashMap.class);
    }

}
