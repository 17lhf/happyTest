package com.basic.happytest.modules.objectUtils.Objs;

/**
 * 测试用的类
 * @author lhf
 */

public class TestClass {

    private Integer num;

    public Float pubFloat;

    final private String str;

    public TestClass () {
        System.out.println("use testClass construct1 method");
        this.num = 0;
        this.str = "abc";
        this.pubFloat = 1.1f;
    }

    public TestClass(Integer num, String str, Float pubFloat) {
        System.out.println("use testClass construct2 method");
        this.num = num;
        this.str = str;
        this.pubFloat = pubFloat;
    }

    public Integer getNum() {
        System.out.println("use getNum method");
        return num;
    }

    public void setNum(Integer num) {
        System.out.println("use setNum method");
        this.num = num;
    }

    public String getStr() {
        return str;
    }
}
