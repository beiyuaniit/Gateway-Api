package com.beiyuan.gatewayapi.executor;

import com.beiyuan.gatewayapi.datasource.Connection;
import com.beiyuan.gatewayapi.executor.result.RpcResult;
import com.beiyuan.gatewayapi.protocol.http.HttpConstants;
import com.beiyuan.gatewayapi.protocol.http.HttpStatement;
import com.beiyuan.gatewayapi.session.Configuration;
import com.beiyuan.gatewayapi.util.SimpleTypeUtil;

import java.util.Map;

/**
 * @author: beiyuan
 * @date: 2023/5/20  16:04
 */
public abstract class BaseExecutor implements Executor {

    //用于doEcecute中调用

    protected Configuration configuration;

    protected Connection connection;

    public BaseExecutor(Configuration configuration, Connection connection) {
        this.configuration = configuration;
        this.connection = connection;
    }

    @Override
    public RpcResult execute(HttpStatement httpStatement, Map<String, Object> params) throws Exception {

        String methodName = httpStatement.getMethodName();
        String paramterType = httpStatement.getParamterType();
        //无参,不用传对象，null即可，否则会报错
        if(params.size()==0){
            try {
                Object data=doExecute(methodName,null,null);
                return RpcResult.success(HttpConstants.ResponseCode._200,data);
            }catch (Exception e){
                return RpcResult.failure(HttpConstants.ResponseCode._502,null);
            }

        }

        //有参数（一个参数，可以是对象）
        Object[]args= SimpleTypeUtil.isSimpleType(paramterType)?params.values().toArray():new Object[]{params};
        try {
            Object data=doExecute(methodName,new String[]{paramterType},
                    args);
            return RpcResult.success(HttpConstants.ResponseCode._200,data);
        }catch (Exception e){
            e.printStackTrace();
            return RpcResult.failure(HttpConstants.ResponseCode._500,null);
        }
    }
    
    protected abstract Object doExecute(String  methodName,String[]paramsTypes,Object[]args);
    
}
