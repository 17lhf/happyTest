package com.basic.happytest.modules.judgeExp.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 用于放搜索数字条件
 * @author lhf
 */

@Getter
@Setter
public class CondNum {

    private Integer num;

    public CondNum(Integer num){
        this.num = num;
    }
}
