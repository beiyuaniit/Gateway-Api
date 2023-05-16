package com.beiyuan.gatewayapi.socket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ExecutionException;

/**
 * 消息处理程序（真正的处理）入站处理器
 * @author: beiyuan
 * @date: 2023/5/13  21:42
 */
public abstract class BaseHandler<T> extends SimpleChannelInboundHandler<T> {

    /**
     * protected 只能被子类访问
     * @param ctx           the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *                      belongs to
     * @param msg           the message to handle
     * @throws Exception
     * Is called for each message of type T.
     * Params:
     * ctx – the ChannelHandlerContext which this SimpleChannelInboundHandler belongs to msg – the message to handle
     * Throws:
     * Exception – is thrown if an error occurred
     */
    /*
    channelRead 是public 类型，可以被外部访问；而channelRead0是protected类型，只能被当前类及其子类访问
    channelRead 中调用了 channelRead0，其会先做消息类型检查，判断当前message 是否需要传递到下一个handler。
    在源码中channelRead做了一个资源的释放，很好的对数据做出了处理
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, T msg) throws Exception {
        //之前一直没有调用这句话，难怪没有输出
        session(ctx,ctx.channel(),msg);
    }

    /**
     * request请求的会话session管理。（入站
     * @param ctx
     * @param channel
     * @param request
     */
    protected abstract void session(ChannelHandlerContext ctx, final Channel channel,T request) throws ExecutionException, InterruptedException;
}
