package com.beiyuan.gatewayapi.session;

import com.beiyuan.gatewayapi.mapping.IGenericReference;

import java.util.Map;

/**
 *
 * 将调用的配置、过程、结果等保存在会话中
 * @author: beiyuan
 * @date: 2023/5/15  9:56
 */
public interface GatewaySession {
    Object getTargetMethodResult(String method, Map<String,Object>params);

    Configuration getConfiguration();

    IGenericReference getProxy();
}
