package com.basic.happytest.modules.ymlConfig;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 测试读取配置文件的值是否成功
 * @author lhf
 */

@SpringBootTest
public class YmlconfigTest {
    @Autowired
    YmlConfig ymlConfig;

    @Test
    public void getMyConfigs(){
        System.out.println(ymlConfig.getHello());
        System.out.println(ymlConfig.getWorld());
        System.out.println(ymlConfig.getAmazingWay());
    }
}
