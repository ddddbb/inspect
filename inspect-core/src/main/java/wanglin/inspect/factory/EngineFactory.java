package wanglin.inspect.factory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import wanglin.inspect.EngineEnum;
import wanglin.inspect.EngineService;
import wanglin.inspect.engine.ELEngineService;
import wanglin.inspect.engine.GroovyEngineService;

public class EngineFactory implements InitializingBean, ApplicationContextAware, BeanClassLoaderAware {

    private ApplicationContext applicationContext;
    private ClassLoader        classLoader;
    ELEngineService     el;
    GroovyEngineService groovy;

    public EngineService get(EngineEnum engine) {
        if (null == engine) return el;
        if (engine.equals(EngineEnum.SCRIPT)) return groovy;
        return el;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        groovy = new GroovyEngineService(applicationContext,classLoader);
        el = new ELEngineService();
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
