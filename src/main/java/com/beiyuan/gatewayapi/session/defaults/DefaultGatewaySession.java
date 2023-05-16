package com.beiyuan.gatewayapi.session.defaults;

import com.beiyuan.gatewayapi.datasource.Connection;
import com.beiyuan.gatewayapi.datasource.DataSource;
import com.beiyuan.gatewayapi.datasource.DataSourceFactory;
import com.beiyuan.gatewayapi.datasource.unpooled.UnpooledDataSourceFactory;

import com.beiyuan.gatewayapi.mapping.IGenericReference;
import com.beiyuan.gatewayapi.session.Configuration;
import com.beiyuan.gatewayapi.session.GatewaySession;


/**
 * @author: beiyuan
 * @date: 2023/5/15  9:58
 */
public class DefaultGatewaySession implements GatewaySession {


    private  Configuration configuration;

    public DefaultGatewaySession(Configuration configuration, DataSource dataSource, String uri) {
        this.configuration = configuration;
        this.dataSource = dataSource;
        this.uri = uri;
    }

    private DataSource dataSource;

    private String uri;

    /**
     * 通过dubbo 的rpc 真正去调用目标方法
     *
     * @param args
     * @return
     */
    @Override
    public Object getTargetMethodResult(String methodName, Object[] args) {

        Connection connection=dataSource.getConnection();
        //返回结果类型为String
        return connection.execute(methodName,new String[]{"java.lang.String"},args);
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
