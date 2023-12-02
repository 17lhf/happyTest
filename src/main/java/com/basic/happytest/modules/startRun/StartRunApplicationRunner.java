package com.basic.happytest.modules.startRun;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 基于ApplicationRunner的项目启动时运行 <br />
 * 官方文档：预计在启动期间运行的任务应该交由 CommandLineRunner 和 ApplicationRunner 组件执行，而不是使用Spring组件的生命周期回调，如 @PostConstruct。 <br />
 * 官方推荐在这里放预计在启动期间运行的任务，甚至是运行潜在耗时的任务 <br />
 * @author lhf
 */

@Component
public class StartRunApplicationRunner implements ApplicationRunner {

    /**
     * 注意，启动参数需要这样写（多个参数用空格隔开，每个参数的参数名需要以两个减号开头，以等号的形式设置参数值）：--a=testArg1 --b=Arg2
     * @param args incoming application arguments
     */
    @Override
    public void run(ApplicationArguments args) {
        System.out.println("--------------------开始运行StartRunApplicationRunner-------------------");
        for (String optionName : args.getOptionNames()) {
            System.out.println("optionName: " + optionName + ", optionValue: " + args.getOptionValues(optionName));
        }
        System.out.println("--------------------结束运行StartRunApplicationRunner-------------------");
    }
}
