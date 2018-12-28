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
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class InspectClient implements InitializingBean, ApplicationContextAware {
    Logger log = LoggerFactory.getLogger(getClass());
    private Cache<Long, ThreadHolder> syncMap = CacheBuilder.newBuilder().maximumSize(100000).expireAfterWrite(10000, TimeUnit.SECONDS).build();

    private SnowflakeIdWorker  snowflakeIdWorker = new SnowflakeIdWorker(0, 0);
    private ApplicationContext applicationContext;
    private InspectService     inspectService;
    private RedissonClient     redisson;
    private RedisTemplate      redisTemplate;

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
        try {
            syncMap.get(sequence, new Callable<ThreadHolder>() {
                @Override
                public ThreadHolder call() throws Exception {
                    return null;
                }
            }).signal(data) ;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public MessageBody convertMessage(Message message) {
        return (MessageBody) redisTemplate.getValueSerializer().deserialize(message.getBody());
    }


    class ThreadHolder {
        private Long      sequence;
        private Condition condition;
        private Integer   timeout;
        private Object    data;
        private Lock      lock;


        public ThreadHolder(Long sequence, Integer timeout) {
            this.timeout = timeout;
            this.sequence = sequence;
            this.lock = new ReentrantLock();
            this.condition = lock.newCondition();
        }

        public void await() throws TimeoutException {
            try {
                lock.lock();
                if (!condition.await(timeout, TimeUnit.MILLISECONDS)) {
                    throw new TimeoutException();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                //这一行基本不会出问题，异常吃了吧
            } finally {
                lock.unlock();
            }
        }

        public Object get() {
            return data;
        }

        public void signal(Object data) {
            try {
                lock.lock();
                this.data = data;
                this.condition.signal();
            }finally {
                lock.unlock();
            }
        }
    }
}
