package com.basic.happytest.modules.others;

import org.junit.jupiter.api.Test;

/**
 * null和字符串转换的测试
 * @author : lhf
 */

public class NullStringTest {

    @Test
    public void transformTest() {
        Object nullObj = null;
        String s1 = String.valueOf(nullObj); // 见方法注释
        System.out.println(s1); // null
        System.out.println(s1.equals("null")); // true
        String s2 = String.valueOf(null); // NullPointerException
        System.out.println(s2);
    }
}
