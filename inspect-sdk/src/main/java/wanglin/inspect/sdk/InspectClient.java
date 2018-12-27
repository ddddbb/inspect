package wanglin.inspect.sdk;

import com.alibaba.dubbo.rpc.RpcException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import wanglin.inspect.InspectService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class InspectClient implements InitializingBean, ApplicationContextAware {
    private Cache<Long, ThreadHolder> syncMap = CacheBuilder.newBuilder().maximumSize(100000).expireAfterWrite(1000, TimeUnit.SECONDS).build();

    private SnowflakeIdWorker  snowflakeIdWorker = new SnowflakeIdWorker(0, 0);
    private ApplicationContext applicationContext;
    private InspectService     inspectService;

    public Object sync(String bizType, Integer timeout, Object object) throws IOException, TimeoutException {
        long sequence = snowflakeIdWorker.nextId();
        try {
            inspectService.inspect(bizType, sequence, object);
        } catch (RpcException e) {
            throw new IOException(e);
        }
        ThreadHolder threadHolder = new ThreadHolder(timeout);
        threadHolder.await();
        return threadHolder.get();

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


    public boolean isSync(long sequence) {
        return null != syncMap.getIfPresent(sequence);
    }

    public void signal(long sequence, Object data) {
        syncMap.getIfPresent(sequence).signal(data);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.inspectService = applicationContext.getBean(InspectService.class);
        Assert.notNull(inspectService,"请初始化Dubbo reference : InspectService");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    class ThreadHolder {
        private final ReentrantLock lock;
        private       Integer       timeout;
        private       Object        data;
        private       Condition     condition;


        public ThreadHolder(Integer timeout) {
            this.timeout = timeout;
            this.lock = new ReentrantLock();
            this.condition = lock.newCondition();
        }

        public void signal(Object data) {
            this.data = data;
            this.condition.signal();
        }

        public void await() throws TimeoutException {
            try {
                lock.lock();
                if (!this.condition.await(timeout, TimeUnit.MILLISECONDS)) {
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
    }
}
