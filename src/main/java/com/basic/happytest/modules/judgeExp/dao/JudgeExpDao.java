package com.basic.happytest.modules.judgeExp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.basic.happytest.modules.judgeExp.entity.JudgeExp;
import com.basic.happytest.modules.judgeExp.model.Cond;
import com.basic.happytest.modules.judgeExp.model.CondNum;
import com.basic.happytest.modules.judgeExp.model.CondStr;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 实验当传参传参数时对应的xml里的if语句应该怎么写的 dao层
 * @author lhf
 */

@Mapper
@Repository
public interface JudgeExpDao extends BaseMapper<JudgeExp> {

    /**
     * 探索当提供的值为基础数据类型数字0时，mybatis的xml条件判断会怎样
     * @param num 要查的满足num_value应该等于的值
     * @return 满足条件的列表
     */
    List<JudgeExp> listByZeroValue(Integer num);

    /**
     * 探索当提供的值为一个对象，但是对象里的值为基础数据类型数字0时，mybatis的xml条件判断会怎样
     * @param condNum 装有 要查的满足num_value应该等于的值的 对象
     * @return 满足条件的列表
     */
    List<JudgeExp> listByZeroNumValue(CondNum condNum);

    /**
     * 只用一个int值来查
     * @param num 要查的满足num_value应该等于的值
     * @return 满足条件的列表
     */
    List<JudgeExp> listByNumValue(Integer num);  // 如果你这里把Integer换成int，实际上也行

    /**
     * 只用一个String值来查
     * @param str 要查的满足str_value应该等于的值
     * @return 满足条件的列表
     */
    List<JudgeExp> listByStrValue(String str);

    /**
     * 用一个对象来查
     * @param cond 要获得的数据应该满足的条件
     * @return 满足条件的列表
     */
    List<JudgeExp> listByCond(Cond cond);

    /**
     * 同时使用int值和字符串来查
     * @param num num_value应该等于的值
     * @param str str_value应该等于的值
     * @return 满足条件的列表
     */
    List<JudgeExp> listByNumAndStr(Integer num, String str);

    /**
     * 同时用两个包含了查询条件的对象来查
     * @param condNum 包含了num_value查询条件的对象
     * @param condStr 包含了star_value查询条件的对象
     * @return 满足条件的列表
     */
    List<JudgeExp> listByTwoCondClass(CondNum condNum, CondStr condStr);

    /**
     * 从外部来的查询条件进行查询（主要是研究外部传来的0在xml里应该怎么避免if判断时被认为是空字符串）
     * 注意：Map<String, Object> params值必须是写成Object才行
     * @param params 参数(只使用num_value查询条件)
     * @return 满足条件的列表
     */
    List<JudgeExp> listSelectFromExternal(Map<String, Object> params);

    /**
     * 使用单个数字作为条件进行查询，同时test中对入参判断不等于undefined
     * @param num 入参数字
     * @return 满足条件的列表
     */
    List<JudgeExp> listByNumValueAndUseTestUndefined(Integer num);
}
