package com.basic.happytest.modules.ymlConfig;

import com.basic.happytest.modules.ymlConfig.method1.YmlConfig;
import com.basic.happytest.modules.ymlConfig.method2.YmlConfig2;
import com.basic.happytest.modules.ymlConfig.method3.AutoYmlConfig3;
import com.basic.happytest.modules.ymlConfig.method3.YmlConfig3;
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
    private YmlConfig ymlConfig;

    @Autowired
    private YmlConfig2 ymlConfig2;

    @Autowired
    private YmlConfig3 ymlConfig3;
    @Autowired
    private AutoYmlConfig3 autoYmlConfig3;

    @Test
    public void getMyConfigs1(){
        System.out.println(ymlConfig.getHello());
        System.out.println(ymlConfig.getWorld());
        System.out.println(ymlConfig.getAmazingWay());
    }

    @Test
    public void getMyConfigs2(){
        System.out.println(ymlConfig2.getHello());
        System.out.println(ymlConfig2.getWorld());
        System.out.println(ymlConfig2.getAmazingWay());
    }

    @Test
    public void getMyConfigs3(){
        // 可以直接只用ymlConfig3
        System.out.println(ymlConfig3.getHello());
        System.out.println(ymlConfig3.getWorld());
        System.out.println(ymlConfig3.getAmazingWay());
        // 也可以通过autoYmlConfig3来使用ymlConfig3
        autoYmlConfig3.printConfigMsg();
    }
}
