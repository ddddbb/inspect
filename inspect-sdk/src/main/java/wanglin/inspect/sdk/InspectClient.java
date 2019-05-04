package wanglin.inspect.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;

public abstract class InspectClient {
    Logger log = LoggerFactory.getLogger(getClass());

    HttpUtils httpUtils = new HttpUtils();
    String    url;
    Charset   charset;


    public InspectClient(String url, String charset) {
        this.url = url;
    }

    public Object sync(String apikey, String bizType, Object object) throws IOException {
        String syncUrl = String.format(url + "/sync?biztype=%s&apikey=%s", bizType, apikey);
        return httpUtils.postJson(syncUrl, object, charset);
    }

    public void async(String apikey, String bizType, Object object) throws IOException {
            String asyncUrl = String.format(url + "/sync?biztype=%s&apikey=%s", bizType, apikey);
            httpUtils.postJson(asyncUrl, object, charset);
    }

    public abstract void onMessage(String biztype, Object result);


}
