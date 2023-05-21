package com.beiyuan.gatewayapi.socket.handlers;

import com.beiyuan.gatewayapi.executor.result.RpcResult;
import com.beiyuan.gatewayapi.mapping.IGenericReference;
import com.beiyuan.gatewayapi.protocol.http.HttpConstants;
import com.beiyuan.gatewayapi.protocol.http.RequestParser;
import com.beiyuan.gatewayapi.protocol.http.ResponseRender;
import com.beiyuan.gatewayapi.session.GatewaySession;
import com.beiyuan.gatewayapi.session.defaults.DefaultGatewaySessionFactory;
import com.beiyuan.gatewayapi.socket.BaseHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * rpc处理器，负责调用远程服务
 * @author: beiyuan
 * @date: 2023/5/21  21:06
 */
public class RpcHandler extends BaseHandler<FullHttpRequest> {

    private final DefaultGatewaySessionFactory sessionFactory;

    public RpcHandler(DefaultGatewaySessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    protected void session(ChannelHandlerContext ctx, Channel channel, FullHttpRequest request) throws ExecutionException, InterruptedException {

        //设置消息体
        //这些方法名要提前在服务器注册好
        //根据rpc调用服务的结果创建了本地的代理类
        //IGenericReference reference= sessionFactory.getGenericReference(methodName);
        //通过本地代理类进行调用。本地代理类内再通过本地创建的dubbo代理类取调用服务
        //String result=reference.$invoke("test kkkkk");

        //获取远程连接源
        try {
            //参数解析
            RequestParser parser=new RequestParser(request);
            String uri=parser.getUri();
            Map<String, Object> params = parser.parse();

            if(params.containsKey("uid")){
                params.remove("uid");
            }
            if(params.containsKey("token")){
                params.remove("token");
            }

            //调用会话服务，完成rpc
            GatewaySession gatewaySession = sessionFactory.openSession(uri);
            //获取本地代理对象
            IGenericReference reference=gatewaySession.getProxy();
            //通过本地调用远程获取结果
            Object result=reference.$invoke(params);

            //渲染结果,写回响应给同一pipeline中的其他handler
            FullHttpResponse response= ResponseRender.reder(RpcResult.success(HttpConstants.ResponseCode._200,result));
            //writeAndFlush是输出。。
            ctx.writeAndFlush(response);

        }catch (Exception e){
            e.printStackTrace();
            FullHttpResponse response=ResponseRender.reder(RpcResult.failure(HttpConstants.ResponseCode._502,null));
            ctx.writeAndFlush(response);
        }
    }
}
