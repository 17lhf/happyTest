package com.basic.happytest.modules.startRun;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 自定义PostConstruct的回调 <br />
 * JSR-250的 @PostConstruct 和 @PreDestroy 注解通常被认为是在现代Spring应用程序中接收生命周期回调的最佳实践。使用这些注解意味着你的Bean不会被耦合到Spring特定的接口。 <br />
 * 参考官方文档：<a href="https://springdoc.cn/spring/core.html#beans-factory-nature">点击前往</a> <br />
 * 与 @Resource 一样，@PostConstruct 和 @PreDestroy 注解类型在JDK 6到8中是标准Java库的一部分。
 * 然而，整个 javax.annotation 包在JDK 9中从核心Java模块中分离出来，最终在JDK 11中被删除。
 * 从Jakarta EE 9开始，该包现在住在 jakarta.annotation 中。如果需要，现在需要通过Maven中心获得 jakarta.annotation-api 工件，只需像其他库一样添加到应用程序的classpath中即可。 <br />
 * 这两个注解，是应用于所处的Bean上的，目标就是为这个Bean做一些额外的事情
 * @author lhf
 */

@Component
public class MyPostConstruct {

    /**
     * 让Bean在容器对Bean设置了所有必要的属性后执行初始化工作（此时例如可以用来执行预填充之类的行为）
     */
    @PostConstruct
    public void init() {
        System.out.println("--------------------MyPostConstruct开始运行PostConstruct-------------------");
        System.out.println("--------------------MyPostConstruct结束运行PostConstruct-------------------");
    }

    /**
     * 让Bean在包含它的容器被销毁时获得一个回调（其实一般就是关闭应用的时候）(此时例如可以用来执行清空缓存之类的行为)
     */
    @PreDestroy
    public void destroy() {
        System.out.println("--------------------MyPostConstruct开始运行PreDestroy-------------------");
        System.out.println("--------------------MyPostConstruct结束运行PreDestroy-------------------");
    }
}
