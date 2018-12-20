package wanglin.inspect;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class InspectClient {
    Cache<Long, ThreadHolder> syncMap = CacheBuilder.newBuilder().maximumSize(100000).expireAfterWrite(1000, TimeUnit.SECONDS).build();

    SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0, 0);
    InspectService    inspectService;

    public Object sync(String bizType, Integer timeout, Object object) throws TimeoutException {
        long sequence = snowflakeIdWorker.nextId();
        inspectService.inspect(bizType, sequence, object);
        ThreadHolder threadHolder = new ThreadHolder(timeout);
        try {
            threadHolder.await();
            return threadHolder.get();
        }catch (InterruptedException e){
            throw new RuntimeException(e);//todo
        }
    }

    public Object async() {
        return null;
    }

    public boolean isSync(long sequence) {
        return null != syncMap.getIfPresent(sequence);
    }

    public void signal(long sequence, Object data) {
        syncMap.getIfPresent(sequence).signal(data);
    }

    class ThreadHolder {
        private Integer   timeout;
        private Object    data;
        private Condition condition;


        public ThreadHolder(Integer timeout) {
            this.timeout = timeout;
            this.condition = new ReentrantLock().newCondition();
        }

        public void signal(Object data) {
            this.data = data;
            this.condition.signal();
        }

        public void await() throws InterruptedException {
            this.condition.await(timeout,TimeUnit.MILLISECONDS);
        }

        public Object get() throws TimeoutException {
            return data;
        }
    }
}
