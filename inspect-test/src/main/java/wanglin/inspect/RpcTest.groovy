package wanglin.inspect

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

class RpcTest {
//    RPC原理代码
    public void rpc(){
        Proxy.newProxyInstance(Thread.class.getClassLoader(),[SomeBizInterface.class] as Class[] ,new InvocationHandler() {
            @Override
            Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //服务发现
//                    负载均衡，容错，……
                //http...socket .....
                //序列化
                return null
            }
        })
    }
    interface SomeBizInterface{

    }
}
