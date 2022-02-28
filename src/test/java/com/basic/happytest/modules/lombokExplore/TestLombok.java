package com.basic.happytest.modules.lombokExplore;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

public class TestLombok {

    @Test
    public void testLombok(){
        SubClassA subClassA = new SubClassA();
        SubClassA subClassAA = new SubClassA();
        SubClassA1 subClassA1 = new SubClassA1();
        SubClassA1 subClassA11 = new SubClassA1();
        SubClassA2 subClassA2 = new SubClassA2();

        System.out.println("-------------set @EqualsAndHashCode(callSuper = true) ------------------");
        // 一开始，确实相等
        if(subClassA.equals(subClassAA)){
            System.out.println("subClassA == SubClassAA");
            System.out.println("subClassA: " + subClassA);
            System.out.println("subClassAA: " + subClassAA);
            System.out.println();
        }
        // 但是当subClassAA的父类值改了以后，就不等于了
        subClassAA.setB(10L);
        if(!subClassA.equals(subClassAA)){
            System.out.println("subClassA != SubClassAA");
            System.out.println("subClassA: " + subClassA);
            System.out.println("subClassAA: " + subClassAA);
            System.out.println();
        }

        System.out.println("-------------set @EqualsAndHashCode(callSuper = false) ------------------");
        // 一开始，确实相等
        if(subClassA1.equals(subClassA11)){
            System.out.println("subClassA1 == SubClassA11");
            System.out.println("subClassA1: " + subClassA1);
            System.out.println("subClassA11: " + subClassA11);
            System.out.println();
        }
        // 当是当subClassA1的父类值改变后，仍旧相等
        subClassA1.setB(11L);
        if(subClassA1.equals(subClassA11)){
            System.out.println("subClassA1 == SubClassA11");
            System.out.println("subClassA1: " + subClassA1);
            System.out.println("subClassA11: " + subClassA11);
            System.out.println();
        }

        System.out.println("--------sub class to json test (set callSuper)---------");
        System.out.println("subClassA: " + subClassA);
        String subClassAJSON = JSON.toJSONString(subClassA);
        System.out.println("SubClassA JSON: " + subClassAJSON);
        System.out.println();

        System.out.println("--------sub class to json test (no set callSuper)---------");
        System.out.println("subClassA2: " + subClassA2);
        String subClassA2JSON = JSON.toJSONString(subClassA2);
        System.out.println("SubClassA2 JSON: " + subClassA2JSON);
    }
}
