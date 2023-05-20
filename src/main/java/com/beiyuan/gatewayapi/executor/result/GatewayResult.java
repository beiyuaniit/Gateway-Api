package com.beiyuan.gatewayapi.executor.result;

/**
 * 响应结果类
 * @author: beiyuan
 * @date: 2023/5/20  15:24
 */
public class GatewayResult {
    private String code;

    private String info;

    private Object data;

    //只能通过子类、成功、失败创建实例
    protected GatewayResult(String code, String info, Object data) {
        this.code = code;
        this.info = info;
        this.data = data;
    }

    //fastjson的JSON.toJSONBytes(result, SerializerFeature.PrettyFormat)会调用toString() 不重写会输出地址？
    @Override
    public String toString() {
        return "GatewayResult{" +
                "code='" + code + '\'' +
                ", info='" + info + '\'' +
                ", data=" + data +
                '}';
    }

    public static GatewayResult success(Object data){return new GatewayResult("0000","invoke success...",data);}

    public static GatewayResult failure(Object data){return new GatewayResult("0001","invoke failure...",data);}

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
