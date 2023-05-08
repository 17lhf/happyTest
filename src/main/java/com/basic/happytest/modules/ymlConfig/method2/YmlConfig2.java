package com.basic.happytest.modules.ymlConfig.method2;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 以优雅的方式读取yml配置文件中的配置值（写法2） <br />
 * 该方法利用@Component来达到配置类直接变成bean，使得可以以类型安全的方式访问配置参数的值。
 * @author lhf
 */

@Getter
@Setter
@Component
// test-config是yml里的配置前缀
// ignoreInvalidFields 默认 false，表示不忽略无效的字段。无效字段一般是指配置了错误的类型，如期望是布尔类型结果却配置了"abc"字符串
@ConfigurationProperties(prefix = "test-config2", ignoreInvalidFields = true)
public class YmlConfig2 {

    private String hello;

    private String world;

    private String amazingWay;
}
