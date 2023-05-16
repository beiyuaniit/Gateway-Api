package com.beiyuan.gatewayapi.session;

import com.beiyuan.gatewayapi.mapping.IGenericReference;

/**
 *
 * 将调用的配置、过程、结果等保存在会话中
 * @author: beiyuan
 * @date: 2023/5/15  9:56
 */
public interface GatewaySession {
    Object getTargetMethodResult(String uri,Object args);

    Configuration getConfiguration();

    IGenericReference getProxy(String uri);
}
