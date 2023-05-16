package com.beiyuan.gatewayapi.datasource.connection;

import com.beiyuan.gatewayapi.datasource.Connection;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;

/**
 * 将本来放在DefaultGatewaySession的Dubbo调用rpc抽出来放在这里为数据源
 * @author: beiyuan
 * @date: 2023/5/16  12:39
 */
public class DubboConnection implements Connection {

    private GenericService genericService;

    //一个配置要设置一个数据源。。
    public DubboConnection(ApplicationConfig application, RegistryConfig registry, ReferenceConfig<GenericService> reference) {
        //通过Dubbo进行对服务提供者进行远程方法调用
        DubboBootstrap bootstrap = DubboBootstrap.getInstance();


        bootstrap.application(application).registry(registry).reference(reference).start();
        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        genericService = cache.get(reference);


    }

    @Override
    public Object execute(String method, String[] parameterType, Object[] args) {
        if (genericService == null) {
            throw new RuntimeException("please init the DubboConnection");
        }
        return genericService.$invoke(method, parameterType, args);
    }
}
