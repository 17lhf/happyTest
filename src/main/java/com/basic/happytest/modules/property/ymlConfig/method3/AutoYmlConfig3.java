package com.basic.happytest.modules.property.ymlConfig.method3;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 配置yml配置文件中的配置值(没有这个类，则YmlConfig3就不会进入IOC容器)（写法3）
 * @author lhf
 */

@Configuration
// 如果A这个类使用了@ConfigurationProperties注解，那么A这个类会与相应配置的配置文件进行动态绑定，并且会将A这个类加入IOC容器，并交由IOC容器进行管理（需要配合@Configuration使用才行）
@EnableConfigurationProperties(YmlConfig3.class)
public class AutoYmlConfig3 {

    private final YmlConfig3 ymlConfig3;

    public AutoYmlConfig3(YmlConfig3 ymlConfig3) {
        this.ymlConfig3 = ymlConfig3;
    }

    public void printConfigMsg() {
        System.out.println(ymlConfig3.getHello() + " " + ymlConfig3.getWorld() + " " + ymlConfig3.getAmazingWay());
    }
}
