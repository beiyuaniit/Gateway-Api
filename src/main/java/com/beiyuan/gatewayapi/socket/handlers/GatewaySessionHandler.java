package com.beiyuan.gatewayapi.socket.handlers;

import com.beiyuan.gatewayapi.executor.result.RpcResult;
import com.beiyuan.gatewayapi.protocol.http.HttpConstants;
import com.beiyuan.gatewayapi.protocol.http.HttpStatement;
import com.beiyuan.gatewayapi.protocol.http.ResponseRender;
import com.beiyuan.gatewayapi.protocol.http.RequestParser;
import com.beiyuan.gatewayapi.session.Configuration;

import com.beiyuan.gatewayapi.socket.BaseHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 参数处理器，请求进行参数解析
 * @author: beiyuan
 * @date: 2023/5/13  21:40
 */

public class GatewaySessionHandler extends BaseHandler<FullHttpRequest> {
    /*
    public interface FullHttpRequest extends HttpRequest, FullHttpMessage
    里面有 HttpRequest的所有方法

     */
    /*
    LoggerFactory.getLogger() 方法通过适配器模式（Adapter Pattern）对各种日志框架进行了封装，从而实现了对多种日志框架的兼容
    LoggerFactory.getLogger(Class<?> clazz) 方法的参数通常是一个 Class 对象，
    用于表示当前正在输出日志的类。通过这种方式，可以在日志中记录下当前日志记录所在的类的名称，方便后续的日志分析和处理
     */
   private final Logger logger= LoggerFactory.getLogger(GatewaySessionHandler.class);


   //已经有人创建好了
   private  final Configuration configuration;

    public GatewaySessionHandler(Configuration configuration) {
        this.configuration=configuration;
    }


    @Override
    protected void session(ChannelHandlerContext ctx, Channel channel, FullHttpRequest request){

        try {
            logger.info("gateway received a request: uri={},method={}",request.uri(),request.method());

            //解析请求参数并获取uri
            String uri= RequestParser.getUri(request);

            //设置httpStatement到channel域
            HttpStatement httpStatement=configuration.getHttpStatement(uri);
            channel.attr(HttpConstants.HTTP_STATEMENT).set(httpStatement);

            //放行
            request.retain();
            //fireChannelRead表示传递消息至下一个处理器，不传递后面的handler永远无法收到
            ctx.fireChannelRead(request);

        }catch (Exception e){
            e.printStackTrace();
            FullHttpResponse response=ResponseRender.reder(RpcResult.failure(HttpConstants.ResponseCode._500,null));
            ctx.writeAndFlush(response);
        }
    }
}
