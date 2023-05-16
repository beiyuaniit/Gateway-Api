package com.beiyuan.gatewayapi.http;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 解析http请求中要调用的方法参数
 * @author: beiyuan
 * @date: 2023/5/16  17:28
 */
public class RequestParser {

    private final FullHttpRequest request;


    public RequestParser(FullHttpRequest request) {
        this.request = request;
    }

    private String getContentType() {
        //Optional处理空指针异常
        //request.headers().entries().stream().findAny() 每一步都可能为null，写判断的话要很过个if
        Optional<Map.Entry<String, String>> any = request.headers().entries().stream().findAny();
        Map.Entry<String, String> entry = any.orElse(null);//没有则null
        assert entry!=null;
        String contentType=entry.getValue();

        //??? 所以是什么格式呢
        int idx=contentType.indexOf(";");
        if(idx>0){
            return contentType.substring(0, idx);
        }else {
            return contentType;
        }

    }

    //从请求中解析除参数名和参数值
    public Map<String ,Object> parse(){
        String contentType=getContentType();

        HttpMethod method = request.method();//请求方式
        if(method==HttpMethod.GET){
            Map<String,Object> params=new HashMap<>();
            //将HTTP查询字符串拆分为路径字符串和键值参数对.get请求参数在路径
            QueryStringDecoder decoder=new QueryStringDecoder(request.uri());
            Map<String, List<String>> parameters = decoder.parameters();
            parameters.forEach((k,v)->{
                //这里规定同名参数只取第一个
                params.put(k,v.get(0));
            });
            return params;

        }else if (method==HttpMethod.POST){
            switch (contentType){
                case "application/json":
                    ByteBuf copy = request.content().copy();//将内容读出来
                    if(copy.isReadable()){
                        String content = copy.toString(StandardCharsets.UTF_8);//转化为String
                        return JSON.parseObject(content);//将字符串转换为对象
                    }
                    break;
                case "multipart/form-data":
                    Map<String,Object> params=new HashMap<>();
                    HttpPostRequestDecoder decoder=new HttpPostRequestDecoder(request);
                    decoder.offer(request);//对HttpContent进行解码
                    decoder.getBodyHttpDatas().forEach(value->{//返回一个List
                        Attribute attribute=(Attribute)value;//获取属性
                        try {
                            params.put(attribute.getName(), attribute.getValue());
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    });
                default:
                    break;
            }
        }

        throw new RuntimeException("The request method of " + method+" is not supported");


    }


}
