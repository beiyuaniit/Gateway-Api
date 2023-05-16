package com.beiyuan.gatewayapi.socket;

import com.beiyuan.gatewayapi.session.defaults.DefaultGatewaySessionFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;

/**
 * Session Server
 * 定义成了一个服务线程
 * @author: beiyuan
 * @date: 2023/5/13  23:04
 */
public class GatewaySocketServer implements Callable<Channel> {
    private Logger logger= LoggerFactory.getLogger(GatewaySocketServer.class);

    private final EventLoopGroup boss=new NioEventLoopGroup(1);//一个端口，所以一个boss就够了
    private final EventLoopGroup worker=new NioEventLoopGroup();

    private Channel channel;

    private final DefaultGatewaySessionFactory gatewaySessionFactory;

    public GatewaySocketServer(DefaultGatewaySessionFactory gatewaySessionFactory) {
        this.gatewaySessionFactory = gatewaySessionFactory;
    }


    @Override
    public Channel call() throws Exception {
        ChannelFuture channelFuture=null; //Callable线程执行的返回结果
        //打印的是创建服务器时的异常
        try{
            //创建服务器  bootstrap:引导
            ServerBootstrap bootstrap=new ServerBootstrap();
            bootstrap.group(boss,worker).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)  //全连接队列大小
                    .childHandler(new GatewayChannelInitializer(gatewaySessionFactory))//处理器
            ;
            //异步创建服务线程
            channelFuture = bootstrap.bind(new InetSocketAddress(7397)).syncUninterruptibly();//不会被打断
            this.channel=channelFuture.channel();//记录返回结果
        }catch (Exception e){
            logger.info("socket server start error :",e.getMessage());
        }finally {
            if(channelFuture!=null && channelFuture.isSuccess()){
                logger.info("socker server start successed");
            }else {
                logger.info("socker server start failed");
            }
        }

        return channel;
    }
}
