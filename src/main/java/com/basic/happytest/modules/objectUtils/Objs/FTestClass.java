package com.basic.happytest.modules.objectUtils.Objs;

import lombok.Getter;
import lombok.Setter;

/**
 * 父级测试类
 * @author lhf
 */
@Getter
@Setter
public class FTestClass {
    /**
     * 父类的数字
     */
    private Integer ftNum;

    FTestClass(final Integer ftNum) {
        this.ftNum = ftNum;
    }
}
