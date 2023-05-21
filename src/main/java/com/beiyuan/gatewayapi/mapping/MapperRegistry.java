package com.beiyuan.gatewayapi.mapping;

import com.beiyuan.gatewayapi.protocol.http.HttpStatement;
import com.beiyuan.gatewayapi.session.Configuration;
import com.beiyuan.gatewayapi.session.GatewaySession;

import java.util.HashMap;
import java.util.Map;

/**
 * 泛化调用注册器
 * 注册到Configuration
 * @author: beiyuan
 * @date: 2023/5/15  10:02
 */
public class MapperRegistry {

    private final Configuration configuration;

    private final Map<String,MapperProxyFactory> mapperProxyFactoryMap=new HashMap<>();
    public MapperRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    //通过mapper工厂返回代理类。包含了目标方法的结果
    public IGenericReference getGenericReference(String uri, GatewaySession gatewaySession){
        MapperProxyFactory mapperProxyFactory = mapperProxyFactoryMap.get(uri);

        if(mapperProxyFactory==null){
            //工厂都还没创建。所以要先执行addMapperProxyFactory()
            throw  new RuntimeException("Uri: {}"+uri+"'s MapperProxyFactory is not created");
        }

        try{

            return mapperProxyFactory.newInstance(gatewaySession);
        }catch (Exception e){
            throw new RuntimeException("Error to get mapper instance "+ e,e );
        }
    }

    //注册mapper工厂
    public void addMapperProxyFactory(HttpStatement httpStatement){
        String uri=httpStatement.getUri();
        if(hasMapperProxyFactory(uri)){
            throw new RuntimeException("The MapperProxyFactory of "+"Uri: "+uri+" is already registered");
        }
        mapperProxyFactoryMap.put(uri,new MapperProxyFactory(uri));

        //保存刚注册好mapper工厂的uri的HttpStatement到配置中
        configuration.addHttpStatement(httpStatement);
    }

    public boolean hasMapperProxyFactory(String uri){
        return mapperProxyFactoryMap.containsKey(uri);
    }

}
