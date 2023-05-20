package com.beiyuan.gatewayapi.executor;

import com.beiyuan.gatewayapi.datasource.Connection;
import com.beiyuan.gatewayapi.executor.result.GatewayResult;
import com.beiyuan.gatewayapi.http.HttpStatement;
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
    public GatewayResult execute(HttpStatement httpStatement, Map<String, Object> params) throws Exception {

        String methodName = httpStatement.getMethodName();
        String paramterType = httpStatement.getParamterType();
        //无参,不用传对象，null即可，否则会报错
        if(params.size()==0){
            try {
                Object data=doExecute(methodName,null,null);
                return GatewayResult.success(data);
            }catch (Exception e){
                return GatewayResult.failure("remote invoke failed..");
            }

        }

        //有参数（一个参数，可以是对象）
        Object[]args= SimpleTypeUtil.isSimpleType(paramterType)?params.values().toArray():new Object[]{params};
        try {
            Object data=doExecute(methodName,new String[]{paramterType},
                    args);
            return GatewayResult.success(data);
        }catch (Exception e){
            e.printStackTrace();
            return GatewayResult.failure(e.getMessage());
        }
    }
    
    protected abstract Object doExecute(String  methodName,String[]paramsTypes,Object[]args);
    
}
