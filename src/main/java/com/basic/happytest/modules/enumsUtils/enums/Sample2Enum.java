package com.basic.happytest.modules.enumsUtils.enums;

import lombok.Getter;

/**
 * 示例枚举类2
 * @author : lhf
 */
@Getter
public enum Sample2Enum {
    A("a"),
    B("b"),
    C("c"),
    ;

    private final String str;

    Sample2Enum(String str) {
        this.str = str;
    }

    /**
     * 常规的枚举比较方式
     * @param value 待比较的值
     * @return true-是期望的枚举值，false-不是
     */
    public static boolean isAttributeEquals(String value) {
        boolean isAttributeEquals = false;
        for (Sample2Enum sample2Enum : Sample2Enum.values()) {
            if (sample2Enum.getStr().equals(value)) {
                isAttributeEquals = true;
                break;
            }
        }
        return isAttributeEquals;
    }

    /**
     * 第一种比较逻辑
     * @param value 待比较的值
     * @return true-是期望的枚举值，false-不是
     */
    public boolean isAttributeEquals2(String value) {
        if (this.equals(Sample2Enum.B)) {
            return false;
        }
        return this.getStr().equals(value);
    }

    /**
     * 第二种比较逻辑
     * @param value 待比较的值
     * @return true-是期望的枚举值，false-不是
     */
    public boolean isAttributeEquals3(String value) {
        return this.getStr().equals(value);
    }
}
