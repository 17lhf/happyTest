package com.basic.happytest.modules.sqlOperation.dao;

import com.basic.happytest.modules.sqlOperation.entity.ClassCondDto;
import com.basic.happytest.modules.sqlOperation.entity.ClassTopScoreCondDto;
import com.basic.happytest.modules.sqlOperation.entity.ClassTotalScoreGradientStatDto;
import com.basic.happytest.modules.sqlOperation.entity.StudentCondDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Sql操作测试
 * @author lhf
 */

@Mapper
@Repository
public interface StatSqlDao {

    /**
     * 获取学生的学籍情况 (带条件的count)
     * @param enrollmentStatus 已注册且在读
     * @param retentionStatus 保留学籍
     * @param leftStatus 已离校
     * @return 学生的学籍情况
     */
    StudentCondDto getStudentCondDto(@Param("enrollmentStatus") Integer enrollmentStatus,
                                     @Param("retentionStatus") Integer retentionStatus,
                                     @Param("leftStatus") Integer leftStatus);

    /**
     * 获取各个班级的学生情况 (先分组再在组内带条件进行分类统计)
     * @param enrollmentStatus 已注册且在读
     * @param retentionStatus 保留学籍
     * @param leftStatus 已离校
     * @return 各个班级的学生情况
     */
    List<ClassCondDto> listClassCondDto(@Param("enrollmentStatus") Integer enrollmentStatus,
                                        @Param("retentionStatus") Integer retentionStatus,
                                        @Param("leftStatus") Integer leftStatus);

    /**
     * 获取各个班级总分前N的学生成绩情况 <br/>
     * 1、自定义映射来返回包含子对象的对象 <br/>
     * 2、支持限制每个分组内按顺序排列的前n个结果（使用的row_number()写法得mysql8才支持）<br/>
     * 3、带条件判断的求和，分组后的求和 <br/>
     * 几大排序函数：<br/>
     * row_numer()：按查出的记录数前后排序，序号不重复。即第1条记录序号为1，第2条记录序号2，第3条记录序号为3（不考虑3条记录的排序字段是否重复）。<br/>
     * rank()：跳跃排序，排序字段值相同的序号相同。例如3条记录中前2条排序字段值相同，第3条不同，则前3条记录的排序号为1,1,3。 <br/>
     * dense_rank()：连续排序。例如前4条记录中，1和2的排序字段值相同，3和4的排序字段值相同，则4条记录的排序号为1,1,2,2。 <br/>
     * @param enrollmentStatus 学生必须是已注册且在读的状态
     * @param chineseSubject 语文学科标识
     * @param mathSubject 数学学科标识
     * @param englishSubject 英语学科标识
     * @param topNum 需要的总分排名前N的学生
     * @return 各个班级总分前N的学生成绩情况
     */
    List<ClassTopScoreCondDto> listClassTopScoreCondDto(@Param("enrollmentStatus") Integer enrollmentStatus,
                                                        @Param("chineseSubject") Integer chineseSubject,
                                                        @Param("mathSubject") Integer mathSubject,
                                                        @Param("englishSubject") Integer englishSubject,
                                                        @Param("topNum") Integer topNum);

    /**
     * 获取各个班级内总成绩的各个层级的人数 <br/>
     * 1、自定义分组 <br/>
     * 2、分组后再分组 <br/>
     * 3、分组后排序 <br/>
     * @param enrollmentStatus 学生状态要求是在读
     * @return 各个班级内总成绩的各个层级的人数
     */
    List<ClassTotalScoreGradientStatDto> listClassTotalScoreGradientStat(@Param("enrollmentStatus") Integer enrollmentStatus);
}
