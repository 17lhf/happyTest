package com.basic.happytest.modules.startRun;

import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 监听ApplicationStarting事件: 一个 ApplicationStartingEvent 在运行开始时被发布，但在任何处理之前，除了注册监听器和初始化器之外。 <br />
 * 注意，因为对应事件发布的时间早于这个类被实例化，所以需要配置spring.factories配置文件才有效
 * 我们自定义的ApplicationListener实现类，如果没有去配置spring.factories，实例化然后关联到ApplicationContext的initialMulticaster（事件广播器）上，开始监听事件的时机： <br />
 * SpringApplication.run() -> refreshContext(context) -> AbstractApplicationContext.refresh() -> registerListeners() <br />
 * 我们去整一个spring.factories，为的就是让SpringApplication初始化时把我们【自定义的ApplicationListener实现类】给加载了。
 * 让其在实例化【SpringApplicationRunListeners的实例对象】时，能够把【自定义的ApplicationListener实现类】给关联到initialMulticaster（事件广播器）上面。
 * 使得可以监听到启动过程中完整的事件。 <br />
 * @author : lhf
 */

@Component
public class MyApplicationStartingListener implements ApplicationListener<ApplicationStartingEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartingEvent event) {
        System.out.println("---------------MyApplicationStartingListener------------------");
    }
}
