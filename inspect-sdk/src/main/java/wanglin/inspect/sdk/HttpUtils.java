package wanglin.inspect.sdk;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public class HttpUtils {
    HttpClient client = org.apache.http.impl.client.HttpClients.createDefault();


    public String postJson(String url, Object request,Charset charset) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        StringEntity se = new StringEntity(JSON.toJSONString(request), charset);
        httpPost.setEntity(se);
        se.setContentType("text/json");
        HttpResponse response = client.execute(httpPost);
        return EntityUtils.toString(response.getEntity(), charset);
    }
}
