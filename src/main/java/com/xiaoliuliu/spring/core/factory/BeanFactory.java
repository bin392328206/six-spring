package com.xiaoliuliu.spring.core.factory;

/**
 * @author 小六六
 * @version 1.0
 * @date 2020-10-11 11:00
 * @des 这个接口也是Spring ioc的核心接口呢
 */
public interface BeanFactory {

    Object getBean(String name) throws Exception;

    <T> T getBean(Class<T> requiredType) throws Exception;
}
