package com.basic.happytest.modules.email;

import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 发送邮件的工具 <br />
 * 注意：<br />
 * 1、需要加载对应的依赖 <br />
 * 2、需要去获取作为发送者的邮箱的smtp信息（可能还需要在对应的邮箱上做配置） <br />
 * @author lhf
 */

@Component
public class EmailUtils {

    @Autowired
    private EmailConfig emailConfig;

    /**
     * 发送邮件
     * @param sendUserName 发送者名字
     * @param emailAddress 收件地址
     * @param title 邮件标题
     * @param content 邮件内容
     * @return 是否成功
     */
    public boolean sendEmail(String sendUserName, String emailAddress, String title, String content){
        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName(emailConfig.getHostName()); // 设置邮件SMTP服务器
            email.setCharset("UTF-8");
            email.addTo(emailAddress); // 收件地址
            email.setFrom(emailConfig.getSendAddress(), sendUserName); // 此处填邮箱地址和用户名,用户名可以任意填写
            email.setAuthentication(emailConfig.getSendAddress(), emailConfig.getAuthorizationCode()); // 此处填写邮箱地址和客户端授权码
            email.setSubject(title); // 此处填写邮件名，邮件名可任意填写
            email.setMsg(content); // 此处填写邮件内容
            email.send();
            return true;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // todo 可整合一个邮件发送工具，统一处理邮件发送请求，同时使用模板来发送html邮件
}
