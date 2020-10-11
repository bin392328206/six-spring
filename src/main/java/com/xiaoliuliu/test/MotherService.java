package com.xiaoliuliu.test;

import com.xiaoliuliu.spring.annotation.Service;

/**
 * @author 小六六
 * @version 1.0
 * @date 2020-10-11 15:04
 */
@Service
public class MotherService {
    public String call() {
        return "你妈妈叫你不要加班！";
    }
}
