package wanglin.inspect.sdk;

import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import wanglin.inspect.AsyncCallback;
import wanglin.inspect.Coasts;
import wanglin.inspect.InspectService;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class InspectClient implements InitializingBean, ApplicationContextAware {
    Logger log = LoggerFactory.getLogger(getClass());
    private Cache<Long, ThreadHolder> syncMap           = CacheBuilder.newBuilder().maximumSize(100000).expireAfterWrite(1000, TimeUnit.SECONDS).build();

    private SnowflakeIdWorker         snowflakeIdWorker = new SnowflakeIdWorker(0, 0);
    private ApplicationContext        applicationContext;
    private InspectService            inspectService;
    private RedissonClient            redisson;
    private RedisTemplate             redisTemplate;

    public Object sync(String bizType, Integer timeout, Object object) throws IOException, TimeoutException {
        long sequence = snowflakeIdWorker.nextId();
        try {
            inspectService.inspect(bizType, sequence, object);
        } catch (RpcException e) {
            throw new IOException(e);
        }
        try {
            ThreadHolder threadHolder = new ThreadHolder(sequence, timeout);
            syncMap.put(sequence, threadHolder);
            threadHolder.await();
            return threadHolder.get();
        } finally {
            syncMap.invalidate(sequence);
        }
    }

    public void async(String bizType, Object object) throws IOException {
        long sequence = snowflakeIdWorker.nextId();
        try {
            inspectService.inspect(bizType, sequence, object);
        } catch (RpcException e) {
            throw new IOException(e);
        }
    }

    public void query(long sequence) throws IOException {
        try {
            inspectService.query(sequence);
        } catch (RpcException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.inspectService = applicationContext.getBean(InspectService.class);
        this.redisson = applicationContext.getBean(RedissonClient.class);
        this.redisTemplate = applicationContext.getBean("redisTemplate", RedisTemplate.class);
        Assert.notNull(redisson, "请初始化RedissonClient");
        Assert.notNull(redisTemplate, "请初始化RedisTemplate");
        Assert.notNull(inspectService, "请初始化Dubbo reference : InspectService");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public boolean isLocalSync(Long sequence) {
        return syncMap.asMap().containsKey(sequence);
    }

    public void signal(Long sequence, Object data) {

    }

    public MessageBody convertMessage(Message message) {
        return (MessageBody) redisTemplate.getValueSerializer().deserialize(message.getBody());
    }



    class ThreadHolder {
        private final Long    sequence;
        private       Integer timeout;
        private       Object  data;
        private       RLock   lock;


        public ThreadHolder(Long sequence, Integer timeout) {
            this.timeout = timeout;
            this.sequence = sequence;
            this.lock = redisson.getLock(Coasts.CACHE.SYNC_SWITCH + sequence);
        }

        public void await() throws TimeoutException {

        }

        public Object get() {
            return data;
        }
    }
}
