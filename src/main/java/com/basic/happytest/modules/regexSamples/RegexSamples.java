package com.basic.happytest.modules.regexSamples;

/**
 * 正则表达式应用的一些实例
 * @author : lhf
 */

public class RegexSamples {

    /**
     * 在原字符串基础上，每隔 n 位字符添加一个字符串
     * @param originalStr 原始字符串
     * @param n 每隔n位字符
     * @param insertStr 插入的字符串
     * @return 变化后的字符串
     */
    public static String eachNumCharInsertStr(String originalStr, Integer n, String insertStr) {
        String regex = "(.{" + n + "})";
        return originalStr.replaceAll(regex, "$1" + insertStr);
    }
}
