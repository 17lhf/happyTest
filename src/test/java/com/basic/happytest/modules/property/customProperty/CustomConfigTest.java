package com.basic.happytest.modules.property.customProperty;

import com.basic.happytest.modules.fileIO.FileIO;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class CustomConfigTest {

    /**
     * 外部配置文件路径（这里做模拟，所以是相对路径，实际上应该是放在项目外头的某个文件夹中，然后这里可以放绝对路径）
     */
    private static final String EXT_CONFIG_FILE_PATH = "/properties/extCustomConfig.properties";

    @Test
    void init() {
        // 不加载外部配置时
        CustomConfig.init(null);
        System.out.println("name：" + CustomConfig.getProp(CustomConfigEnum.NAME.getKeyName()));
        System.out.println("age：" + CustomConfig.getProp(CustomConfigEnum.AGE.getKeyName()));
    }

    @Test
    void init2() throws IOException {
        // 加载外部配置时
        CustomConfig.init(FileIO.getResourceAbsolutePath(EXT_CONFIG_FILE_PATH)); // 如果EXT_CONFIG_FILE_PATH是绝对路径，这里传参要修改
        System.out.println("name：" + CustomConfig.getProp(CustomConfigEnum.NAME.getKeyName()));
        System.out.println("age：" + CustomConfig.getProp(CustomConfigEnum.AGE.getKeyName()));
    }

    @Test
    void updProps() {
        // 不加载外部配置时
        CustomConfig.init(null);
        System.out.println("--------------------------初始配置------------------------------");
        System.out.println("name：" + CustomConfig.getProp(CustomConfigEnum.NAME.getKeyName()));
        System.out.println("age：" + CustomConfig.getProp(CustomConfigEnum.AGE.getKeyName()));
        CustomConfig.updProps("Carol(新角色)", "33岁");
        System.out.println("--------------------------新配置------------------------------");
        System.out.println("name：" + CustomConfig.getProp(CustomConfigEnum.NAME.getKeyName()));
        System.out.println("age：" + CustomConfig.getProp(CustomConfigEnum.AGE.getKeyName()));
    }

    @Test
    void updProps2() throws IOException {
        // 加载外部配置时
        CustomConfig.init(FileIO.getResourceAbsolutePath(EXT_CONFIG_FILE_PATH)); // 如果EXT_CONFIG_FILE_PATH是绝对路径，这里传参要修改
        System.out.println("--------------------------初始配置------------------------------");
        System.out.println("name：" + CustomConfig.getProp(CustomConfigEnum.NAME.getKeyName()));
        System.out.println("age：" + CustomConfig.getProp(CustomConfigEnum.AGE.getKeyName()));
        CustomConfig.updProps("Carol(新角色)", "33岁");
        System.out.println("--------------------------新配置------------------------------");
        System.out.println("name：" + CustomConfig.getProp(CustomConfigEnum.NAME.getKeyName()));
        System.out.println("age：" + CustomConfig.getProp(CustomConfigEnum.AGE.getKeyName()));
    }
}