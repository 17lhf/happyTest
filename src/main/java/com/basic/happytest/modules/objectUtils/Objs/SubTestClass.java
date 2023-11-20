package com.basic.happytest.modules.objectUtils.Objs;

import lombok.Getter;
import lombok.Setter;

/**
 * 子测试类
 * @author lhf
 */

@Getter
@Setter
public class SubTestClass extends FTestClass{

    /**
     * 子测试类的数字
     */
    private Integer subNum;

    SubTestClass(final Integer ftNum, final Integer subNum) {
        super(ftNum);
        this.subNum = subNum;
    }
}
