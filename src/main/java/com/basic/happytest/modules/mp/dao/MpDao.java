package com.basic.happytest.modules.mp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.basic.happytest.modules.mp.entity.Mp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Mybatis Plus相关实验的Dao层
 * @author lhf
 */

@Mapper
@Repository
public interface MpDao extends BaseMapper<Mp> {

    /**
     * 分页查询 <br />
     * 实测在有多个参数的时候，3.0.1开始不使用@Param("params")注解，且数据库脚本参数的写法不带params.,直接尝试读取Map里的值
     * 则会报错：Parameter 'numValue' not found. Available parameters are [page, params, param1, param2]。
     * 2.3.3则不会报错，xml里用key能正常取到值。(但是为了兼容性，最好还是加上注解)
     * @param page 分页信息
     * @param params 查询参数
     * @return 分页结果
     */
    Page<Mp> selectPageVo(Page<Mp> page, @Param("params") Map<String, Object> params);

    /**
     * 查询 <br />
     * 实测在有多个参数的时候，3.0.1开始不使用@Param("params")注解，且数据库脚本参数的写法不带params.,直接尝试读取Map里的值
     * 则会报错：Parameter 'numValue' not found. Available parameters are [page, params, param1, param2]。
     * 2.3.3则不会报错，xml里用key能正常取到值。(但是为了兼容性，最好还是加上注解)
     * 但是此例可以发现，str其实是可以正常取到值的，即便没有设置注解。
     * @param str 字符串查询条件
     * @param params 查询参数
     * @return 符合条件的结果
     */
    List<Mp> selectByMap(String str, @Param("params") Map<String, Object> params);
}
