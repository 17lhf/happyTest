package com.basic.happytest.modules.judgeExp.dao;

import com.basic.happytest.modules.judgeExp.entity.JudgeExp;
import com.basic.happytest.modules.judgeExp.model.Cond;
import com.basic.happytest.modules.judgeExp.model.CondNum;
import com.basic.happytest.modules.judgeExp.model.CondStr;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 关于在mapper中写select语句时，特别是写test判断条件的时候，对于入参值的获取以及_parameter的使用测试
 * @author lhf
 * 扩展阅读： https://www.cnblogs.com/straybirds/p/9085414.html
 */

@SpringBootTest
class JudgeExpDaoTest {

    @Autowired
    JudgeExpDao judgeExpDao;

    @Test
    void addSomeData(){
        JudgeExp judgeExp1 = new JudgeExp();
        judgeExp1.setNumValue(1);
        judgeExp1.setStrValue("1");
        judgeExpDao.insert(judgeExp1);
        judgeExpDao.insert(judgeExp1); // 特意插入两个一样的

        JudgeExp judgeExp2 = new JudgeExp();
        judgeExp2.setNumValue(2);
        judgeExp2.setStrValue("2");
        judgeExpDao.insert(judgeExp2);

        JudgeExp judgeExp3 = new JudgeExp();
        judgeExp3.setNumValue(3);
        judgeExp3.setStrValue("3");
        judgeExpDao.insert(judgeExp3);

        JudgeExp judgeExp4 = new JudgeExp();
        judgeExp4.setNumValue(2);
        judgeExp4.setStrValue("3");
        judgeExpDao.insert(judgeExp4);
    }

    /**
     * 比较特殊的用单个数字查
     */
    @Test
    void listByNumValue() {
        List<JudgeExp> judgeExpList = judgeExpDao.listByNumValue(1);
        for (JudgeExp judgeExp: judgeExpList) {
            System.out.println("id = " + judgeExp.getId());
        }
    }

    /**
     * 比较特殊的用单个字符串查
     */
    @Test
    void listByStrValue() {
        List<JudgeExp> judgeExpList = judgeExpDao.listByStrValue("1");
        for (JudgeExp judgeExp: judgeExpList) {
            System.out.println("id = " + judgeExp.getId());
        }
    }

    /**
     * 这是最经常的写法，用类来装查询参数，然后放入mapper里
     */
    @Test
    void listByCond() {
        Cond cond1 = new Cond(1, "1");
        List<JudgeExp> judgeExpList1 = judgeExpDao.listByCond(cond1);
        for (JudgeExp judgeExp: judgeExpList1) {
            System.out.println("id = " + judgeExp.getId());
        }
        System.out.println("=============================================");
        Cond cond2 = new Cond(2, null);
        List<JudgeExp> judgeExpList2 = judgeExpDao.listByCond(cond2);
        for (JudgeExp judgeExp: judgeExpList2) {
            System.out.println("id = " + judgeExp.getId());
        }
        System.out.println("=============================================");
        Cond cond3 = new Cond(null, "3");
        List<JudgeExp> judgeExpList3 = judgeExpDao.listByCond(cond3);
        for (JudgeExp judgeExp: judgeExpList3) {
            System.out.println("id = " + judgeExp.getId());
        }
    }

    /**
     * 用两个基础类型来查，然后用_parameter来读取并test非空
     */
    @Test
    void listByNumAndStr() {
        List<JudgeExp> judgeExpList = judgeExpDao.listByNumAndStr(2, "3");
        for (JudgeExp judgeExp: judgeExpList) {
            System.out.println("id = " + judgeExp.getId());
        }
        System.out.println("=============================================");
        List<JudgeExp> judgeExpList1 = judgeExpDao.listByNumAndStr(null, "1");
        for (JudgeExp judgeExp: judgeExpList1) {
            System.out.println("id = " + judgeExp.getId());
        }
        System.out.println("=============================================");
        List<JudgeExp> judgeExpList2 = judgeExpDao.listByNumAndStr(1, null);
        for (JudgeExp judgeExp: judgeExpList2) {
            System.out.println("id = " + judgeExp.getId());
        }
    }


    /**
     * 使用两个对象作为条件进行查询，使用_parameter来读取并test非空
     */
    @Test
    void listByTwoCondClass() {
        CondNum condNum = new CondNum(2);
        CondNum condNumNull = new CondNum(null);
        CondStr condStr = new CondStr("3");
        CondStr condStrNull = new CondStr(null);

        List<JudgeExp> judgeExpList = judgeExpDao.listByTwoCondClass(condNum, condStr);
        for (JudgeExp judgeExp: judgeExpList) {
            System.out.println("id = " + judgeExp.getId());
        }
        /*System.out.println("=============================================");
        List<JudgeExp> judgeExpList1 = judgeExpDao.listByTwoCondClass(condNumNull, condStr);
        for (JudgeExp judgeExp: judgeExpList1) {
            System.out.println("id = " + judgeExp.getId());
        }
        System.out.println("=============================================");
        List<JudgeExp> judgeExpList2 = judgeExpDao.listByTwoCondClass(condNum, condStrNull);
        for (JudgeExp judgeExp: judgeExpList2) {
            System.out.println("id = " + judgeExp.getId());
        }*/
    }
}