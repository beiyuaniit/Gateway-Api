package com.beiyuan.gatewayapi.datasource.connection;

import com.beiyuan.gatewayapi.datasource.Connection;
import io.netty.handler.codec.http.HttpHeaderValues;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * @author: beiyuan
 * @date: 2023/5/16  12:39
 */
public class HttpConnection implements Connection {

    private final HttpClient httpClient;

    private PostMethod postMethod;

    public HttpConnection(String uri){
        httpClient=new HttpClient();
        postMethod=new PostMethod(uri);

        postMethod.addRequestHeader("connection", "Keep-Alive");
        postMethod.addRequestHeader("Content-Type", "application/json;charset=GBK");
        postMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");



    }


    @Override
    public Object execute(String method, String[] parameterType, Object[] args) {
        //这里其实并没有用到方法名，方法参数，参数。http请求只是请求了一个路径
        String result=null;
        try{
            int code=httpClient.executeMethod(postMethod);
            if(code==200){
                result=postMethod.getResponseBodyAsString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
