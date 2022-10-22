package com.basic.happytest.modules.sqlOperation.enums;

/**
 * sql操作统计测试用学生实体的状态枚举
 * @author lhf
 */

public enum StatStudentStatusEnums {
    /**
     * 注册学籍，标识还在上学，没有毕业
     */
    ENROLLMENT(0),

    /**
     * 保留学籍，表示中断学习，还未毕业，可在一年内复学
     */
    RETENTION(1),

    /**
     * 已离校，说明已经离开学校，可能是毕业，也可能是休学
     */
    LEFT(2),
    ;

    StatStudentStatusEnums(Integer status) {
        this.status = status;
    }

    private final Integer status;

    public Integer getStatus() {
        return status;
    }
}
