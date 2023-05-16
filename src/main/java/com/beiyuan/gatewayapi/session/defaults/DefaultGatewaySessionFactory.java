package com.beiyuan.gatewayapi.session.defaults;

import com.beiyuan.gatewayapi.session.Configuration;
import com.beiyuan.gatewayapi.session.GatewaySession;
import com.beiyuan.gatewayapi.session.GatewaySessionFactory;
import com.beiyuan.gatewayapi.socket.GatewaySocketServer;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 启动服务器
 * @author: beiyuan
 * @date: 2023/5/14  18:46
 */
public class DefaultGatewaySessionFactory implements GatewaySessionFactory {

    private final Logger logger= LoggerFactory.getLogger(DefaultGatewaySessionFactory.class);

    private final Configuration configuration;

    public DefaultGatewaySessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public GatewaySession openSession(){
        return new DefaultGatewaySession(configuration);
    }


    //这个是第二章节的
//    @Override
//    public Future<Channel> openSession() throws ExecutionException, InterruptedException {
//        GatewaySocketServer server=new GatewaySocketServer(configuration);
//        Future<Channel> future = Executors.newFixedThreadPool(2).submit(server);
//        //打印的是服务器是否创建成功的信息
//        Channel channel=future.get();
//        if(future==null){
//            throw new RuntimeException("gateway server start error with channel is null");
//        }
//        //等待启动完成
//        while (!channel.isActive()){
//            logger.info("gateway is starting, Please wait...");
//            Thread.sleep(500);
//        }
//        logger.info("gateway sever is started  with :{} ",channel.localAddress());
//        return future;
//    }
}
