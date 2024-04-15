package com.basic.happytest.modules.others;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

/**
 * 浮点精度测试
 * @author : lhf
 */

public class FloatPointTest {

    private strictfp double add(double a, double b) {
        return a+b;
    }

    @Test
    public void addTest() {
        double a = 0.1;
        double b = 0.2;
        System.out.println("0.1 + 0.2 = " + (a+b)); // 0.1 + 0.2 = 0.30000000000000004
        System.out.println("0.1 + 0.2 == 0.3 ? " + ((a+b) == 0.3)); // 0.1 + 0.2 == 0.3 ? false
        System.out.println("0.1 + 0.2 == 0.3 (strictfp)? " + (add(a, b) == 0.3)); // 0.1 + 0.2 == 0.3 (strictfp)? false
        String aStr = String.valueOf(a);
        String bStr = String.valueOf(b);
        String cStr = String.valueOf(0.3);
        BigDecimal aBigDecimal = new BigDecimal(aStr);
        BigDecimal bBigDecimal = new BigDecimal(bStr);
        BigDecimal cBigDecimal = new BigDecimal(cStr);
        System.out.println("0.1 + 0.2 == 0.3 (BigDecimal)? " + (aBigDecimal.add(bBigDecimal).equals(cBigDecimal))); // 0.1 + 0.2 == 0.3 (BigDecimal)? true
    }
}
