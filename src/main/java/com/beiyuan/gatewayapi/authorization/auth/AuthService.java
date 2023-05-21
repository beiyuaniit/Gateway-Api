package com.beiyuan.gatewayapi.authorization.auth;

import com.beiyuan.gatewayapi.authorization.GatewayAuthorizingToken;
import com.beiyuan.gatewayapi.authorization.IAuth;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

/**
 * 实现认证服务
 * @author: beiyuan
 * @date: 2023/5/20  23:46
 */
public class AuthService implements IAuth {

    /*
    验证的整个流程信息
     */
    private Subject subject;

    public AuthService() {
        //进行初始化配置  shiro.ini中可添加验证类
        Factory<SecurityManager> factory=new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager=factory.getInstance();
        //设置的是一个static属性
        SecurityUtils.setSecurityManager(securityManager);
        //
        this.subject = SecurityUtils.getSubject();
    }

    @Override
    public boolean validate(String id, String token) {
        try {
            //身份验证
            subject.login(new GatewayAuthorizingToken(id,token));//会拿token去已经注册的realm中验证
            //subject.login(new UsernamePasswordToken(id,token));
            //返回结果
            //subject每次验证结果都可能不同，所以是subject的更新isAuthenticated()  还是每次创建AuthService？估计前者
            //
            return subject.isAuthenticated();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        finally {
            //退出
            subject.logout();
        }
    }

}
