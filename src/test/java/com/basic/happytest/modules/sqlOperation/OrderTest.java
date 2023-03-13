package com.basic.happytest.modules.sqlOperation;

import com.basic.happytest.modules.randomUtils.GenChineseName;
import com.basic.happytest.modules.sqlOperation.dao.OrderTeacherDao;
import com.basic.happytest.modules.sqlOperation.model.OrderTeacher;
import com.basic.happytest.modules.time.TimeUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

/**
 * 排序 SQL测试
 * @author lhf
 */

@SpringBootTest
public class OrderTest {
    private static final Logger log = LoggerFactory.getLogger(OrderTest.class);

    @Autowired
    private OrderTeacherDao orderTeacherDao;

    // 教师数
    int teacherNum = 20;

    /**
     * 数据库初始化，必须先执行这一步，然后才有数据用来测试sql操作
     */
    @Test
    public void initial() throws InterruptedException {
        orderTeacherDao.delete(null);
        // 每多少个教师创建时间相同
        int sameCreTimePerNum = 8;
        Date now = new Date();
        for (int i = 0; i < teacherNum; i++) {
            if(i % sameCreTimePerNum == 0) {
                now = new Date();
            } else {
                Thread.sleep(500);
            }
            OrderTeacher orderTeacher = new OrderTeacher();
            orderTeacher.setName(GenChineseName.getChineseName());
            orderTeacher.setCreTime(now);
            orderTeacherDao.insert(orderTeacher);
        }
    }

    /**
     * 测试寻常的分页排序
     * 查询时，如果依据排序列有相同项，则mysql会随机取相同的项的数据，导致数据混乱
     * 这里单元测试执行的结果顺序是混乱的，甚至会出现重复
     */
    @Test
    public void testNormalOrder() throws InterruptedException {
        int beginNum = 0;
        int num = 5;
        while (beginNum < teacherNum) {
            log.info("BeginNum: " + beginNum + ", EndNum: " + (beginNum + num));
            List<OrderTeacher> list = orderTeacherDao.query(beginNum, num);
            for (OrderTeacher orderTeacher : list) {
                log.info("teacher ID: " + orderTeacher.getId() + ", teacher create time: "
                        + TimeUtils.getFormatTime(orderTeacher.getCreTime(), TimeUtils.TIME_PATTERN)
                        + ", teacher create time: " + orderTeacher.getCreTime().getTime());
            }
            beginNum = Math.min(beginNum + num, teacherNum);
            // 如果内存足够大，执行mysql的时候会提供足够大的缓冲池，在mysql客户端存在缓存时每一次查询都走缓存所以顺序会正常
            // 如果不设置等待时间，则会顺序正常
            // 但是如果查询的间隔的时间长一些就不一定走缓存了，于是这里加了等待时间
            Thread.sleep(3000);
        }
    }

    /**
     * 测试多列排序的查询
     * 查询时，如果依据排序列有相同项，则mysql会随机取相同的项的数据，导致数据混乱
     * 所以可以使用多列查询，左边的列的排序优先级高于右侧，若左边的列重复时，就会用右边的列进行排序，若左边的列没有重复，则不会用右边的列进行排序
     * 例如本例测试中，creTime优先排序（升序），creTime相同时，才使用主键ID进行排序(降序)
     * 这里单元测试的结果顺序是正常的（因为主键肯定不同，此时肯定能排序）
     */
    @Test
    public void testMultiOrder() throws InterruptedException {
        int beginNum = 0;
        int num = 5;
        while (beginNum < teacherNum) {
            log.info("BeginNum: " + beginNum + ", EndNum: " + (beginNum + num));
            List<OrderTeacher> list = orderTeacherDao.queryMultiOrder(beginNum, num);
            for (OrderTeacher orderTeacher : list) {
                log.info("teacher ID: " + orderTeacher.getId() + ", teacher create time: "
                        + TimeUtils.getFormatTime(orderTeacher.getCreTime(), TimeUtils.TIME_PATTERN)
                        + ", teacher create time: " + orderTeacher.getCreTime().getTime());
            }
            beginNum = Math.min(beginNum + num, teacherNum);
            // 如果内存足够大，执行mysql的时候会提供足够大的缓冲池，在mysql客户端存在缓存时每一次查询都走缓存所以顺序会正常
            // 这里同上排除缓存影响
            Thread.sleep(3000);
        }
    }
}
