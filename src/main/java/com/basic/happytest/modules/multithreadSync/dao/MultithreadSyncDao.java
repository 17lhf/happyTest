package com.basic.happytest.modules.multithreadSync.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.basic.happytest.modules.multithreadSync.entity.MultithreadSync;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MultithreadSyncDao extends BaseMapper<MultithreadSync> {
    /**
     * 以某一初始值，添加一个新项
     * @param multithreadSync 新项
     */
    void add(MultithreadSync multithreadSync);

    /**
     * 对id = id的数据进行value的+1递增测试
     * @param id 项的id
     */
    void increaseValue(Integer id);

    /**
     * 获取id对应的项
     * @param id id
     */
    MultithreadSync selectObjectById(Integer id);

    /**
     * 更新id对应的信息
     * @param id id
     */
    void updateValueById(Integer id, Integer newValue);
}