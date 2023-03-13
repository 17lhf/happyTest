package com.basic.happytest.modules.sqlOperation.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.basic.happytest.modules.time.TimeUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * sql测试排序用的老师实体
 * @author lhf
 */

@Getter
@Setter
@TableName("t_order_teacher")
public class OrderTeacher {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 名字
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

    /**
     * 打印信息
     */
    public void print() {
        System.out.println("id= " + id + ", name= " + name + ", Create Time= " + TimeUtils.getFormatTime(creTime, TimeUtils.TIME_PATTERN));
    }
}
