package com.basic.happytest.modules.mp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * Mybatis Plus相关测试的实体
 * @author lhf
 */

@Getter
@Setter
@TableName("mp")
public class Mp {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 放数字的值
     */
    private Integer numValue;

    /**
     * 放字符串的值
     */
    private String strValue;
}
