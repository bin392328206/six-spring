package com.xiaoliuliu.test;

import com.xiaoliuliu.spring.annotation.Autowired;
import com.xiaoliuliu.spring.annotation.Service;

/**
 * @author 小六六
 * @version 1.0
 * @date 2020-10-11 13:23
 */
@Service
public class HelloService implements IHelloService{

    //尝试注入MotherService
    @Autowired
    private MotherService motherService;

    @Override
    public void sayHello() {
        System.out.println("hello world!");
        System.out.println(motherService.call());
    }
}
