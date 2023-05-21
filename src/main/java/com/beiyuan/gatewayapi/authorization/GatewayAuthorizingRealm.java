package com.beiyuan.gatewayapi.authorization;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * Realm：领域
 * 完成验证和授权
 * @author: beiyuan
 * @date: 2023/5/20  23:51
 */
public class GatewayAuthorizingRealm extends AuthorizingRealm {


    //之前少写了这个方法，一直报错。。
    @Override
    public Class<?> getAuthenticationTokenClass() {
        return GatewayAuthorizingToken.class;
    }

    /**
     * Author:授权（权利）
     * @param principalCollection 登陆后的主体
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //暂时不做授权处理
        return null;
    }

    /**
     * 通过传来的token和本地用户信息比对
     * Authen：认证（登陆）
     * @param token  （前端传来的token）
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        try {
            JwtUtil.decode(((GatewayAuthorizingToken)token).getJwt());
        }catch (Exception e){
            throw new RuntimeException("validation failed wiht "+e.getMessage());
        }
        //将验证好的用户id和密文和Realm名组合成一个验证后的信息
        return new SimpleAuthenticationInfo(token.getPrincipal(),token.getCredentials(),this.getName());
    }

}







