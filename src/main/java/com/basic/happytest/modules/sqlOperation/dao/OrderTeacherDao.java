package com.basic.happytest.modules.sqlOperation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.basic.happytest.modules.sqlOperation.model.OrderTeacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * SQL测试排序的教师DAO层
 * @author lhf
 */

@Mapper
@Repository
public interface OrderTeacherDao extends BaseMapper<OrderTeacher> {

    /**
     * 寻常的带排序的查询
     * @param beginNum 起始数
     * @param num 查询数
     * @return 结果
     */
    List<OrderTeacher> query(@Param("beginNum") int beginNum, @Param("num") int num);

    /**
     * 多列排序的查询
     * @param beginNum 起始数
     * @param num 查询数
     * @return 结果
     */
    List<OrderTeacher> queryMultiOrder(@Param("beginNum") int beginNum, @Param("num") int num);
}
