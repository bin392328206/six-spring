package com.xiaoliuliu.spring.aop;

/**
 * @author 小六六
 * @version 1.0
 * @date 2020/10/12 10:22
 * 顶层接口
 *
 * 总结一个aop的实现原理，首先我们知道aop最最底层的实现原理肯定是动态代理，在Spring中的话，就是我们在把Beandefinish变成spring bean的时候先先看看这个用动态代理生成
 * 代理对象，就比如说我们有接口的jdk动态代码 会有一个advisesupport对象，他们里面可以封装我们当前这个类的config 包含了aop的一些原数据，然后通过这个advise
 * 对象，和config 封装不同的前置通知，后置通知，然后用责任链模式去执行，来完成aop
 */
public interface AopProxy {

    Object getProxy();

    Object getProxy(ClassLoader classLoader);
}
