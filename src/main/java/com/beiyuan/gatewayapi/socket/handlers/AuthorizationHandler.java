package com.beiyuan.gatewayapi.socket.handlers;

import com.beiyuan.gatewayapi.executor.result.RpcResult;
import com.beiyuan.gatewayapi.protocol.http.HttpConstants;
import com.beiyuan.gatewayapi.protocol.http.HttpStatement;
import com.beiyuan.gatewayapi.protocol.http.RequestParser;
import com.beiyuan.gatewayapi.protocol.http.ResponseRender;
import com.beiyuan.gatewayapi.session.Configuration;
import com.beiyuan.gatewayapi.socket.BaseHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author: beiyuan
 * @date: 2023/5/21  18:59
 */
public class AuthorizationHandler extends BaseHandler<FullHttpRequest> {

    private final Configuration configuration;

    public AuthorizationHandler(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void session(ChannelHandlerContext ctx, Channel channel, FullHttpRequest request) throws ExecutionException, InterruptedException {
        //错误的话直接返回响应
        try{
            //从channel域中获取
            HttpStatement httpStatement=channel.attr(HttpConstants.HTTP_STATEMENT).get();
            if(httpStatement.needAuth()){
                //直接从请求中拿属性
                //String uid=request.headers().get("uid");
                //String token=request.headers().get("token");
                Map<String, Object> params = new RequestParser(request).parse();
                String uid=(String) params.get("uid");
                String token=(String) params.get("token");
                if(uid==null || uid=="" || token==null || token==""){
                    FullHttpResponse response= ResponseRender.reder(RpcResult.failure(HttpConstants.ResponseCode._400,null));
                    ctx.writeAndFlush(response);
                }
                if(configuration.authValidate(uid, token)){
                    ctx.fireChannelRead(request.retain());
                }else {
                    FullHttpResponse response=ResponseRender.reder(RpcResult.failure(HttpConstants.ResponseCode._403,null));
                    ctx.writeAndFlush(response);
                }
            }else {
                //不用验证直接放行
                /*
                 如果请求了WebSocket协议升级，则增加引用计数（调用retain()方 法 ），并将它传递给下一个ChannelInboundHandler
                 如果该HTTP请求指向了地址为/ws的URI，那么HttpRequestHandler将调用FullHttpRequest对象上的retain()方法
                 并通过调用fireChannelRead(msg)方法将它转发给下一个ChannelInboundHandler。之所以需要调用retain()方法，
                 是因为调用channelRead()方法完成之后，它将调用FullHttpRequest对象上的release()方法以释放它的资源
                 */
                /*
                对于retain()方法的调用是必需的，因为当channelRead0()方法返回时，TextWebSocketFrame的引用计数将会被减少。由于所有的操作都是异步的，‘
                因此，writeAndFlush()方法可能会在channelRead0()方法返回之后完成，而且它绝对不能访问一个已经失效的引用。
                 */
                //这里也没判断是不是ws啊。。
                request.retain();//增加消息的引用计数，并将它写到ChannelGroup中所有已经连接的客户端
                ctx.fireChannelRead(request);
            }
        }catch (Exception e){
            FullHttpResponse response=ResponseRender.reder(RpcResult.failure(HttpConstants.ResponseCode._403,null));
            ctx.writeAndFlush(response);
        }
    }

}
