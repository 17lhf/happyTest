package com.basic.happytest.modules.judgeExp.service;

import com.basic.happytest.modules.judgeExp.dao.JudgeExpDao;
import com.basic.happytest.modules.judgeExp.entity.JudgeExp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 测试从前端传来数据时，接收的数据拿去作为查询条件，会怎样
 * @author lhf
 */

@Service
public class JudgeExpService {
    @Autowired
    JudgeExpDao judgeExpDao;

    public List<JudgeExp> select(Map<String, Object> params){
        return judgeExpDao.listSelectFromExternal(params);
    }
}
