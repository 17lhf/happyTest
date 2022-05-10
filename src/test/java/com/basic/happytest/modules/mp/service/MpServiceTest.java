package com.basic.happytest.modules.mp.service;

import com.basic.happytest.modules.mp.entity.Mp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class MpServiceTest {
    @Autowired
    MpService mpService;

    /**
     * 当使用updateById时，若依据的实体对象中，有字段值为null,则MybatisPlus的默认策略会自动忽略这个字段的更新
     * 辅助参考文章： https://blog.csdn.net/weixin_42151235/article/details/121222174
     */
    @Test
    void updByMp() {
        Mp mp = new Mp();
        mp.setNumValue(1);
        mp.setStrValue("one");
        // 先注入一个数据
        Integer mpAfterAddId = mpService.add(mp);
        Mp mpAfterAdd = mpService.getById(mpAfterAddId);
        System.out.println("mpAfterAdd: id=" + mpAfterAdd.getId() + ", num=" + mpAfterAdd.getNumValue() +
                ", str=" + mpAfterAdd.getStrValue());
        // 更新带有null字段的数据
        mp.setId(mpAfterAddId);
        mp.setNumValue(2);
        mp.setStrValue(null);
        mpService.updByMp(mp);
        Mp mpAfterUpdByMp = mpService.getById(mp.getId());
        System.out.println("mpAfterUpdByMp: id=" + mpAfterUpdByMp.getId() + ", num=" + mpAfterUpdByMp.getNumValue() +
                ", str=" + mpAfterUpdByMp.getStrValue());
        // 更新带有""字段(空字符串)的数据
        mp.setId(mpAfterAddId);
        mp.setNumValue(3);
        mp.setStrValue("");
        mpService.updByMp(mp);
        Mp mpAfterUpdByMp2 = mpService.getById(mp.getId());
        System.out.println("mpAfterUpdByMp2: id=" + mpAfterUpdByMp2.getId() + ", num=" + mpAfterUpdByMp2.getNumValue() +
                ", str=" + mpAfterUpdByMp2.getStrValue());
        // 将数字置为null
        mp.setId(mpAfterAddId);
        mp.setNumValue(null);
        mp.setStrValue("three");
        mpService.updByMp(mp);
        Mp mpAfterUpdByMp3 = mpService.getById(mp.getId());
        System.out.println("mpAfterUpdByMp3: id=" + mpAfterUpdByMp3.getId() + ", num=" + mpAfterUpdByMp3.getNumValue() +
                ", str=" + mpAfterUpdByMp3.getStrValue());
        // 将数字置为0
        mp.setId(mpAfterAddId);
        mp.setNumValue(0);
        mp.setStrValue("four");
        mpService.updByMp(mp);
        Mp mpAfterUpdByMp4 = mpService.getById(mp.getId());
        System.out.println("mpAfterUpdByMp4: id=" + mpAfterUpdByMp4.getId() + ", num=" + mpAfterUpdByMp4.getNumValue() +
                ", str=" + mpAfterUpdByMp4.getStrValue());
        // 删除测试数据
        mpService.delById(mpAfterAddId);
        // 结果：
        // mpAfterAdd: id=1, num=1, str=one
        // mpAfterUpdByMp: id=1, num=2, str=one
        // mpAfterUpdByMp2: id=1, num=3, str=
        // mpAfterUpdByMp3: id=1, num=3, str=three
        // mpAfterUpdByMp4: id=1, num=0, str=four
        // 可以看到，str和num的字段并没有变成null，依旧是原来的值，更新的信息被忽略了
        // 但是str的字段被置为空字符串倒是没有问题，num被置零也没问题
    }
}