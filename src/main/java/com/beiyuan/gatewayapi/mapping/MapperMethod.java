package com.beiyuan.gatewayapi.mapping;

import com.beiyuan.gatewayapi.http.HttpCommandType;
import com.beiyuan.gatewayapi.session.Configuration;
import com.beiyuan.gatewayapi.session.GatewaySession;

import java.util.Map;

/**
 * 绑定调用方法类型
 * @author: beiyuan
 * @date: 2023/5/15  10:00
 */
public class MapperMethod {

    String methodName;

    private final HttpCommandType commandType;


    //对每个方法绑定uri和调用方式
    public MapperMethod(String uri, Configuration configuration) {
        this.methodName=configuration.getHttpStatement(uri).getMethodName();
        this.commandType = configuration.getHttpStatement(uri).getCommandType();
    }

    //根据不同的请求类型执行不同的调用
    public Object execute(GatewaySession session, Map<String, Object> args){
        Object result = null;
        switch (commandType){
            case GET:
                //一开始这里没设置result。。。
                result=session.getTargetMethodResult(methodName,args);
                break;
            case POST:
                break;
            case PUT:
                break;
            case DELETE:
                break;
            default:
                throw new RuntimeException("Unknown http request type for :"+commandType);
        }
        return result;
    }
}
