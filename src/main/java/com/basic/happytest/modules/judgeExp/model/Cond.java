package com.basic.happytest.modules.judgeExp.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 用于放同时含数字和字符串的搜索条件
 * @author lhf
 */

@Getter
@Setter
public class Cond {

    private Integer num;

    private String str;

    public Cond(Integer num, String str){
        this.num = num;
        this.str = str;
    }
}
