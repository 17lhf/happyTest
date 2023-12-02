package com.basic.happytest.modules.startRun;

import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 自定义ServletContextListener监听器 <br />
 * 可以利用@Autowired注入依赖项是完全OK的 <br />
 * ServletContext初始化是在ApplicationContext refresh的时候，onRefresh()里，ServletContext相关的监听器会在初始化的时候一并注册。 <br />
 * @author lhf
 */

@Component
public class MyServletContextListener implements ServletContextListener {

    /**
     * ServletContext初始化后执行
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("--------------------ServletContext Initialized -------------------");
    }

    /**
     * ServletContext销毁触发，一般就是结束应用的时候
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("--------------------ServletContext Destroyed -------------------");
    }

}
