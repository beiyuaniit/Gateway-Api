package com.beiyuan.gatewayapi.executor;

import com.beiyuan.gatewayapi.datasource.Connection;
import com.beiyuan.gatewayapi.session.Configuration;
import com.beiyuan.gatewayapi.util.SimpleTypeUtil;

/**
 * @author: beiyuan
 * @date: 2023/5/20  16:13
 */
public class SimpleExecutor extends BaseExecutor {
    public SimpleExecutor(Configuration configuration, Connection connection) {
        super(configuration, connection);
    }

    @Override
    protected Object doExecute(String methodName, String[] paramsTypes, Object[] args) {


        /*
         *只支持单参数就这样写
         * 因为本地没有远程调用的接口，如果多参数且是对象的话，转化会很复杂
         * (允许）： 无参
         * (允许)：java.lang.String
         * (允许)：cn.beiyaun.gateway.rpc.entity.Medicine
         * (拒绝)：com.beiyuan.gateway.rpc.dto.Medicine,java.lang.String —— 不提供多参数方法的处理
         * */

        //处理无参的方法
        if(paramsTypes==null || paramsTypes.length==0){
            return connection.execute(methodName,null,null);
        }
        //有参的
        return connection.execute(methodName,
                paramsTypes,
                args);

        //返回结果类型为Object
        //多参数调用思路。这里好像参数名并没有什么用。主要拿参数类型和参数值。位置一一对应
        //return connection.execute(methodName,paramterTypes,params.values().toArray(new Object[0]));

    }
}
