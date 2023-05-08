package com.basic.happytest.modules.textProcessing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextProcessingUtilsTest {

    @Test
    void replaceLineBreak() {
        String data = "abc\\\\ndef\\\\nghi";
        System.out.println("原先：" + data);
        String result = TextProcessingUtils.replaceLineBreak(data);
        System.out.println("转换后：\n" + result);
    }
}