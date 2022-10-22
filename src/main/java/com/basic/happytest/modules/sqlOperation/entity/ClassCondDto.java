package com.basic.happytest.modules.sqlOperation.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 各个班级的在读情况
 * @author lhf
 */

@Getter
@Setter
public class ClassCondDto {
    /**
     * 班级名
     */
    private String name;

    /**
     * 在读人数
     */
    private Integer enrolledStuCnt;

    /**
     * 保留学籍人数
     */
    private Integer retentionStuCnt;

    /**
     * 已离校人数
     */
    private Integer leftStuCnt;
}
