package com.basic.happytest.modules.sqlOperation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.basic.happytest.modules.sqlOperation.model.StatStudent;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * sql测试统计用的学生Dao
 * @author lhf
 */

@Mapper
@Repository
public interface StatStudentDao extends BaseMapper<StatStudent> {
}
