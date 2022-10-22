package com.basic.happytest.modules.sqlOperation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.basic.happytest.modules.sqlOperation.model.StatClass;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * sql测试统计用的班级Dao
 * @author lhf
 */

@Mapper
@Repository
public interface StatClassDao extends BaseMapper<StatClass> {
}
