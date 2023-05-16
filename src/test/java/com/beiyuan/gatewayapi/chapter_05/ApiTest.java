package com.beiyuan.gatewayapi.chapter_05;

import com.beiyuan.gatewayapi.http.HttpCommandType;
import com.beiyuan.gatewayapi.http.HttpStatement;
import com.beiyuan.gatewayapi.session.Configuration;
import com.beiyuan.gatewayapi.session.defaults.DefaultGatewaySessionFactory;
import com.beiyuan.gatewayapi.socket.GatewaySocketServer;
import io.netty.channel.Channel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author: beiyuan
 * @date: 2023/5/15  14:36
 */

public class ApiTest {

    Logger logger= LoggerFactory.getLogger(getClass());


    //和chapter_03的一模一样
    //这里是创建网关服务器并启动。

    @Test
    public void test() throws ExecutionException, InterruptedException {
        //这些配置应该是服务提供者在连接上网关时就注册好

        //访问http://localhost:7397/session/medicine?mid=3&mname=kkk   一个对象参数  Medicine有mid,mname属性，并有set方法
        HttpStatement httpStatement1=new HttpStatement(
                "api-gateway-test",
                "/session/medicine",
                "com.beiyuan.gateway.rpc.IActivity",
                "getCount",
                "com.beiyuan.gateway.entity.Medicine",
                HttpCommandType.GET
                );
        //http://localhost:7397/session/test?name=kkk     单个参数
        HttpStatement httpStatement2=new HttpStatement(
                "api-gateway-test",
                "/session/test",
                "com.beiyuan.gateway.rpc.IActivity",
                "sayHi",
                "java.lang.String",
                HttpCommandType.GET
        );

        HttpStatement httpStatement3=new HttpStatement(
                "api-gateway-test",
                "/session/noargs",
                "com.beiyuan.gateway.rpc.IActivity",
                "noArgs",
                "",
                HttpCommandType.GET
        );
        //添加到配置
        Configuration configuration=new Configuration();
        configuration.addMapperProxyFactory(httpStatement1);
        configuration.addMapperProxyFactory(httpStatement2);
        configuration.addMapperProxyFactory(httpStatement3);

        //通过配置创建会话工厂
        DefaultGatewaySessionFactory gatewaySessionFactory=new DefaultGatewaySessionFactory(configuration);

        //线程类.将会话传给服务器。这里应该设置为可添加多个
        GatewaySocketServer socketServer=new GatewaySocketServer(gatewaySessionFactory);
        //启动服务器
        Future<Channel> future = Executors.newFixedThreadPool(2).submit(socketServer);
        Channel channel= future.get();

        if(channel==null){
            throw  new RuntimeException("socket server start failed");
        }
        while (!channel.isActive()){
            logger.info("socket server is starting....");
            TimeUnit.MILLISECONDS.sleep(100);
        }
        logger.info("socket server started with : {}",channel.localAddress());
        TimeUnit.DAYS.sleep(1);
    }


}
