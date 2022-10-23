package com.basic.happytest.modules.sqlOperation.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 班级内成绩总分梯度统计
 * @author lhf
 */

@Getter
@Setter
public class ClassTotalScoreGradientStatDto {

    /**
     * 班级名
     */
    private String name;

    /**
     * 成绩为优秀（总分 >= 240）
     */
    private Integer a;

    /**
     * 成绩为良好（总分 ∈ [210, 240)）
     */
    private Integer b;

    /**
     * 成绩为合格（总分 ∈ [180, 210)）
     */
    private Integer c;

    /**
     * 成绩为差（总分 < 180）
     */
    private Integer d;
}
