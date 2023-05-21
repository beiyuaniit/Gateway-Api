package com.beiyuan.gatewayapi.authorization;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt工具类，生成token
 * @author: beiyuan
 * @date: 2023/5/20  23:47
 */
public class JwtUtil {

    private static final String signKey="B*B^5Fe";//类似于md5中的salt，自定义。增加加密的随机性

    /**
     * 根据信息生成密文token
     * @param issuer   发行人
     * @param ttlMillis  过期时间（毫秒）
     * @param claims 所有权（额外信息）
     * @return
     */
    public static String encode(String issuer, long ttlMillis, Map<String,Object>claims){
        if(claims==null){
            claims=new HashMap<>();
        }

        long nowMillis=System.currentTimeMillis();
        Date now=new Date(nowMillis);

        JwtBuilder builder=Jwts.builder()
                //荷载部分
                .setClaims(claims)
                //签发时间
                .setExpiration(now)
                //签发人
                .setSubject(issuer)
                //生成签名的算法和密钥。ES256就报错，因为格式要求不一样
                //HS256 单向hash，不可逆
                .signWith(SignatureAlgorithm.HS256,signKey);

        //需要设置过期时间
        if(ttlMillis>0){
            long expMillis=nowMillis+ttlMillis;
            builder.setExpiration(new Date(expMillis));
        }

        //compact:契约。就是密文
        return builder.compact();
    }

    /**
     * 验证（不可逆算法居然能够解密？？？
     * 根据密文token进行验证
     * 失败抛出异常
     * @param token
     * @return
     */
    public static Claims decode(String token){
        return Jwts.parser()
                //密钥
                .setSigningKey(signKey)
                //需要验证的token
                .parseClaimsJws(token)
                //
                .getBody();
    }

}
