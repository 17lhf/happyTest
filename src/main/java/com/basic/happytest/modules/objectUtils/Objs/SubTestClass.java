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

    private static Integer staticNum = 1;

    public SubTestClass(final Integer ftNum, final Integer subNum) {
        super(ftNum);
        this.subNum = subNum;
    }

    // 这里不能写 @Override，因为不是重写
    public static void printOneStr() {
        System.out.println("SubTestClass static method: getOneStr");
    }

    @Override
    public void printAnotherStr() {
        printOneStr();
        System.out.println("SubTestClass method: printAnotherStr");
    }
}
