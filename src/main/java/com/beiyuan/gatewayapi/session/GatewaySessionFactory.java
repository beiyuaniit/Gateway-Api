package com.beiyuan.gatewayapi.session;


/**
 * @author: beiyuan
 * @date: 2023/5/14  18:37
 */
public interface GatewaySessionFactory {

    //打开会话，简称开启gateway服务器
   GatewaySession openSession(String uri);
}
