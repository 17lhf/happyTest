package com.basic.happytest.modules.startRun;

import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 监听ApplicationContextInitialized事件: 当 ApplicationContext 已准备好并且 ApplicationContextInitializers 被调用，
 * 但在任何Bean定义被加载之前，ApplicationContextInitializedEvent 被发布(所以此时不能用@Autowired注入依赖项，因为没有bean)。 <br />
 * 注意，因为对应事件发布的时间早于这个类被实例化，需要配置spring.factories配置文件才有效 <br />
 * 我们自定义的ApplicationListener实现类，如果没有去配置spring.factories，实例化然后关联到ApplicationContext的initialMulticaster（事件广播器）上，开始监听事件的时机： <br />
 * SpringApplication.run() -> refreshContext(context) -> AbstractApplicationContext.refresh() -> registerListeners() <br />
 * 我们去整一个spring.factories，为的就是让SpringApplication初始化时把我们【自定义的ApplicationListener实现类】给加载了。
 * 让其在实例化【SpringApplicationRunListeners的实例对象】时，能够把【自定义的ApplicationListener实现类】给关联到initialMulticaster（事件广播器）上面。
 * 使得可以监听到启动过程中完整的事件。 <br />
 * @author : lhf
 */

@Component
public class MyApplicationContextInitializedListener implements ApplicationListener<ApplicationContextInitializedEvent> {

    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent event) {
        System.out.println("---------------MyApplicationContextInitializedListener------------------");
    }
}
