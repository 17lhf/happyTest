package com.basic.happytest.modules.lombokExplore;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

public class TestLombok {

    /**
     * todo
     */
    @Test
    public void testLombok(){
        SubClassA subClassA = new SubClassA();
        SubClassA subClassA1 = new SubClassA();
        if(subClassA.equals(subClassA1)){
            System.out.println("subClassA == SubClassA1");
            System.out.println("subClassA: " + subClassA.toString());
            System.out.println("subClassA1: " + subClassA1.toString());
            System.out.println();
        }
        subClassA1.setB(10L);
        if(!subClassA.equals(subClassA1)){
            System.out.println("subClassA != SubClassA1");
            System.out.println("subClassA: " + subClassA.toString());
            System.out.println("subClassA1: " + subClassA1.toString());
            System.out.println();
        }

        System.out.println("--------sub class to json test---------");
        System.out.println("subClassA: " + subClassA.toString());
        String subClassAJSON = JSON.toJSONString(subClassA);
        System.out.println("SubClassA JSON: " + subClassAJSON);
    }
}
