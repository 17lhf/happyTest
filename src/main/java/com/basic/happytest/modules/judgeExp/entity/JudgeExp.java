package com.basic.happytest.modules.judgeExp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 实验当传参只传一个参数时对应的xml怎么写的 实体层
 * @author lhf
 */

@Setter
@Getter
@TableName("t_judge_experiment")
public class JudgeExp {

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
