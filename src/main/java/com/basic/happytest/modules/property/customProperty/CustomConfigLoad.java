package com.basic.happytest.modules.property.customProperty;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 启动时吗，容器初始化完成后，加载自定义配置
 * @author lhf
 */

// @Component // 必须交由bean管理，否则不会执行(放开注释就能执行)
@Order(1) // 初始化加载优先级，数字越小优先级越高
public class CustomConfigLoad implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        // 加载内部配置文件
        // CustomConfig.init(null);
        // 加载外部配置文件(注意修改外部配置文件路径为具体路径)
        // CustomConfig.init("xxx\\extCustomConfig.properties");
    }
}
