package com.basic.happytest.modules.startRun;

import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.boot.context.event.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

/**
 *  监听应用程序启动过程 <br />
 *  注意，@EventListener涉及以下内容：<br />
 *  需要Spring版本>=4.2<br />
 *  只用于监听被发布到ApplicationContext中的ApplicationEvent<br />
 *  注解的解析，涉及AnnotationConfigApplicationContext: 激活要在 Bean 类中检测的各种注解，比如@PostConstruct、@PreDestroy <br />
 *  EventListener 注解的处理是通过内部 EventListenerMethodProcessor bean 执行的。<br />
 *  此外，EventListenerMethodProcessor继承自BeanFactoryPostProcessor接口，实现的postProcessBeanFactory方法，在容器将所有bean解析成beanDefinition后，
 *  实例化之前执行（方法内容是：先获取所有实现EventListenerFactory接口的单例对象，排序后保存在容器中）。<br />
 *  当所有单例 bean 都初始化完成以后，Spring的IOC容器会回调实现SmartInitializingSingleton接口的EventListenerMethodProcessor 的 afterSingletonsInstantiated()方法。
 *  afterSingletonsInstantiated方法本身在各种判断后，会调用processBean方法。processBean方法会注册@EventListener注解的方法作为独立的ApplicationListener实例，
 *  并关联到ApplicationContext的initialMulticaster（事件广播器）上，这之后，@EventListener注解的监听器才会开始监听事件。<br />
 *  afterSingletonsInstantiated执行时机：SpringApplication.run() -> refreshContext(context) -> AbstractApplicationContext.refresh() ->
 *  finishBeanFactoryInitialization(beanFactory) -> beanFactory.preInstantiateSingletons() -> DefaultListableBeanFactory.preInstantiateSingletons()
 * @author : lhf
 */

@Configuration
public class MyApplicationListener{

    /**
     * 监听ApplicationStarting事件（无效）: 一个 ApplicationStartingEvent 在运行开始时被发布，但在任何处理之前，除了注册监听器和初始化器之外。
     */
    @EventListener
    public void applicationStartingListener(ApplicationStartingEvent applicationStartingEvent) {
        System.out.println("-----------------ApplicationStarting----------------");
    }

    /**
     * 监听ApplicationEnvironmentPrepared事件（无效）: 当在上下文中使用的 Environment 已知，但在创建上下文之前，将发布 ApplicationEnvironmentPreparedEvent。
     */
    @EventListener
    public void applicationEnvironmentPreparedListener(ApplicationEnvironmentPreparedEvent applicationEnvironmentPreparedEvent) {
        System.out.println("-----------------ApplicationEnvironmentPrepared----------------");
    }

    /**
     * 监听ApplicationContextInitialized事件（无效）: 当 ApplicationContext 已准备好并且 ApplicationContextInitializers 被调用，但在任何Bean定义被加载之前，ApplicationContextInitializedEvent 被发布。
     */
    @EventListener
    public void applicationContextInitializedListener(ApplicationContextInitializedEvent applicationContextInitializedEvent) {
        System.out.println("-----------------ApplicationContextInitialized----------------");
    }

    /**
     * 监听ApplicationPrepared事件（无效）: 一个 ApplicationPreparedEvent 将在刷新开始前但在Bean定义加载后被发布。
     */
    @EventListener
    public void applicationPreparedListener(ApplicationPreparedEvent applicationPreparedEvent) {
        System.out.println("-----------------ApplicationPrepared----------------");
    }

    /**
     * 监听ApplicationContextRefreshedEvent事件
     */
    @EventListener(classes = {ContextRefreshedEvent.class}) // 允许的写法2，这里可以写多个要监听的类
    public void contextRefreshedEventListener() {
        System.out.println("-----------------ContextRefreshedEvent----------------");
    }

    /**
     * 监听ApplicationStarted事件: 在上下文被刷新之后，但在任何应用程序和命令行运行程序被调用之前，将发布一个 ApplicationStartedEvent。
     */
    @EventListener
    public void applicationStartedListener(ApplicationStartedEvent applicationStartedEvent) {
        System.out.println("-----------------ApplicationStarted----------------");
    }

    /**
     * 官方文档：应用组件可以在任何时候通过注入 ApplicationAvailability 接口并调用其上的方法来检索当前的可用性状态。
     * 一个应用程序的 “Liveness” 状态告诉我们它的内部状态是否允许它正常工作，或者在当前失败的情况下自行恢复。
     * 一个broken状态的 “Liveness” 状态意味着应用程序处于一个无法恢复的状态，基础设施应该重新启动应用程序。
     * Spring Boot应用程序的内部状态大多由Spring ApplicationContext 表示。
     * 如果 application context 已成功启动，Spring Boot就认为应用程序处于有效状态。一旦context被刷新，应用程序就被认为是活的 <br />
     */
    @EventListener
    public void onLivenessStateChange(AvailabilityChangeEvent<LivenessState> event) {
        switch (event.getState()) {
            case CORRECT:
                System.out.println("LivenessState is correct");
                break;
            case BROKEN:
                System.out.println("LivenessState is broken");
                break;
        }
    }

    /**
     * 监听ApplicationReady事件: 在任何ApplicationRunner 和 CommandLineRunner被调用后，将发布一个 ApplicationReadyEvent。
     */
    @EventListener
    public void applicationReadyListener(ApplicationReadyEvent applicationReadyEvent) {
        System.out.println("-----------------ApplicationReady----------------");
    }

    /**
     * 官方文档：应用组件可以在任何时候通过注入 ApplicationAvailability 接口并调用其上的方法来检索当前的可用性状态。
     * 应用程序的 “Readiness” 状态告诉平台，该应用程序是否准备好处理流量。failing状态的 “Readiness” 告诉平台，它暂时不应该将流量发送到该应用程序。
     * 这通常发生在启动期间，当 CommandLineRunner 和 ApplicationRunner 组件还在被处理的时候，或者是应用程序觉得目前负载已经到了极限，不能再处理额外的请求的时候。
     * 一旦 ApplicationRunner 和 CommandLineRunner 被调用完成，就认为应用程序已经准备好了
     */
    @EventListener
    public void onReadinessStateChange(AvailabilityChangeEvent<ReadinessState> event) {
        switch (event.getState()) {
            case ACCEPTING_TRAFFIC:
                System.out.println("ReadinessState is accepting traffic");
                break;
            case REFUSING_TRAFFIC:
                System.out.println("ReadinessState is refusing traffic");
                break;
        }
    }

    /**
     * 监听ApplicationFailed事件: 如果启动时出现异常，将发布一个 ApplicationFailedEvent
     */
    @EventListener
    public void applicationFailedListener(ApplicationFailedEvent applicationFailedEvent) {
        System.out.println("-----------------ApplicationFailed----------------");
    }
}
