package com.basic.happytest.modules.startRun;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 监听ContextRefreshedEvent事件 <br />
 * 注意，有效，不需要配置spring.factories配置文件 <br />
 * 我们自定义的ApplicationListener实现类，如果没有去配置spring.factories，实例化然后关联到ApplicationContext的initialMulticaster（事件广播器）上，开始监听事件的时机： <br />
 * SpringApplication.run() -> refreshContext(context) -> AbstractApplicationContext.refresh() -> registerListeners() <br />
 * @author : lhf
 */

@Component
public class MyContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("---------------MyContextRefreshedListener------------------");
    }
}
