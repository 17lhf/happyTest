package com.basic.happytest.modules.sqlOperation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.basic.happytest.modules.sqlOperation.model.StatSubjectScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * sql操作统计测试学科分数Dao
 * @author lhf
 */

@Mapper
@Repository
public interface StatSubjectScoreDao extends BaseMapper<StatSubjectScore> {

    /**
     * 批量添加学科分数
     * @param subjectScoreList 学科分数列表
     */
    void insertBatch(@Param("subjectScoreList") List<StatSubjectScore> subjectScoreList);


}
