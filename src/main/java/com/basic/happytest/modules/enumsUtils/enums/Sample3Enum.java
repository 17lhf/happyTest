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
}
