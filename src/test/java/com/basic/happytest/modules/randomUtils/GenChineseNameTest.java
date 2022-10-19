package com.basic.happytest.modules.randomUtils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 随机生成中文名称测试
 */

class GenChineseNameTest {

    @Test
    void getChineseName() {
        for (int i = 0; i < 10; i++) {
            System.out.println(GenChineseName.getChineseName());
        }
    }
}