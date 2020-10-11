package com.xiaoliuliu.test;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.xiaoliuliu.spring.context.support.ApplicationContext;
import com.xiaoliuliu.spring.context.support.DefaultApplicationContext;

import java.io.InputStream;
import java.util.Map;

/**
 * @author 小六六
 * @version 1.0
 * @date 2020-10-11 13:23
 */
public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new DefaultApplicationContext("application.properties");
        IHelloService helloService = (IHelloService) applicationContext.getBean("helloService");
        helloService.sayHello();

    }

}
