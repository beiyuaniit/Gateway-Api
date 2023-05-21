package com.beiyuan.gatewayapi.authorization;

/**
 * @author: beiyuan
 * @date: 2023/5/20  23:38
 */
public interface IAuth {
    //接口中方法默认pulic，volidate有验证的意思
    boolean validate(String id,String token);
}
