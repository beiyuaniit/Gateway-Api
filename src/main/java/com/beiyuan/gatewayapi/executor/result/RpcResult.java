package com.beiyuan.gatewayapi.executor.result;

import com.beiyuan.gatewayapi.protocol.http.HttpConstants;

/**
 * 响应结果类
 * @author: beiyuan
 * @date: 2023/5/20  15:24
 */
public class RpcResult {
    private String code;

    private String info;

    private Object data;

    //只能通过子类、成功、失败创建实例
    protected RpcResult(String code, String info, Object data) {
        this.code = code;
        this.info = info;
        this.data = data;
    }

    //fastjson的JSON.toJSONBytes(result, SerializerFeature.PrettyFormat)会调用toString() 不重写会输出地址？不用重写，因为fastjson是通过反序列获取
//    @Override
//    public String toString() {
//        return "GatewayResult{" +
//                "code='" + code + '\'' +
//                ", info='" + info + '\'' +
//                ", data=" + data +
//                '}';
//    }

    public static RpcResult success(HttpConstants.ResponseCode responseCode,Object data){
        return new RpcResult(responseCode.getCode(), responseCode.getInfo(), data);}

    //public static RpcResult failure(Object data){return new RpcResult("0001","invoke failure...",data);}

    public static RpcResult failure(HttpConstants.ResponseCode responseCode,Object data){
        return new RpcResult(responseCode.getCode(), responseCode.getInfo(), data);}

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public Object getData() {
        return data;
    }


}
