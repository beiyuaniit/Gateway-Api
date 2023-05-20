package com.beiyuan.gatewayapi.socket.handlers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.beiyuan.gatewayapi.datasource.request.http.ResponseRender;
import com.beiyuan.gatewayapi.http.RequestParser;
import com.beiyuan.gatewayapi.mapping.IGenericReference;
import com.beiyuan.gatewayapi.session.GatewaySession;

import com.beiyuan.gatewayapi.session.defaults.DefaultGatewaySessionFactory;
import com.beiyuan.gatewayapi.socket.BaseHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;


/**
 * session处理器，对请求进行设置
 * @author: beiyuan
 * @date: 2023/5/13  21:40
 */

public class GatewayServerHandler extends BaseHandler<FullHttpRequest> {
    /*
    public interface FullHttpRequest extends HttpRequest, FullHttpMessage
    里面有 HttpRequest的所有方法

     */
    /*
    LoggerFactory.getLogger() 方法通过适配器模式（Adapter Pattern）对各种日志框架进行了封装，从而实现了对多种日志框架的兼容
    LoggerFactory.getLogger(Class<?> clazz) 方法的参数通常是一个 Class 对象，
    用于表示当前正在输出日志的类。通过这种方式，可以在日志中记录下当前日志记录所在的类的名称，方便后续的日志分析和处理
     */
   private final Logger logger= LoggerFactory.getLogger(GatewayServerHandler.class);


   //已经有人创建好了
   private  final DefaultGatewaySessionFactory sessionFactory;

    public GatewayServerHandler(DefaultGatewaySessionFactory sessionFactory) {
        this.sessionFactory=sessionFactory;
    }


    @Override
    protected void session(ChannelHandlerContext ctx, Channel channel, FullHttpRequest request){

        logger.info("gateway received a request: uri={},method={}",request.uri(),request.method());

        //解析请求参数
        Map<String, Object> params = new RequestParser(request).parse();
        //舍弃uri ?以及之后的参数
        String uri=request.uri();
        int idx=uri.indexOf("?");
        if(idx>0){
            uri=uri.substring(0,idx);
        }

        if(uri.equals("/favicon.ico")){
            //uri前面的小图标，没有的话浏览器会自动请求一个
            //设置有的话就自己写回一个图标
            return;
        }




        //设置消息体
        String methodName= request.uri().substring(1);
        //这些方法名要提前在服务器注册好


        //根据rpc调用服务的结果创建了本地的代理类
        //IGenericReference reference= sessionFactory.getGenericReference(methodName);
        //通过本地代理类进行调用。本地代理类内再通过本地创建的dubbo代理类取调用服务
        //String result=reference.$invoke("test kkkkk");

        //获取远程连接源
        try {
            GatewaySession gatewaySession = sessionFactory.openSession(uri);
            //获取本地代理对象
            IGenericReference reference=gatewaySession.getProxy();

            Object result=null;
            try {
                //通过本地调用远程获取结果
                result=reference.$invoke(params)+"   "+new Date().getTime();
            }catch (Exception e){
                e.printStackTrace();
            }

            //渲染结果
            FullHttpResponse response= ResponseRender.reder(result);
            //写回响应给同一pipeline中的其他handler
            ctx.writeAndFlush(response);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
