package com.basic.happytest.modules.sqlOperation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.basic.happytest.modules.sqlOperation.model.StatSubjectScore;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * sql操作统计测试学科分数Dao
 * @author lhf
 */

@Mapper
@Repository
public interface StatSubjectScoreDao extends BaseMapper<StatSubjectScore> {
}
