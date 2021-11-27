package com.basic.happytest.modules.multithreadSync.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.basic.happytest.modules.multithreadSync.dao.MultithreadSyncDao;
import com.basic.happytest.modules.multithreadSync.entity.MultithreadSync;
import com.basic.happytest.modules.multithreadSync.service.MultithreadSyncService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MultithreadSyncServiceImpl extends ServiceImpl<MultithreadSyncDao, MultithreadSync> implements MultithreadSyncService {
    /**
     * 统计执行次数
     */
    private static int times;

    @Override
    public void add(Integer initValue, String desc) {
        MultithreadSync multithreadSync = new MultithreadSync();
        multithreadSync.setValueGen(initValue);
        multithreadSync.setDescription(desc);
        baseMapper.add(multithreadSync);
    }

    @Override
    public void increaseValue(Integer id) {
        baseMapper.increaseValue(id);
    }

    @Override
    public MultithreadSync selectObjectById(Integer id) {
        return baseMapper.selectObjectById(id);
    }

    @Override
    public void updateValueById(MultithreadSync multithreadSync) {
        baseMapper.updateValueById(multithreadSync.getId(), multithreadSync.getValueGen());
    }

    @Override
    @Transactional
    public synchronized void increaseTS(Integer id) {
        MultithreadSync multithreadSync = baseMapper.selectObjectById(id);
        Integer newValue = multithreadSync.getValueGen() + 1;
        baseMapper.updateValueById(id, newValue);
        times++;
        System.out.println("times: " + times);
    }

    @Override
    public synchronized void increaseS(Integer id) {
        MultithreadSync multithreadSync = baseMapper.selectObjectById(id);
        Integer newValue = multithreadSync.getValueGen() + 1;
        baseMapper.updateValueById(id, newValue);
        times++;
        System.out.println("times: " + times);
    }

    @Override
    @Transactional
    public synchronized void increaseMB(Integer id) {
        MultithreadSync multithreadSync = baseMapper.selectById(id);
        multithreadSync.setValueGen(multithreadSync.getValueGen() + 1);
        baseMapper.updateById(multithreadSync);
        times++;
        System.out.println("times: " + times);
    }

    @Override
    @Transactional
    public synchronized void increaseNoSelect(Integer id) {
        baseMapper.increaseValue(id);
        times++;
        System.out.println("times: " + times);
    }
}
