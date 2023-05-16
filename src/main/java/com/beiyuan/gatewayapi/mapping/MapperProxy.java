package com.beiyuan.gatewayapi.mapping;

import com.beiyuan.gatewayapi.session.GatewaySession;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 映射调用代理类，负责调用映射方法
 * @author: beiyuan
 * @date: 2023/5/15  10:01
 */
public class MapperProxy implements MethodInterceptor {
    private final String uri;

    private final GatewaySession gatewaySession;


    public MapperProxy(String uri, GatewaySession gatewaySession) {
        this.uri = uri;
        this.gatewaySession = gatewaySession;
    }


    //拦截代理类的方法,进行代理
    /**
     * 代理类正常流程
     * --前置工作:...
     * --调用目标对象的目标方法方法: methodProxy.invoke(o,args);
     * --后置工作:...
     * 设置返回的结果 return result;
     */
    /*
      但是现在只有本地代理类，并没有本地的目标代理对象
      所以泛化代理对象还要通过底层的rpc来获取目标方法的结果（如dubbo，可更换其他rpc框架，所以做到了泛化）
      将目标方法的结果设置给泛化代理对象的方法结果
     */
    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        MapperMethod mapperMethod=new MapperMethod(uri,gatewaySession.getConfiguration());
        return mapperMethod.execute(gatewaySession,(Map<String, Object>) args[0]);
    }
}
