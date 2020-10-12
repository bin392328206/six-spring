package com.xiaoliuliu.spring.aop.aspect;

/**
 * @author 小六六
 * @version 1.0
 * @date 2020/10/12 10:20
 * 异常通知
 */

import com.xiaoliuliu.spring.aop.intercept.MethodInterceptor;
import com.xiaoliuliu.spring.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

public class AfterThrowingAdviceInterceptor extends AbstractAspectAdvice implements MethodInterceptor {


    private String throwingName;

    public AfterThrowingAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        try {
            //直接调用下一个拦截器，如果不出现异常就不调用异常通知
            return mi.proceed();
        } catch (Throwable e) {
            //异常捕捉中调用通知方法
            invokeAdviceMethod(mi, null, e.getCause());
            throw e;
        }
    }

    public void setThrowName(String throwName) {
        this.throwingName = throwName;
    }
}