package com.beiyuan.gatewayapi.chapter_06;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 这里rpc根本没有走网关
 * @author: beiyuan
 * @date: 2023/5/16  21:24
 */
public class RpcTest {

    @Test
    public void rpc_test(){
        ApplicationConfig application = new ApplicationConfig();
        application.setName("api-gateway-test");
        application.setQosEnable(false);

        RegistryConfig registry = new RegistryConfig();
        registry.setAddress("zookeeper://127.0.0.1:2181");
        registry.setRegister(false);

        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setInterface("com.beiyuan.gateway.rpc.IActivity");
        reference.setVersion("1.0.0");
        reference.setGeneric("true");

        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        bootstrap.application(application)
                .registry(registry)
                .reference(reference)
                .start();

        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        GenericService genericService = cache.get(reference);

        Map<String, Object> allRequestParams = new HashMap();
        allRequestParams.put("mid", "121");
        allRequestParams.put("mname", "drug");
        Object result = genericService.$invoke("getCount", new String[]{"com.beiyuan.gateway.entity.Medicine"}, new Object[]{allRequestParams});
        System.out.println(result+"kkk");
    }
}
