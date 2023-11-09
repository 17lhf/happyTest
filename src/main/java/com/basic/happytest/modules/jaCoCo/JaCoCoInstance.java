package com.basic.happytest.modules.jaCoCo;

/**
 * 用于JaCoCo代码覆盖率测试的示例类
 * @author lhf
 */

public class JaCoCoInstance {

    /**
     * 获取更大的数
     * @param a 一个数字
     * @param b 一个数字
     * @return 更大的数
     */
    public static Integer max(Integer a, Integer b) {
        Integer result;
        if (b == null || a == null) {
            result = null;
        } else if (a > b) {
            result = a;
        } else {
            result = b;
        }
        return result;
    }
}
