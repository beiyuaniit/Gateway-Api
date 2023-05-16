package com.beiyuan.gatewayapi.session.defaults;

import com.beiyuan.gatewayapi.datasource.DataSource;
import com.beiyuan.gatewayapi.datasource.DataSourceFactory;
import com.beiyuan.gatewayapi.datasource.unpooled.UnpooledDataSourceFactory;
import com.beiyuan.gatewayapi.session.Configuration;
import com.beiyuan.gatewayapi.session.GatewaySession;
import com.beiyuan.gatewayapi.session.GatewaySessionFactory;

/**
 * 启动服务器
 * @author: beiyuan
 * @date: 2023/5/14  18:46
 */
public class DefaultGatewaySessionFactory implements GatewaySessionFactory {

    private final Configuration configuration;

    public DefaultGatewaySessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public GatewaySession openSession(String uri){
        DataSourceFactory dataSourceFactory=new UnpooledDataSourceFactory();
        dataSourceFactory.SetProperties(configuration,uri);
        return new DefaultGatewaySession(configuration,dataSourceFactory.getDataSource(),uri);
    }


}
