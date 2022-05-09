package com.basic.happytest.modules.mp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.basic.happytest.modules.mp.entity.Mp;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Mybatis Plus相关测试的Dao层
 * @author lhf
 */

@Mapper
@Repository
public interface MpDao extends BaseMapper<Mp> {
}
