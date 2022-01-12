package com.basic.happytest.modules.judgeExp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.basic.happytest.modules.judgeExp.entity.JudgeExp;
import com.basic.happytest.modules.judgeExp.model.Cond;
import com.basic.happytest.modules.judgeExp.model.CondNum;
import com.basic.happytest.modules.judgeExp.model.CondStr;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 实验当传参只传一个参数时对应的xml怎么写的 dao层
 * @author lhf
 */

@Mapper
@Repository
public interface JudgeExpDao extends BaseMapper<JudgeExp> {
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
}
