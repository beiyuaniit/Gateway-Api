package com.beiyuan.gatewayapi.datasource.request.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.netty.handler.codec.http.*;

/**
 * 渲染响应
 * @author: beiyuan
 * @date: 2023/5/20  16:33
 */
public class ResponseRender {

    public static FullHttpResponse reder(Object result){
        //设置响应
        DefaultFullHttpResponse response=new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);//OK是200
//        String content="your had visited the gateway of beiyaun with uri="+request.uri();
//        //先设置消息体
//        response.content().writeBytes(JSON.toJSONBytes(content, SerializerFeature.PrettyFormat));
        response.content().writeBytes(JSON.toJSONBytes(result, SerializerFeature.PrettyFormat));
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


        return response;

    }
}
