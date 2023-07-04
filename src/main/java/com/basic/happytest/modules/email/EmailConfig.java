package com.basic.happytest.modules.email;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 发送邮件相关的配置
 * @author lhf
 */

@Getter
@Setter
@Configuration
@ConfigurationProperties("email")
public class EmailConfig {
    /**
     * 邮件SMTP服务器
     */
    private String hostName;

    /**
     * 邮箱地址
     */
    private String sendAddress;

    /**
     * 客户端授权码
     */
    private String authorizationCode;
}
