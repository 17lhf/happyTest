package com.basic.happytest.modules.lombokExplore;

import lombok.Data;


@Data
public class ClassA {

    private char c;

    private int i;

    private Long b;

    private String s;

    public ClassA(){
        c = 'c';
        i = 1;
        b = 1111L;
        s = "abc";
    }
}
