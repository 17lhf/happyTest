package com.basic.happytest.modules.unboundedWildcardsAndGenerics;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 无界符和通配符的区别
 * @author lhf
 */

public class UnboundedWildcardsAndGenericsTest {
    @Test
    public void testGeneric() {
        // https://www.zhihu.com/question/31429113
        Box<String> box = new Box<>();
        List<String> list = new ArrayList<>();
        list.add("aaa");
        box.setList(list);
        box.setList2(list);
        System.out.println(box.getList().get(0));
        System.out.println(box.getList2().get(0));
        System.out.println("=======================");
        Box<Integer> box1 = new Box<>();
        List<Integer> list1 = new ArrayList<>();
        list1.add(0);
        box1.setList(list1);
        box.getSet(box1);
        System.out.println(box1.getList().get(0));
        System.out.println("=======================");
        List<Double> list2 = new ArrayList<>();
        list2.add(1.0);
        Box<Double> box2 = new Box<>(list2, list2);
        System.out.println(box2.getList().get(0));
        System.out.println(box2.getList2().get(0));
    }
}
