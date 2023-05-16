package com.beiyuan.gatewayapi.session.defaults;

import com.beiyuan.gatewayapi.http.HttpStatement;
import com.beiyuan.gatewayapi.mapping.IGenericReference;
import com.beiyuan.gatewayapi.session.Configuration;
import com.beiyuan.gatewayapi.session.GatewaySession;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;


/**
 * @author: beiyuan
 * @date: 2023/5/15  9:58
 */
public class DefaultGatewaySession implements GatewaySession {


    private final Configuration configuration;

    public DefaultGatewaySession(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 通过dubbo 的rpc 真正去调用目标方法
     * @param uri
     * @param args
     * @return
     */
    @Override
    public Object getTargetMethodResult(String uri, Object args) {
        //获取http信息
        HttpStatement httpStatement=configuration.getHttpStatement(uri);
        String applicationName=httpStatement.getApplicationName();
        String interfaceName=httpStatement.getInterfaceName();

        //获取配置
        ApplicationConfig applicationConfig=configuration.getApplicationConfig(applicationName);
        RegistryConfig registryConfig=configuration.getRegistryConfig(applicationName);
        ReferenceConfig<GenericService> referenceConfig=configuration.getReferenceConfig(interfaceName);


        //dubbo调用目标方法
        DubboBootstrap bootstrap=DubboBootstrap.getInstance();
        bootstrap.application(applicationConfig).registry(registryConfig).reference(referenceConfig).start();

        //获取泛化调用服务
        ReferenceConfigCache cache=ReferenceConfigCache.getCache();
        GenericService genericService=cache.get(referenceConfig);
        //返回结果类型为String
        return genericService.$invoke(httpStatement.getMethodName(),
                new String[]{"java.lang.String"},new Object[]{" kafka "});
    }

    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }

    @Override
    public IGenericReference getProxy(String uri) {
        return configuration.getGenericReference(uri,this);
    }
}
