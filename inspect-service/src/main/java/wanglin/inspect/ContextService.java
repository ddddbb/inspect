package wanglin.inspect;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class ContextService {
    ConcurrentMap<Long,InspectContext> cache = new ConcurrentHashMap<>();
    public void saveInspectContext(InspectContext context) {
        saveToCache(context);
        saveToRedis(context);
    }


    protected InspectContext getInspectContext(long sequence) {
        InspectContext context = getFromCache(sequence);
        if(null == context) {
            context = getFromRedis(sequence);
        }
        if(null == context){
            throw new RuntimeException("无此检测快照");
        }
        return context;
    }

    private void saveToCache(InspectContext context) {
        cache.put(context.id,context);
    }

    private void saveToRedis(InspectContext context) {

    }

    private InspectContext getFromCache(Long sequence) {
        return cache.get(sequence);
    }

    private InspectContext getFromRedis(Long sequence) {
        return null;
    }


}
