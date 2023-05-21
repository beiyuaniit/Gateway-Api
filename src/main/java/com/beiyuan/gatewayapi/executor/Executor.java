package com.beiyuan.gatewayapi.executor;

import com.beiyuan.gatewayapi.executor.result.RpcResult;
import com.beiyuan.gatewayapi.protocol.http.HttpStatement;

import java.util.Map;

/**
 * 执行器，负责对数据源进行调用
 * @author: beiyuan
 * @date: 2023/5/20  15:30
 */
public interface Executor {

    RpcResult execute(HttpStatement httpStatement, Map<String,Object>params) throws Exception;
}
