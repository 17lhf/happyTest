package com.basic.happytest.modules.property.ymlConfig.method3;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 设置与yml配置文件中配置值关联（写法3）
 * @author lhf
 */

@Getter
@Setter
// test-config是yml里的配置前缀
// ignoreInvalidFields 默认 false，表示不忽略无效的字段。无效字段一般是指配置了错误的类型，如期望是布尔类型结果却配置了"abc"字符串
@ConfigurationProperties(prefix = "test-config3", ignoreInvalidFields = true)
public class YmlConfig3 {

    private String hello;

    private String world;

    private String amazingWay;
}
