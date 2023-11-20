package com.basic.happytest.modules.mp.dao;

import com.basic.happytest.modules.mp.entity.Mp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class MpDaoTest {

    @Autowired
    private MpDao mpDao;

    /**
     * 注入初始数据
     */
    // @Test
    public void insertData() {
        final int dataNum = 20;
        for (int i = 0; i < dataNum; i++) {
            Mp mp = new Mp();
            mp.setNumValue(i);
            mp.setStrValue("Str" + i);
            mpDao.insert(mp);
        }
    }

    @Test
    public void selectByMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("numValue", 1);
        mpDao.selectByMap("1", map);
    }
}