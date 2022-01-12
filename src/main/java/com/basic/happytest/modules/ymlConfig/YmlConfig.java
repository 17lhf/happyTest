package com.basic.happytest.modules.ymlConfig;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@EnableConfigurationProperties(YmlConfig.class)
// test-config是yml里的配置前缀
// ignoreInvalidFields 默认 false，表示不忽略无效的字段。无效字段一般是指配置了错误的类型，如期望是布尔类型结果却配置了"abc"字符串
@ConfigurationProperties(prefix = "test-config", ignoreInvalidFields = true)
public class YmlConfig {

    private String hello;

    private String world;

    private String amazingWay;
}
