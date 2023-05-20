package com.beiyuan.gatewayapi.session.defaults;

import com.beiyuan.gatewayapi.datasource.Connection;
import com.beiyuan.gatewayapi.datasource.DataSource;


import com.beiyuan.gatewayapi.executor.Executor;
import com.beiyuan.gatewayapi.mapping.IGenericReference;
import com.beiyuan.gatewayapi.session.Configuration;
import com.beiyuan.gatewayapi.session.GatewaySession;
import com.beiyuan.gatewayapi.util.SimpleTypeUtil;

import java.util.Map;


/**
 * @author: beiyuan
 * @date: 2023/5/15  9:58
 */
public class DefaultGatewaySession implements GatewaySession {

    private  Configuration configuration;

    private String uri;


    private Executor executor;

    public DefaultGatewaySession(Configuration configuration, String uri,Executor executor) {
        this.configuration = configuration;
        this.uri = uri;
        this.executor=executor;
    }

    /**
     * 通过dubbo 的rpc 真正去调用目标方法
     * @return
     */
    @Override
    public Object getTargetMethodResult(String methodName, Map<String,Object>params) {
        try {
            return executor.execute(configuration.getHttpStatement(uri),params);
        } catch (Exception e) {
            throw new RuntimeException("Error to execute: "+e.getMessage());
        }
    }

    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }

    @Override
    public IGenericReference getProxy() {
        return configuration.getGenericReference(uri,this);
    }
}
