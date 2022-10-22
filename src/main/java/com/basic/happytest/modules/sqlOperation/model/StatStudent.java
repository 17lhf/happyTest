package com.basic.happytest.modules.sqlOperation.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * sql测试统计用的学生实体
 * @author lhf
 */

@Getter
@Setter
@TableName("t_stat_student")
public class StatStudent {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 学生名字
     */
    private String name;

    /**
     * 学生当前状态 {@link com.basic.happytest.modules.sqlOperation.enums.StatStudentStatusEnums}
     */
    private Integer status;

    /**
     * 学生归属班级
     */
    private Integer classId;

    /**
     * 创建时间
     */
    private Date creTime;

    /**
     * 更新时间
     */
    private Date updTime;
}
