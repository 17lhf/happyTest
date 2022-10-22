package com.basic.happytest.modules.sqlOperation.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 班级在读学生的总分前N名情况
 */

@Getter
@Setter
public class ClassTopScoreCondDto {

    /**
     * 班级名称
     */
    private String name;

    /**
     * 总分前N名的学生成绩情况列表
     */
    private List<StuScoreCondDto> scoreCondList;
}
