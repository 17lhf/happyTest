package com.basic.happytest.modules.sqlOperation.enums;

/**
 * sql操作统计测试学科分数实体的学科枚举
 * @author lhf
 */
public enum StatSubjectTypeEnums {
    /**
     * 语文
     */
    CHINESE(0),

    /**
     * 数学
     */
    MATH(1),

    /**
     * 英语
     */
    ENGLISH(2);

    StatSubjectTypeEnums(Integer type) {
        this.type = type;
    }

    private final Integer type;

    public Integer getType() {
        return type;
    }
}
