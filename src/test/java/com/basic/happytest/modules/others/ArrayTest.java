package com.basic.happytest.modules.others;

import org.junit.jupiter.api.Test;

/**
 * 关于数组的一些测试
 * @author lhf
 */

public class ArrayTest {

    public class TestClass {
        private Integer num;

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }
    }

    /**
     * 关于取值的测试（是不是引用取值）
     */
    @Test
    public void getTest() {
        TestClass[] testClasses = new TestClass[3];
        for (int i = 0; i < 3; i++) {
            TestClass testClass = new TestClass();
            testClass.setNum(i);
            testClasses[i] = testClass;
        }
        for (TestClass testClass : testClasses) {
            System.out.println("初始num: " + testClass.getNum());
        }
        // 这里获取的是引用，对temp的修改将直接影响到原来的数组
        TestClass temp = testClasses[0];
        temp.setNum(100);
        for (TestClass testClass : testClasses) {
            System.out.println("num: " + testClass.getNum());
        }
    }
}
