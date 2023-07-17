package com.basic.happytest.modules.email;

import com.basic.happytest.modules.objectUtils.Objs.TestClass;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class EmailUtilsTest {

    @Autowired
    private EmailUtils emailUtils;

    @Test
    void sendEmail() {
        String senderName = "SenderName";
        String title = "nothing title";
        String content = "Hello world";
        String receiveAddress = "123@qq.com";
        System.out.println(emailUtils.sendEmail(senderName, receiveAddress, title, content));
    }

    @Test
    void sendTemplateEmail() throws Exception {
        String senderName = "SenderName";
        String title = "nothing title";
        String receiveAddress = "13@qq.com";
        TestClass testClass = new TestClass();
        System.out.println(emailUtils.sendTemplateEmail(testClass, senderName, receiveAddress, title, "templateTest1"));
    }
}