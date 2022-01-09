package com.basic.happytest.modules.multithreadSync.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 多线程并发测试实体
 * @author lhf
 */


@Getter
@Setter
@TableName("t_multithread_sync")
public class MultithreadSync {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 进行并发+1的值
     */
    private Integer valueGen;
    /**
     * 并发操作的描述
     */
    private String description;
}
