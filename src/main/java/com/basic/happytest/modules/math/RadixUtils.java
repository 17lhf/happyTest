package com.basic.happytest.modules.math;

/**
 * 进制数工具
 * @author lhf
 */

public class RadixUtils {

    /**
     * 判定十六进制数据转为二进制后，第一位是否为1
     * @param hex 十六进制数
     * @return true-是，false-否
     */
    public static boolean hexIsFirstBitOne(String hex) {
        int decimal = Integer.parseInt(hex, 16);
        String binary = Integer.toBinaryString(decimal);
        return binary.charAt(0) == '1';
    }
}
