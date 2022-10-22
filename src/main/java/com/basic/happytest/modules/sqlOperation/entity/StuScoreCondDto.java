package com.basic.happytest.modules.sqlOperation.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 学生成绩情况
 * @author lhf
 */

@Getter
@Setter
public class StuScoreCondDto {

    /**
     * 学生名
     */
    private String name;

    /**
     * 总分
     */
    private double sumScore;

    /**
     * 语文成绩
     */
    private double chineseScore;

    /**
     * 数学成绩
     */
    private double mathScore;

    /**
     * 英语成绩
     */
    private double englishScore;
}
