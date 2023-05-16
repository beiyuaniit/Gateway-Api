package com.beiyuan.gatewayapi.session.defaults;

import com.beiyuan.gatewayapi.datasource.Connection;
import com.beiyuan.gatewayapi.datasource.DataSource;
import com.beiyuan.gatewayapi.datasource.DataSourceFactory;
import com.beiyuan.gatewayapi.datasource.unpooled.UnpooledDataSourceFactory;

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


    private DataSource dataSource;

    private String uri;

    private  Configuration configuration;

    public DefaultGatewaySession(Configuration configuration, DataSource dataSource, String uri) {
        this.configuration = configuration;
        this.dataSource = dataSource;
        this.uri = uri;
    }

    /**
     * 通过dubbo 的rpc 真正去调用目标方法
     * @return
     */
    @Override
    public Object getTargetMethodResult(String methodName, Map<String,Object>params) {

        Connection connection=dataSource.getConnection();
        String parameterTypes=configuration.getHttpStatement(uri).getParamterType();
        /*
         *只支持单参数就这样写
         * 因为本地没有远程调用的接口，如果多参数且是对象的话，转化会很复杂
         * (允许）： 无参
         * (允许)：java.lang.String
         * (允许)：cn.beiyaun.gateway.rpc.entity.Medicine
         * (拒绝)：com.beiyuan.gateway.rpc.dto.Medicine,java.lang.String —— 不提供多参数方法的处理
         * */

        //处理无参的方法
        if(parameterTypes==null || parameterTypes==""){
            return connection.execute(methodName,null,null);
        }

        //有参的
        return connection.execute(methodName,
                new String[]{parameterTypes},
                SimpleTypeUtil.isSimpleType(parameterTypes) ? params.values().toArray() : new Object[]{params});

        //返回结果类型为Object
        //多参数调用思路。这里好像参数名并没有什么用。主要拿参数类型和参数值。位置一一对应
        //return connection.execute(methodName,paramterTypes,params.values().toArray(new Object[0]));


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
