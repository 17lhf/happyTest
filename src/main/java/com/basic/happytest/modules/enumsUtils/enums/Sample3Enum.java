package com.basic.happytest.modules.enumsUtils.enums;

import lombok.Getter;

/**
 * 枚举示例类3
 * @author : lhf
 */

@Getter
public enum Sample3Enum {

    A("a", "11"),
    B("b", "12"),
    C("c", "13");

    private final String str;
    private final String numStr;

    Sample3Enum(String str, String numStr) {
        this.str = str;
        this.numStr = numStr;
    }

    /**
     * 比较逻辑
     * @param str 待比较属性1
     * @param numStr 待比较属性2
     * @return 若两个属性恰好是枚举值的属性，则返回true，否则返回false
     */
    public boolean isAttributeEquals(String str, String numStr) {
        return this.getStr().equals(str) && this.getNumStr().equals(numStr);
    }
}
