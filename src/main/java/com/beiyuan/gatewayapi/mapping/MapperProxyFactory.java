package com.beiyuan.gatewayapi.mapping;

import com.beiyuan.gatewayapi.protocol.http.HttpStatement;
import com.beiyuan.gatewayapi.session.GatewaySession;
import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InterfaceMaker;
import org.objectweb.asm.Type;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 主要用来创建泛化调用代理对象
 * @author: beiyuan
 * @date: 2023/5/15  10:02
 */
public class MapperProxyFactory {
    private String uri;

    public MapperProxyFactory(String uri) {
        this.uri = uri;
    }

    //uri和它的代理类
    private final Map<String,IGenericReference> referenceCache=new ConcurrentHashMap<>();

    public IGenericReference newInstance(GatewaySession session){

        return referenceCache.computeIfAbsent(uri,value->{
            //获取http请求信息
            HttpStatement httpStatement=session.getConfiguration().getHttpStatement(uri);
            //泛化调用
            MapperProxy mapperProxy=new MapperProxy(uri,session);
            //创建接口
            InterfaceMaker interfaceMaker=new InterfaceMaker();
            interfaceMaker.add(new Signature(httpStatement.getMethodName(), Type.getType(String.class),
                    new Type[]{Type.getType(String.class)}),null);
            Class<?> interfaceClass = interfaceMaker.create();//创建接口并返回类型

            //创建代理对象
            Enhancer enhancer=new Enhancer();
            enhancer.setSuperclass(Object.class);
            //这里是要代理的接口，IGenericReference.class要算上，之前忘了
            enhancer.setInterfaces(new Class[]{interfaceClass,IGenericReference.class});
            //create()创建代理类完成后回调mapperProxy的intercept
            //回调方法去执行
            //cglib创建目标代理类，创建完成后在回调类中通过dubbo的rpc完成远程方法调用，获得结果

            /**
             * MapperProxyFactory通过cglib创建目标方法的代理类。
             * 代理类创建结束后在回调方法里创建dubbo的rpc代理类，并获取rpc结果
             * IGenericReference
             */
            enhancer.setCallback(mapperProxy);
           // System.out.println(enhancer.create());
            return (IGenericReference) enhancer.create();
        });
    }

}
