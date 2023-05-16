package com.beiyuan.gatewayapi.chapter_04;

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
    //访问localhost:7397/session/test
    @Test
    public void test() throws ExecutionException, InterruptedException {
        //这些配置应该是服务提供者在连接上网关时就注册好
        HttpStatement httpStatement=new HttpStatement(
                "api-gateway-test",
                "com.beiyuan.gateway.rpc.IActivity",
                "sayHi",
                "/session/test",
                HttpCommandType.GET
        );

        //添加到配置
        Configuration configuration=new Configuration();
        configuration.addMapperProxyFactory(httpStatement);

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
