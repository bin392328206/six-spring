package com.xiaoliuliu.spring.webmvc.servlet;

import java.io.File;
import java.util.Locale;

/**
 * @author 小六六
 * @version 1.0
 * @date 2020/10/12 14:49
 * ViewResolver需要根据模板名找到对应的模板，封装成View
 */
public class ViewResolver {

    private final String DEFAULT_TEMPLATE_SUFFIX = ".html";

    /**模板根目录*/
    private File templateRootDir;

    public ViewResolver(String templateRoot) {
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        templateRootDir = new File(templateRootPath);
    }

    public View resolveViewName(String viewName, Locale locale) throws Exception{
        if(null == viewName || "".equals(viewName.trim())){return null;}
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX) ? viewName : (viewName + DEFAULT_TEMPLATE_SUFFIX);
        File templateFile = new File((templateRootDir.getPath() + "/" + viewName).replaceAll("/+","/"));
        return new View(templateFile);
    }
}
