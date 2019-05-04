package wanglin.inspect.factory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import wanglin.inspect.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DataServiceFactory implements ApplicationContextAware {
    ConcurrentMap<String, DataService> cache = new ConcurrentHashMap<>();
    @Autowired
    InspectService                    inspectService;


    public DataService get(String dataService) {
        return cache.get(dataService);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, DataService> beans = applicationContext.getBeansOfType(DataService.class);
        beans.forEach((id, bean) -> {
            cache.put(bean.name(), bean);
        });
        cache.put("requestDataService", new RequestDataService());
        cache.put("typeDataService", new TypeDataService());
    }


    class RequestDataService implements DataService {
        @Override
        public void fetch(Long sequence, String varName, BizType bizType, Object request) {
            if (varName.equals(Coasts.CTX.REQUEST)) {
                inspectService.varNotify(sequence, Coasts.CTX.REQUEST, request);
            }
        }


        @Override
        public String name() {
            return "requestDataService";
        }
    }

    class TypeDataService implements DataService {
        @Override
        public void fetch(Long sequence, String varName, BizType bizType, Object request) {
            if (varName.equals(Coasts.CTX.BIZ_TYPE)) {
                inspectService.varNotify(sequence, Coasts.CTX.BIZ_TYPE, bizType);
            }
        }



        @Override
        public String name() {
            return "typeDataService";
        }
    }
}
