package com.basic.happytest.modules.sqlOperation.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 学生各个学籍的状态
 * @author lhf
 */

@Getter
@Setter
public class StudentCondDto {
    /**
     * 总数
     */
    private Integer totalCnt;

    /**
     * 已注册且在读数
     */
    private Integer enrollmentCnt;

    /**
     * 保留学籍数
     */
    private Integer retentionCnt;

    /**
     * 已离校数
     */
    private Integer leftCnt;
}
