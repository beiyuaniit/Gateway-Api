package com.beiyuan.gatewayapi.chapter_08;

import com.beiyuan.gatewayapi.authorization.IAuth;
import com.beiyuan.gatewayapi.authorization.JwtUtil;
import com.beiyuan.gatewayapi.authorization.auth.AuthService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: beiyuan
 * @date: 2023/5/21  12:42
 */
public class ShiroTest {

    /**
     * 测试token生成
     */
    @Test
    public void testCreateJwt(){
        String issuer="beiyuan";
        long ttlMillis= 7 * 24 * 60 * 60 * 1000L;//单位是毫秒a。。
        Map<String,Object>claims=new HashMap<>();
        claims.put("key","kkk");

        //根据用户名生成token密文
        String jwt = JwtUtil.encode(issuer, ttlMillis, claims);
        System.out.println(jwt);//eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiZWl5dWFuIiwiZXhwIjoxNjg1MjYxMjY0LCJrZXkiOiJra2sifQ.nBXrmzqc4WOqW7fR9NnlNUSr7kv4YhJBxiNuCJ3T9uU

        //根据密文返回用户信息
        System.out.println(JwtUtil.decode(jwt).getSubject());//beiyuan
    }

    /**
     * 通过testCreateJwt生成的密文进行验证。
     */
    @Test
    public void test_AuthService(){
        IAuth auth=new AuthService();
        boolean validate=auth.validate("beiyuan","eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiZWl5dWFuIiwiZXhwIjoxNjg1MjYxMjY0LCJrZXkiOiJra2sifQ.nBXrmzqc4WOqW7fR9NnlNUSr7kv4YhJBxiNuCJ3T9uU");
        System.out.println(validate);
    }

    /**
     * 日常使用。与本项目无关
     */
    @Test
    public void test_Shiro(){
        Factory<SecurityManager> factory=new IniSecurityManagerFactory("classpath:test07_shiro.ini");
        SecurityManager manager = factory.getInstance();
        SecurityUtils.setSecurityManager(manager);

        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token=new UsernamePasswordToken("beiyuan","kkk");
        try {
            subject.login(token);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("login fail..");
        }finally {
            subject.logout();
        }
        System.out.println(subject.isAuthenticated());
    }
}
