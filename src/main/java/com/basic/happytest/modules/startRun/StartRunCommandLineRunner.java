package com.basic.happytest.modules.startRun;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 基于CommandLineRunner的项目启动时运行 <br />
 * 官方文档：预计在启动期间运行的任务应该交由 CommandLineRunner 和 ApplicationRunner 组件执行，而不是使用Spring组件的生命周期回调，如 @PostConstruct。 <br />
 * 官方推荐在这里放预计在启动期间运行的任务，甚至是运行潜在耗时的任务 <br />
 * @author lhf
 */

@Component
public class StartRunCommandLineRunner implements CommandLineRunner {

    /**
     * @param args 启动参数这样写就行（多个参数用空格隔开）：testArg1,Arg2
     */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("--------------------开始运行StartRunCommandLineRunner-------------------");
        for (String arg : args) {
            System.out.println(arg);
        }
        System.out.println("--------------------结束运行StartRunCommandLineRunner-------------------");
    }
}
