package com.basic.happytest.modules.sqlOperation.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * sql操作统计测试学科分数实体
 * @author lhf
 */

@Getter
@Setter
@TableName("t_stat_subject_score")
public class StatSubjectScore {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 分数
     */
    private Double score;

    /**
     * 学生ID
     */
    private Integer stuId;

    /**
     * 学科类型 {@link com.basic.happytest.modules.sqlOperation.enums.StatSubjectTypeEnums}
     */
    private Integer subjectType;

    /**
     * 创建时间
     */
    private Date creTime;

    /**
     * 更新时间
     */
    private Date updTime;
}
