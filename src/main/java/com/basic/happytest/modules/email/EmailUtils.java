package com.basic.happytest.modules.email;

import com.basic.happytest.modules.objectUtils.ObjMapTransformUtil;
import com.basic.happytest.modules.objectUtils.Objs.TestClass;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.util.Date;
import java.util.Map;

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
    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 发送邮件
     * @param sendUserName 发送者名字
     * @param emailAddress 收件地址
     * @param title 邮件标题
     * @param content 邮件内容
     * @param file 附件的文件流（若无附件，则可直接为null）
     * @return 是否成功
     */
    public boolean sendEmail(String sendUserName, String emailAddress, String title, String content, File file){
        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName(emailConfig.getHostName()); // 设置邮件SMTP服务器
            email.setCharset("UTF-8");
            email.addTo(emailAddress); // 收件地址
            email.setFrom(emailConfig.getSendAddress(), sendUserName); // 此处填邮箱地址和用户名,用户名可以任意填写
            email.setAuthentication(emailConfig.getSendAddress(), emailConfig.getAuthorizationCode()); // 此处填写邮箱地址和客户端授权码
            email.setSubject(title); // 此处填写邮件名，邮件名可任意填写
            email.setMsg(content); // 此处填写邮件内容
            if (file != null) {
                email.attach(file); // 添加附件
            }
            email.send();
            return true;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 使用模板来发送html邮件（示例）
     * @param testClass 入参实体
     * @param sendUserName 发送者名字
     * @param emailAddress 收件地址
     * @param title 邮件标题
     * @param templateFilePath 模板文件路径
     * @param file 附件的文件流（若无附件，则可直接为null）
     * @return 是否发送成功
     * @throws Exception 异常
     */
    public boolean sendTemplateEmail(TestClass testClass, String sendUserName, String emailAddress, String title,
                                     String templateFilePath, File file) throws Exception {
        Map<String, Object> map = ObjMapTransformUtil.Obj2Map(testClass);
        // 手动设置值
        map.put("oneDay", new Date());
        Context context = new Context();
        context.setVariables(map);
        // templateFilePath模板文件路径默认是resources/templates/底下的html后缀文件，符合的话甚至可以不用带html后缀
        String content = templateEngine.process(templateFilePath, context);
        return sendEmail(sendUserName, emailAddress, title, content, file);
    }
}
