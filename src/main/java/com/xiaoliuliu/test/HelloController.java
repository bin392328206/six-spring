package com.xiaoliuliu.test;

import com.xiaoliuliu.spring.annotation.Controller;
import com.xiaoliuliu.spring.annotation.RequestMapping;
import com.xiaoliuliu.spring.webmvc.servlet.ModelAndView;

import java.util.HashMap;

/**
 * @author 小六六
 * @version 1.0
 * @date 2020/10/12 15:23
 */
@Controller
public class HelloController {

    @RequestMapping("/hello")
    public ModelAndView hello() {
        HashMap<String, Object> model = new HashMap<>();
        model.put("data1", "hello");
        model.put("data2", "world");
        return new ModelAndView("test", model);
    }
}
