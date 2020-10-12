package com.xiaoliuliu.spring.webmvc.servlet;

import com.xiaoliuliu.spring.annotation.Controller;
import com.xiaoliuliu.spring.annotation.RequestMapping;
import com.xiaoliuliu.spring.context.support.DefaultApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 小六六
 * @version 1.0
 * @date 2020/10/12 14:38
 * 总结一下SpringMVC的流程，其实我们知道 首先就是一个Http请求打过来，那么既然是一个Http请求，那么我们必须得有一个web服务器，例如我们的tomcat，tomcat其实就是处理我们的请求，然后把
 * 请求给到我们的servlet 那么什么是servlet呢？其实就是我们Java的一个规范，它规范了按照怎么样的步骤去处理这个http请求，你可以自己实现，也可以用实现好的，然后就是我们
 * 所说的SiprngMvc 那么我们的SpringMVC处理的流程是怎么样的呢？首先我们知道servlet 中有一个init方法，那么在这个方法中呢？我们首先是先完成Spring容器的创建，然后在把我们
 * springMVC中的9大组件 注入到Spring容器中，比如我们的HadlerMapping,我们的DispatcherServlet HandlerAdapter 等等这些 然后这些都是初始化的流程，我们来看看请求流程
 * 当我们的请求走到了doGet方法，然后第一步拿到request中的url 然后把它去HandlerMapping中去匹配 找到对应的HandlerMapping,然后再通过HandlerMapping 去找到HandlerAdapter
 * 然后把HandlerAdapter中的参数去填充到Controller中 然后把参数封装好了之后，请求也走到了controller中，然后接下来就是业务了，把业务走完，就到了我们这边ModelAndView 或者就是JOSN格式的
 * 数据了，目前来说JSON格式用的比较多了，然后ModelAndView 其实就是找到我们的模板，然后封装好模板的占位符，然后填充数据，再把页面也通过http请求统一返回。
 *
 *
 */
public class DispatcherServlet extends HttpServlet {

    /**配置文件地址，从web.xml中获取*/
    private static final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

    private DefaultApplicationContext context;

    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    private Map<HandlerMapping, HandlerAdapter> handlerAdapters = new HashMap<>();

    private List<ViewResolver> viewResolvers = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            this.doDispatch(req, resp);
        } catch (Exception e) {
            resp.getWriter().write("500 Exception,Details:\r\n"
                    + Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]", "")
                    .replaceAll(",\\s", "\r\n"));
            e.printStackTrace();
        }
    }

        /**
             * @Des  真正的处理我们的http请求的方法
             * @Author 小六六
             * @Date 2020/10/12 14:54
             * @Param 
             * @Return 
             */
    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) {
        //1、通过从request中拿到URL，去匹配一个HandlerMapping
        HandlerMapping handler = getHandler(req);

        if (handler == null) {
            //没有找到handler返回404
            processDispatchResult(req, resp, new ModelAndView("404"));
            return;
        }

        //2、准备调用前的参数
        HandlerAdapter ha = getHandlerAdapter(handler);

        //3、真正的调用controller的方法
        ModelAndView mv = null;
        try {
            mv = ha.handle(req, resp, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //4、渲染页面输出
        processDispatchResult(req, resp, mv);
    }

    private HandlerAdapter getHandlerAdapter(HandlerMapping handler) {
        if (this.handlerAdapters.isEmpty()) {
            return null;
        }
        HandlerAdapter ha = this.handlerAdapters.get(handler);
        return ha;
    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, ModelAndView mv) {
        if (null == mv) {
            return;
        }

        if (this.viewResolvers.isEmpty()) {
            return;
        }

        for (ViewResolver viewResolver : this.viewResolvers) {
            try {
                //根据模板名拿到View
                View view = viewResolver.resolveViewName(mv.getViewName(), null);
                //开始渲染
                view.render(mv.getModel(), req, resp);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private HandlerMapping getHandler(HttpServletRequest req) {
        if (this.handlerMappings.isEmpty()) {
            return null;
        }

        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");

        for (HandlerMapping handler : this.handlerMappings) {
            try {
                Matcher matcher = handler.getPattern().matcher(url);
                //如果没有匹配上继续下一个匹配
                if (!matcher.matches()) {
                    continue;
                }
                return handler;
            } catch (Exception e) {
                throw e;
            }
        }
        return null;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        //1、初始化ApplicationContext
        context = new DefaultApplicationContext(config.getInitParameter(CONTEXT_CONFIG_LOCATION));

        //2、初始化Spring MVC 九大组件
        initStrategies(context);
    }

    //初始化策略
    protected void initStrategies(DefaultApplicationContext context) {
        //多文件上传的组件

        //初始化本地语言环境

        //初始化模板处理器

        //handlerMapping，必须实现
        initHandlerMappings(context);

        //初始化参数适配器，必须实现
        initHandlerAdapters(context);

        //初始化异常拦截器

        //初始化视图预处理器

        //初始化视图转换器，必须实现
        initViewResolvers(context);

        //参数缓存器
    }


    private void initHandlerMappings(DefaultApplicationContext context) {
        String[] beanNames = context.getBeanDefinitionNames();

        try {
            for (String beanName : beanNames) {
                Object controller = context.getBean(beanName);
                Class<?> clazz = controller.getClass();
                if (!clazz.isAnnotationPresent(Controller.class)) {
                    continue;
                }

                String baseUrl = "";
                //获取Controller的url配置
                if (clazz.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                    baseUrl = requestMapping.value();
                }

                //获取Method的url配置
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {

                    //没有加RequestMapping注解的直接忽略
                    if (!method.isAnnotationPresent(RequestMapping.class)) {
                        continue;
                    }

                    //映射URL
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    String regex = ("/" + baseUrl + "/" + requestMapping.value().replaceAll("\\*", ".*")).replaceAll("/+", "/");
                    Pattern pattern = Pattern.compile(regex);

                    this.handlerMappings.add(new HandlerMapping(controller, method, pattern));
                    System.out.println("Mapped " + regex + "," + method);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initHandlerAdapters(DefaultApplicationContext context) {
        //一个HandlerMapping对应一个HandlerAdapter
        for (HandlerMapping handlerMapping : this.handlerMappings) {
            this.handlerAdapters.put(handlerMapping, new HandlerAdapter());
        }
    }

    private void initViewResolvers(DefaultApplicationContext context) {
        //配置文件中拿到模板的存放目录
        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        File templateRootDir = new File(templateRootPath);
        String[] templates = templateRootDir.list();
        for (int i = 0; i < templates.length; i ++) {
            this.viewResolvers.add(new ViewResolver(templateRoot));
        }
    }
}
