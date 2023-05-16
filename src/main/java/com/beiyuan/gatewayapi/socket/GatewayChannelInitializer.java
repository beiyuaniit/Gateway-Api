package com.beiyuan.gatewayapi.socket;

//不要导错包
import com.beiyuan.gatewayapi.session.defaults.DefaultGatewaySessionFactory;
import com.beiyuan.gatewayapi.socket.handlers.GatewayServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;


/**
 * @author: beiyuan
 * @date: 2023/5/13  22:52
 */
public class GatewayChannelInitializer extends ChannelInitializer<SocketChannel> {

    //不用这个配置了，直接用会话工厂
//    private final Configuration configuration;
//
//    public GatewayChannelInitializer(Configuration configuration) {
//        this.configuration = configuration;
//    }

    private final DefaultGatewaySessionFactory gatewaySessionFactory;

    public GatewayChannelInitializer(DefaultGatewaySessionFactory gatewaySessionFactory) {
        this.gatewaySessionFactory = gatewaySessionFactory;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline=socketChannel.pipeline();


        //类似于序列化和反序列化
        //添加请求解码器，解析http请求格式的消息
        pipeline.addLast(new HttpRequestDecoder());
        //添加响应编码器
        pipeline.addLast(new HttpResponseEncoder());
        //HttpObjectAggregator 是 Netty 提供的 HTTP 消息聚合器，通过它可以把 HttpMessage 和 HttpContent 聚合成一个 FullHttpRequest
        // 或者 FullHttpResponse(取决于是处理请求还是响应），方便我们使用。
        pipeline.addLast(new HttpObjectAggregator(1024*1024));
        //自定义的会话处理器
        pipeline.addLast(new GatewayServerHandler(gatewaySessionFactory));
    }

}
