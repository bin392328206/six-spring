package com.xiaoliuliu.spring.aop.intercept;



import com.xiaoliuliu.spring.aop.aspect.JoinPoint;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*    /**
     * @Des   在aspectj中 对应的 MethodInvocationProceedingJoinPoint 
     * @Author 小六六
     * @Date 2020/10/12 10:02
     * @Param
     * @Return
     外层拦截器对象，负责执行整个拦截器链，主要逻辑是：先遍历执行完拦截器链，最后才执行被代理的方法。这其实就是责任链模式的实现。
     */

public class MethodInvocation implements JoinPoint {

    /**代理对象*/
    private Object proxy;

    /**被代理对象的class*/
    private Class<?> targetClass;

    /**被代理的对象*/
    private Object target;

    /**被代理的方法*/
    private Method method;

    /**被代理的方法的入参*/
    private Object [] arguments;

    /**拦截器链*/
    private List<Object> interceptorsAndDynamicMethodMatchers;

    /**用户参数*/
    private Map<String, Object> userAttributes;

    /**记录当前拦截器执行的位置*/
    private int currentInterceptorIndex = -1;

    public MethodInvocation(Object proxy,
                            Object target,
                            Method method,
                            Object[] arguments,
                            Class<?> targetClass,
                            List<Object> interceptorsAndDynamicMethodMatchers) {

        this.proxy = proxy;
        this.target = target;
        this.targetClass = targetClass;
        this.method = method;
        this.arguments = arguments;
        this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
    }

    /**
     * 调度执行拦截器链
     */
    public Object proceed() throws Throwable {
        //拦截器执行完了，最后真正执行被代理的方法
        if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
            return this.method.invoke(this.target,this.arguments);
        }

        //获取一个拦截器
        Object interceptorOrInterceptionAdvice =
                this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
        if (interceptorOrInterceptionAdvice instanceof MethodInterceptor) {
            MethodInterceptor mi = (MethodInterceptor) interceptorOrInterceptionAdvice;
            //执行通知方法
            return mi.invoke(this);
        } else {
            //跳过，调用下一个拦截器
            return proceed();
        }
    }

    @Override
    public Object getThis() {
        return this.target;
    }

    @Override
    public Object[] getArguments() {
        return this.arguments;
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public void setUserAttribute(String key, Object value) {
        if (value != null) {
            if (this.userAttributes == null) {
                this.userAttributes = new HashMap<>();
            }
            this.userAttributes.put(key, value);
        }
        else {
            if (this.userAttributes != null) {
                this.userAttributes.remove(key);
            }
        }
    }

    @Override
    public Object getUserAttribute(String key) {
        return (this.userAttributes != null ? this.userAttributes.get(key) : null);
    }
}
