package com.beiyuan.gatewayapi.session;

import com.alibaba.fastjson.JSON;
import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InterfaceMaker;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;
import org.junit.Test;
import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * @author: beiyuan
 * @date: 2023/5/13  23:30
 */

public class SessionServerTest  implements MethodInterceptor { //方法拦截器是cglib要用到的

    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    Configuration configuration=new Configuration();

//    @Test
//    public void TestSessionServer01() throws ExecutionException, InterruptedException {
//        GatewaySocketServer server=new GatewaySocketServer(configuration);
//        Future<Channel> future = Executors.newFixedThreadPool(2).submit(server);//线程池执行服务线程
//        Channel channel=future.get();
//        if(channel==null){
//            throw new RuntimeException("Session server start failed with channel is null");
//        }
//        //成功了但是未启动完成，等待启动完成
//        while (!channel.isActive()){
//            logger.info("Server starting...");
//            Thread.sleep(500);
//        }
//        logger.info("Session server started with address {}",channel.localAddress());
//
//        Thread.sleep(Long.MAX_VALUE);
//    }


    /**
     * 这里是直接通过dubbo到注册中心进行rpc调用，并没有通过gateway
     */
    @Test
    public void DubboRcpTest(){
        //设置要访问的服务及接口
        ApplicationConfig application=new ApplicationConfig();
        application.setName("api-gateway-test");
        application.setQosEnable(false);

        RegistryConfig registry=new RegistryConfig();
        registry.setAddress("zookeeper://localhost:2181");
        registry.setRegister(false);

        ReferenceConfig<GenericService>reference=new ReferenceConfig<>();
        reference.setInterface("com.beiyuan.gateway.rpc.IActivity");
        reference.setVersion("1.0.0");
        reference.setGeneric("true");

        //同过dubbo线程进行调用
        DubboBootstrap bootstrap=DubboBootstrap.getInstance()
                .application(application).registry(registry).reference(reference);
        bootstrap.start();

        //通过缓存获取调用服务
        ReferenceConfigCache cache=ReferenceConfigCache.getCache();
        GenericService genericService=cache.get(reference);
        //进行调用
        Object result=genericService.$invoke("sayHi",new String[]{"java.lang.String"},new Object[]{"hello world"});

        System.out.println(result);
    }


    /**
     * 通过cglib创建代理类去调用，也没有通过网关。不过这里还要连接上zookeeper，现在只是本地调用
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return JSON.toJSONString(objects);
    }
    @Test
    public void CglibRpcTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //创建接口
        InterfaceMaker interfaceMaker=new InterfaceMaker();
        interfaceMaker.add(new Signature("sayHi", Type.getType(String.class),new Type[]{Type.getType(String.class)}),null);
        Class<?>interfaceClass=interfaceMaker.create();

        //创建代理类
        Enhancer enhancer=new Enhancer();
        enhancer.setCallback(this);  //返回到当前对象
        enhancer.setSuperclass(Object.class);
        enhancer.setInterfaces(new Class[]{interfaceClass});
        Object proxy=enhancer.create();

        //调用方法
        Method method=proxy.getClass().getMethod("sayHi",String.class);
        Object result=method.invoke("hi i am kafka");

        System.out.println(result);


    }



    /**
     * 真正的网关调用，调用者通过http请求到网关，网关通过rpc调用服务，最后通过http返回结果
     * 3个，主程序那边是gateway服务器，这里是调用者，提供者已经提供好了服务在gateway
     */
    //这里只是启动了下网关服务器
//    @Test
//    public void GatewayTest() throws ExecutionException, InterruptedException {
//        Configuration config=new Configuration();
//        config.addGenericReference("api-gateway-test","com.beiyuan.gateway.rpc.IActivity","sayHi");
//
//        GenericReferenceSessionFactoryBuilder builder=new GenericReferenceSessionFactoryBuilder();
//        Future<Channel> future = builder.build(configuration);
//
//        logger.info("gateway server started :"+future.get().localAddress());
//
//        TimeUnit.DAYS.sleep(1);
//    }

}
