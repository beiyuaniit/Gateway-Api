package com.beiyuan.gatewayapi.datasource.request.http;

/**
 * http请求信息
 * @author: beiyuan
 * @date: 2023/5/15  9:59
 */
public class HttpStatement {

    private String applicationName;

    private String interfaceName;

    private String methodName;

    private String uri;

    private HttpCommandType commandType;

    private String paramterType;
    public HttpStatement(String applicationName, String uri,String interfaceName,
                         String methodName, String paramterType, HttpCommandType commandType) {
        this.applicationName = applicationName;
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.uri = uri;
        this.commandType = commandType;
        this.paramterType=paramterType;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getParamterType() {
        return paramterType;
    }



    public String getInterfaceName() {
        return interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getUri() {
        return uri;
    }

    public HttpCommandType getCommandType() {
        return commandType;
    }
}
