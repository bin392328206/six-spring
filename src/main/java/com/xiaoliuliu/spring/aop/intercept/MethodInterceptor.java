package com.xiaoliuliu.spring.aop.intercept;

/**
 * @author 小六六
 * @version 1.0
 * @date 2020/10/12 9:59
 */
public interface MethodInterceptor {
    Object invoke(MethodInvocation invocation) throws Throwable;
}
