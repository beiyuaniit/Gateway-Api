package com.beiyuan.gatewayapi.session;

import io.netty.channel.Channel;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author: beiyuan
 * @date: 2023/5/14  18:37
 */
public interface GatewaySessionFactory {

    //打开会话，简称开启gateway服务器
   GatewaySession openSession();
}
