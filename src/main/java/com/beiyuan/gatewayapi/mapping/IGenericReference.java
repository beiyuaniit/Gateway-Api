package com.beiyuan.gatewayapi.mapping;

import java.util.Map;

/**
 * 映射层，将
 * 泛化通用接口
 * @author: beiyuan
 * @date: 2023/5/14  13:30
 */
public interface IGenericReference {
    //标识符必须以字母、下划线_ 、美元符$开头

    //返回值类型用Object接收，不用指定
    Object  $invoke(Map<String,Object>params);

    /*
      Enhancer enhancer=new Enhancer();
      ...
      enhancer.setInterfaces(new Class[]{interfaceClass,IGenericReference.class});
      //enhancer.create();返回值类型GenericService。有个genericService.$invoke()方法
      //这个$invoke就相当于执行目标方法（或者就是？）IGenericReference的$invoke对 GenericService的$invoke进行了覆盖？
      //反正后续可以IGenericReference接口的代理类进行方法调用
      return (IGenericReference)enhancer.create();
     */
}
