package com.basic.happytest.modules.sqlOperation.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * sql测试统计用的班级实体
 * @author lhf
 */

@Getter
@Setter
@TableName("t_stat_class")
public class StatClass {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 班级名称
     */
    private String name;

    /**
     * 创建时间
     */
    private Date creTime;

    /**
     * 更新时间
     */
    private Date updTime;
}
