package wanglin.inspect;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanglin.inspect.utils.HttpUtils;
import wanglin.inspect.utils.JsonUtils;

import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping(value = "inspect")
public class InspectController implements  ApplicationListener<ResultNotify> {
    Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    InspectService inspectService;

    SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0, 0);

    HttpUtils httpUtils = new HttpUtils();

    Cache<Long, CompletableFuture> syncMap = CacheBuilder.newBuilder().maximumSize(100000).expireAfterWrite(10000, TimeUnit.SECONDS).build();


    @RequestMapping(value = "sync")
    public ApiResult sync(String apikey, String biztype, Integer timeout, @RequestBody  Object request) throws InterruptedException, ExecutionException, TimeoutException {
        long sequence = snowflakeIdWorker.nextId();
        timeout = timeout == null?500:timeout;
        CompletableFuture future = new CompletableFuture<>();
        syncMap.put(sequence, future);
        inspectService.inspect(biztype, sequence, request);
        Object result = future.get(timeout, TimeUnit.MICROSECONDS);
        log.info("同步回调{}:{}结果:{}", biztype, sequence, JsonUtils.toJSONString(result));
        return new ApiResult(result);
    }

    @RequestMapping(value = "inspect")
    public void inspect(String apikey, String biztype, Object object) {
        long sequence = snowflakeIdWorker.nextId();
        inspectService.inspect(biztype, sequence, JsonUtils.toJSONString(object));
    }


    @Override
    public void onApplicationEvent(ResultNotify event) {
        if (isSync(event.getSequence())) {
            CompletableFuture future = syncMap.getIfPresent(event.getSequence());
            if (event.getResult() instanceof Throwable) {
                future.completeExceptionally((Throwable) event.getResult());
            } else {
                future.complete(event.getResult());
            }
        } else {
            BizType type = inspectService.getBizType(event.getBiztype());
            if (!StringUtils.isEmpty(type.getNotifyUrl())) {
                try {
                    httpUtils.postJson(type.getNotifyUrl(), event.getResult(), Charset.forName(type.getCharset()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                log.error("异步回调url为空,{}",JsonUtils.toJSONString(event));
            }
        };;
    }

    private boolean isSync(Long sequence) {
        return syncMap.asMap().containsKey(sequence);
    }
}
