package wanglin.inspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
public class MethodTimeActive {
    /**
     * 自定义map集合，key：方法名，value：[0：运行次数，1：总时间]
     */
    public static Map<String, Long[]> methodTest = new HashMap<String, Long[]>();


    @Around("execution(* wanglin.inspect..*(..))")
    public Object invoke(ProceedingJoinPoint invocation) throws Throwable {
        // 创建一个计时器
        StopWatch watch = new StopWatch();
        // 计时器开始
        watch.start();
        // 执行方法
        Object object = invocation.proceed();
        // 计时器停止
        watch.stop();
        // 方法名称
        String methodName = invocation.getSignature().getName();
        // 获取计时器计时时间
        Long time = watch.getTotalTimeMillis();
        if (methodTest.containsKey(methodName)) {
            Long[] x = methodTest.get(methodName);
            x[0]++;
            x[1] += time;
        } else {
            methodTest.put(methodName, new Long[]{1L, time});
        }
        return object;
    }
}
