package com.basic.happytest;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AsyncAppenderBase;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

import java.util.Iterator;

@SpringBootApplication
public class HappyTestApplication {

    public static void main(String[] args) {
        // region 这是Spring Boot允许的自定义SpringApplication方式
        SpringApplication application = new SpringApplication(HappyTestApplication.class);
        // 缓存应用程序启动步骤并将其排入（发送）到外部度量系统。应用程序可以在任何组件中要求（通过注入）获得 BufferingApplicationStartup 类型的bean。
        application.setApplicationStartup(new BufferingApplicationStartup(20480));
        application.setBannerMode(Banner.Mode.CONSOLE); // 设置Banner的打印模式（例如打印到日志、不打印）
        application.run(args);
        // endregion

        // 这是使用默认的SpringApplication
        // SpringApplication.run(HappyTestApplication.class, args);

        // 打印日志框架配置的情况
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        for (ch.qos.logback.classic.Logger logger : context.getLoggerList()) {
            for (Iterator<Appender<ILoggingEvent>> it = logger.iteratorForAppenders(); it.hasNext(); ) {
                Appender<ILoggingEvent> appender = it.next();
                if (appender instanceof AsyncAppenderBase) {
                    System.out.println("AsyncAppender found: " + appender.getName());
                } else {
                    System.out.println("Sync Appender found: " + appender.getName());
                }
            }
        }
    }

}
