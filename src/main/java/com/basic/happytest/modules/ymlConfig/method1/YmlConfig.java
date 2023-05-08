package com.basic.happytest.modules.ymlConfig.method1;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 以优雅的方式读取yml配置文件中的配置值（写法1） <br />
 * （这种写法相当于直接把写法3的两个类合二为一）
 * @author lhf
 */

@Getter
@Setter
@Configuration
// test-config是yml里的配置前缀
// ignoreInvalidFields 默认 false，表示不忽略无效的字段。无效字段一般是指配置了错误的类型，如期望是布尔类型结果却配置了"abc"字符串
@ConfigurationProperties(prefix = "test-config", ignoreInvalidFields = true)
public class YmlConfig {

    private String hello;

    private String world;

    private String amazingWay;
}
