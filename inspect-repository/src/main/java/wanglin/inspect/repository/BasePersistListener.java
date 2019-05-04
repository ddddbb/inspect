package wanglin.inspect.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import wanglin.inspect.BizTypeService;
import wanglin.inspect.DataService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BasePersistListener implements ApplicationContextAware {
    protected Logger log = LoggerFactory.getLogger(getClass());


    protected ConcurrentMap<String, DataService>    dataServiceCache = new ConcurrentHashMap<>();
    protected ConcurrentMap<String, BizTypeService> bizServiceCache  = new ConcurrentHashMap<>();

    protected DataService getDataService(String beanName) {
        DataService ds = dataServiceCache.get(beanName);
        return ds;
    }

    protected BizTypeService getBizTypeService(String beanName) {
        BizTypeService ds = bizServiceCache.get(beanName);
        return ds;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, DataService> dsBeans = applicationContext.getBeansOfType(DataService.class);
        dsBeans.forEach((k, bean) -> {
            dataServiceCache.put(bean.name(), bean);
        });
        Map<String, BizTypeService> btBeans = applicationContext.getBeansOfType(BizTypeService.class);
        btBeans.forEach((k, bean) -> {
            bizServiceCache.put(bean.name(), bean);
        });
    }

}

