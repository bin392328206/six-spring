package com.xiaoliuliu.test;

import com.xiaoliuliu.spring.server.HttpServer;

/**
 * @author 小六六
 * @version 1.0
 * @date 2020/10/12 17:40
 * 测试 netty httpserver
 */
public class Application {
    public static void main(String[] args) throws Exception{
        HttpServer server = new HttpServer(8081);// 8081为启动端口
        server.start();
    }
}
