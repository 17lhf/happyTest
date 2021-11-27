package com.basic.happytest.modules.multithreadSync.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("t_multithread_sync")
public class MultithreadSync {

    @TableId(type = IdType.AUTO)
    Integer id;

    Integer valueGen;

    String description;
}
