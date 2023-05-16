package com.beiyuan.gatewayapi.datasource;

/**
 * @author: beiyuan
 * @date: 2023/5/16  15:06
 */
public interface Connection {
    public Object execute(String method, String []parameterType,Object[]args);
}
