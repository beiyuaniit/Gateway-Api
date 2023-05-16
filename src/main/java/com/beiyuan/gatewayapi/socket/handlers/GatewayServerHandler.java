package com.beiyuan.gatewayapi.socket.handlers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.beiyuan.gatewayapi.http.RequestParser;
import com.beiyuan.gatewayapi.mapping.IGenericReference;
import com.beiyuan.gatewayapi.session.GatewaySession;
import com.beiyuan.gatewayapi.session.GatewaySessionFactory;
import com.beiyuan.gatewayapi.session.defaults.DefaultGatewaySessionFactory;
import com.beiyuan.gatewayapi.socket.BaseHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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





        //设置消息体
        String methodName= request.uri().substring(1);
        //这些方法名要提前在服务器注册好


        //根据rpc调用服务的结果创建了本地的代理类
        //IGenericReference reference= sessionFactory.getGenericReference(methodName);
        //通过本地代理类进行调用。本地代理类内再通过本地创建的dubbo代理类取调用服务
        //String result=reference.$invoke("test kkkkk");

        GatewaySession gatewaySession = sessionFactory.openSession(uri);
        IGenericReference reference=gatewaySession.getProxy();

        Object result=null;
        try {
            result=reference.$invoke(params)+"   "+new Date().getTime();
        }catch (Exception e){
            e.printStackTrace();
        }




        //设置响应
        DefaultFullHttpResponse response=new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);//OK是200
//        String content="your had visited the gateway of beiyaun with uri="+request.uri();
//        //先设置消息体
//        response.content().writeBytes(JSON.toJSONBytes(content, SerializerFeature.PrettyFormat));
        response.content().writeBytes(JSON.toJSONBytes(result,SerializerFeature.PrettyFormat));
        //设置头部
        HttpHeaders headers=response.headers();
        //响应类型   HttpHeaderValues.APPLICATION_JSON="application/json"
        headers.add(HttpHeaderNames.CONTENT_TYPE,HttpHeaderValues.APPLICATION_JSON+"; charset=UTF-8");
        //长度
        //    /**
        //     * Returns the number of readable bytes which is equal to
        //     * {@code (this.writerIndex - this.readerIndex)}.
        //     */
        //    public abstract int readableBytes();
        headers.add(HttpHeaderNames.CONTENT_LENGTH,response.content().readableBytes());
        //长连接
        headers.add(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
        //设置可跨域访问
        // 配置跨域访问
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS,"*");
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS,"GET, POST, PUT, DELETE");
        /*
        客户端代码必须将 XMLHttpRequest 上的 withCredentials 属性设置为 true 以授予权限.
        但是，仅此标头是不够的.服务器必须以 Access-Control-Allow-Credentials 标头响应.使用此标头响应 true 意味着服务器允许将 cookie(或其他用户凭据)包含在跨域请求中.
        您还需要确保 如果您希望跨域凭据请求正常工作，您的浏览器不会阻止第三方 cookie.
        请注意，无论您是发出同源请求还是跨源请求，您都需要保护您的网站免受 CSRF 的影响(尤其是当您的请求包含 cookie 时).
         */
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS,true);

        //写回响应给同一pipeline中的其他handler
        ctx.writeAndFlush(response);
    }
}
