package com.beiyuan.gatewayapi.session;

import com.beiyuan.gatewayapi.mapping.MapperRegistry;
import com.beiyuan.gatewayapi.http.HttpStatement;
import com.beiyuan.gatewayapi.mapping.IGenericReference;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置类，用于存放代理对象，服务配置，http和rpc的绑定等信息
 * dubbo的配置
 * @author: beiyuan
 * @date: 2023/5/14  16:31
 */
public class Configuration {

   //private final GenericReferenceRegistry registry=new GenericReferenceRegistry(this);


    private final MapperRegistry mapperRegistry=new MapperRegistry(this);

    //http请求信息。key是uri
    private final Map<String, HttpStatement>httpStatementMap=new HashMap<>();

    //服务配置项，每一个服务对应一个配置
    private final Map<String, ApplicationConfig>applicationConfigMap=new HashMap<>();

    //注册中心配置
    private final Map<String, RegistryConfig>registryConfigMap=new HashMap<>();

    //泛化服务配置
    private final Map<String, ReferenceConfig<GenericService>>referenceConfigMap=new HashMap<>();

    public Configuration() {
        ApplicationConfig application=new ApplicationConfig();
        application.setName("api-gateway-test");
        application.setQosEnable(false);//是否开启服务质量

        //zookeeper配置
        RegistryConfig registry=new RegistryConfig();
        registry.setAddress("zookeeper://localhost:2181");
        registry.setRegister(false);
        /*
        register="true"时才能通过zookeeper找到相应接口的实现（由provider配置）
        如果配置成register="false"，表示当前系统的服务不发布到注册中心，部署到生产后，web端会无法启动服务，在使用该接口的controller里会报错。
         */

        //要调用的接口
        ReferenceConfig<GenericService>reference=new ReferenceConfig<>();
        reference.setInterface("com.beiyuan.gateway.rpc.IActivity");
        reference.setVersion("1.0.0");
        reference.setGeneric("true");  //设置为泛化接口

        applicationConfigMap.put("api-gateway-test",application);
        registryConfigMap.put("api-gateway-test",registry);
        referenceConfigMap.put("com.beiyuan.gateway.rpc.IActivity",reference);

    }

//    public void addGenericReference(String application, String interfaceName, String methodName){
//        registry.addGenericReference(application,interfaceName,methodName);
//    }

    public void addMapperProxyFactory(HttpStatement httpStatement){
        mapperRegistry.addMapperProxyFactory(httpStatement);
    }
    public void addHttpStatement(HttpStatement httpStatement){
        httpStatementMap.put(httpStatement.getUri(),httpStatement);
    }

    public HttpStatement getHttpStatement(String uri){
        return httpStatementMap.get(uri);
    }

    public ApplicationConfig getApplicationConfig(String applicationName) {
        return applicationConfigMap.get(applicationName);
    }

    public RegistryConfig getRegistryConfig(String applicationName) {

        return registryConfigMap.get(applicationName);
    }

    public ReferenceConfig<GenericService> getReferenceConfig(String interfaceName) {
        return referenceConfigMap.get(interfaceName);
    }
    public IGenericReference getGenericReference(String uri,GatewaySession gatewaySession) {
        return mapperRegistry.getGenericReference(uri,gatewaySession);
    }
}
