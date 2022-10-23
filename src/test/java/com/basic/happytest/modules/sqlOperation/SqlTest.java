package com.basic.happytest.modules.sqlOperation;

import com.basic.happytest.modules.randomUtils.GenChineseName;
import com.basic.happytest.modules.randomUtils.GenNumber;
import com.basic.happytest.modules.sqlOperation.dao.StatClassDao;
import com.basic.happytest.modules.sqlOperation.dao.StatSqlDao;
import com.basic.happytest.modules.sqlOperation.dao.StatStudentDao;
import com.basic.happytest.modules.sqlOperation.dao.StatSubjectScoreDao;
import com.basic.happytest.modules.sqlOperation.entity.*;
import com.basic.happytest.modules.sqlOperation.enums.StatStudentStatusEnums;
import com.basic.happytest.modules.sqlOperation.enums.StatSubjectTypeEnums;
import com.basic.happytest.modules.sqlOperation.model.StatClass;
import com.basic.happytest.modules.sqlOperation.model.StatStudent;
import com.basic.happytest.modules.sqlOperation.model.StatSubjectScore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Sql操作测试
 * @author lhf
 */

@SpringBootTest
public class SqlTest{

    @Autowired
    private StatClassDao statClassDao;

    @Autowired
    private StatStudentDao statStudentDao;

    @Autowired
    private StatSubjectScoreDao statSubjectScoreDao;

    @Autowired
    private StatSqlDao statSqlDao;

    /**
     * 初始化数据, 必须先执行这一步，然后才有数据用来测试各种复杂sql操作
     */
    @Test
    public void initialData() {
        // 4个班级
        int classNum = 4;
        // 200个学生
        int studentNum = 200;
        for (int i = 0; i < classNum; i++) {
            StatClass statClass = new StatClass();
            statClass.setName((i + 1) + "班");
            statClass.setCreTime(new Date());
            statClassDao.insert(statClass);
        }
        Random random = new Random();
        for (int i = 0; i < studentNum; i++) {
            StatStudent statStudent = new StatStudent();
            statStudent.setName(GenChineseName.getChineseName());
            statStudent.setStatus(StatStudentStatusEnums.values()[random.nextInt(StatStudentStatusEnums.values().length)].getStatus());
            statStudent.setClassId(random.nextInt(classNum) + 1);
            statStudent.setCreTime(new Date());
            statStudentDao.insert(statStudent);
        }
        for (int i = 0; i < StatSubjectTypeEnums.values().length; i++) {
            Integer subjectType = StatSubjectTypeEnums.values()[i].getType();
            for (int j = 0; j < studentNum; j++) {
                StatSubjectScore statSubjectScore = new StatSubjectScore();
                double score = GenNumber.genDoubleNumber(0.0, true, 100.0, true);
                BigDecimal bigDecimal = new BigDecimal(score);
                statSubjectScore.setScore(bigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue());
                statSubjectScore.setStuId(j + 1);
                statSubjectScore.setSubjectType(subjectType);
                statSubjectScore.setCreTime(new Date());
                statSubjectScoreDao.insert(statSubjectScore);
            }
        }
    }

    @Test
    public void getStudentCondDtoTest() {
        StudentCondDto dto = statSqlDao.getStudentCondDto(StatStudentStatusEnums.ENROLLMENT.getStatus(),
                StatStudentStatusEnums.RETENTION.getStatus(), StatStudentStatusEnums.LEFT.getStatus());
        System.out.println("学生总数: " + dto.getTotalCnt());
        System.out.println("学生在读数: " + dto.getEnrollmentCnt());
        System.out.println("学生保留学籍数: " + dto.getRetentionCnt());
        System.out.println("学生离校数: " + dto.getLeftCnt());
    }

    @Test
    public void getClassCondTest() {
        List<ClassCondDto> dtoList = statSqlDao.listClassCondDto(StatStudentStatusEnums.ENROLLMENT.getStatus(),
                StatStudentStatusEnums.RETENTION.getStatus(), StatStudentStatusEnums.LEFT.getStatus());
        for (ClassCondDto dto : dtoList) {
            System.out.println("班级名：" + dto.getName());
            System.out.println("班级在读人数：" + dto.getEnrolledStuCnt());
            System.out.println("班级保留学籍人数：" + dto.getRetentionStuCnt());
            System.out.println("班级离校人数：" + dto.getLeftStuCnt());
        }
    }

    @Test
    public void listClassTopScoreCondTest() {
        List<ClassTopScoreCondDto> list = statSqlDao.listClassTopScoreCondDto(StatStudentStatusEnums.ENROLLMENT.getStatus(),
                StatSubjectTypeEnums.CHINESE.getType(), StatSubjectTypeEnums.MATH.getType(),
                StatSubjectTypeEnums.ENGLISH.getType(), 3);
        if(list == null || list.isEmpty()) {
            System.out.println("列表为空");
        } else {
            for (ClassTopScoreCondDto dto : list) {
                System.out.println("班级名：" + dto.getName());
                for (StuScoreCondDto scoreCondDto : dto.getScoreCondList()) {
                    // 因为精度问题，所以要转换一下总分
                    BigDecimal bigDecimal = BigDecimal.valueOf(scoreCondDto.getSumScore());
                    System.out.println("学生名：" + scoreCondDto.getName()
                            + " 总分：" + bigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue()
                            + " 语文：" + scoreCondDto.getChineseScore()
                            + " 数学：" + scoreCondDto.getMathScore() + " 英语：" + scoreCondDto.getEnglishScore());
                }
            }
        }
    }

    @Test
    public void listClassTotalScoreGradientStatTest() {
        List<ClassTotalScoreGradientStatDto> dtoList = statSqlDao.listClassTotalScoreGradientStat(StatStudentStatusEnums.ENROLLMENT.getStatus());
        for (ClassTotalScoreGradientStatDto dto : dtoList) {
            System.out.println("班级名：" + dto.getName());
            System.out.println("A: " + dto.getA());
            System.out.println("B: " + dto.getB());
            System.out.println("C: " + dto.getC());
            System.out.println("D: " + dto.getD());
        }
    }
}
