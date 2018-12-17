package wanglin.inspect;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class ContextService {
    ConcurrentMap<String,InspectContext> cache = new ConcurrentHashMap<>();
    public void saveInspectContext(InspectContext context) {
        saveToCache(context);
        saveToRedis(context);
    }


    protected InspectContext getInspectContext(String uuid) {
        InspectContext context = getFromCache(uuid);
        if(null == context) {
            context = getFromRedis(uuid);
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

    private InspectContext getFromCache(String uuid) {
        return cache.get(uuid);
    }

    private InspectContext getFromRedis(String uuid) {
        return null;
    }


}
