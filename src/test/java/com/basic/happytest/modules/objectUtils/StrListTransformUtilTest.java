package com.basic.happytest.modules.objectUtils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符串和列表相互转换的工具类测试
 * @author : lhf
 */

class StrListTransformUtilTest {

    @Test
    void str2ListNumber() {
        List<Integer> list = new ArrayList<>(3);
        list.add(1);
        list.add(2);
        list.add(3);
        String listStr = list.toString();
        System.out.println(listStr); // [1, 2, 3]
        List<Integer> newList = StrListTransformUtil.str2List(listStr, Integer.class);
        System.out.println("Integer: " + newList);
        List<Short> newList2 = StrListTransformUtil.str2List(listStr, Short.class);
        System.out.println("Short: " + newList2);
        List<Long> newList3 = StrListTransformUtil.str2List(listStr, Long.class);
        System.out.println("Long: " + newList3);
        List<String> newList4 = StrListTransformUtil.str2List(listStr, String.class);
        System.out.println("String: " + newList4);
        List<Float> newList5 = StrListTransformUtil.str2List(listStr, Float.class);
        System.out.println("Float: " + newList5);
        List<Double> newList6 = StrListTransformUtil.str2List(listStr, Double.class);
        System.out.println("Double: " + newList6);
    }

    @Test
    void str2ListBool() {
        List<Boolean> list = new ArrayList<>(3);
        list.add(true);
        list.add(false);
        list.add(true);
        String listStr = list.toString();
        System.out.println(listStr); // [true, false, true]
        List<Boolean> newList = StrListTransformUtil.str2List(listStr, Boolean.class);
        System.out.println(" : " + newList);
    }

    @Test
    void str2ListFloat() {
        List<Float> list = new ArrayList<>(3);
        list.add(1.1f);
        list.add(2.1f);
        list.add(3.1f);
        String listStr = list.toString();
        System.out.println(listStr); // [1.1, 2.1, 3.1]
        List<Float> newList5 = StrListTransformUtil.str2List(listStr, Float.class);
        System.out.println("Float: " + newList5);
        List<Double> newList6 = StrListTransformUtil.str2List(listStr, Double.class);
        System.out.println("Double: " + newList6);
    }
}