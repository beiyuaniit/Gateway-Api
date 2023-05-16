package com.beiyuan.gatewayapi.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 不DefaultGatewaySession不是只支持单参数，目前这个类没用到
 * 基本类型工具类。
 * @author: beiyuan
 * @date: 2023/5/16  17:37
 */
public class SimpleTypeUtil {

    private static final Set<String>SAMPLE_TYPES_SET= new HashSet<>();
    static {

        SAMPLE_TYPES_SET.add(String.class.getName());
        SAMPLE_TYPES_SET.add(Boolean.class.getName());
        SAMPLE_TYPES_SET.add(Byte.class.getName());
        SAMPLE_TYPES_SET.add(Character.class.getName());
        SAMPLE_TYPES_SET.add(Short.class.getName());
        SAMPLE_TYPES_SET.add(Integer.class.getName());//java.lang.Integer
        SAMPLE_TYPES_SET.add(Float.class.getName());
        SAMPLE_TYPES_SET.add(Long.class.getName());
        SAMPLE_TYPES_SET.add(Double.class.getName());

        SAMPLE_TYPES_SET.add(Date.class.getName());
        SAMPLE_TYPES_SET.add(Class.class.getName());
        SAMPLE_TYPES_SET.add(BigInteger.class.getName());
        SAMPLE_TYPES_SET.add(BigDecimal.class.getName());

    }
    //私有构造方法，不用创建对象
    private SimpleTypeUtil(){}

    //判断是否是常用包装类型，包括基本类型和其他的
    public static boolean isSimpleType(String typeName){
        return SAMPLE_TYPES_SET.contains(typeName);
    }
}
