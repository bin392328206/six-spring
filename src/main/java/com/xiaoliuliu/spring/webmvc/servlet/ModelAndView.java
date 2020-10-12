package com.xiaoliuliu.spring.webmvc.servlet;

import java.util.Map;

/**
 * @author 小六六
 * @version 1.0
 * @date 2020/10/12 14:45
 * ModelAndView是Controller方法返回的类型，封装了模板引擎名称和参数
 */
public class ModelAndView {

    //模板名字
    private String viewName;

    //模板中填充的参数
    private Map<String, ?> model;

    public ModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public ModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }
}
