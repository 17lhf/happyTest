package com.basic.happytest.modules.judgeExp.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 用于放搜索字符串搜索条件
 * @author lhf
 */

@Getter
@Setter
public class CondStr {

    private String str;

    public CondStr(String str){
        this.str = str;
    }
}
