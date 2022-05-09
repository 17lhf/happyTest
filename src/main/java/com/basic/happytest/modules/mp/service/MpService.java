package com.basic.happytest.modules.mp.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.basic.happytest.modules.mp.dao.MpDao;
import com.basic.happytest.modules.mp.entity.Mp;
import org.springframework.stereotype.Service;

/**
 * Mybatis Plus相关测试的服务层
 * @author lhf
 */

@Service
public class MpService extends ServiceImpl<MpDao, Mp> {
    /**
     * 添加项
     * @param mp 待添加项
     * @return 新项的主键id
     */
    public Integer add(Mp mp){
        baseMapper.insert(mp);
        return mp.getId();
    }

    /**
     * 利用MybatisPlus来更新项
     * @param mp 新的数据信息
     */
    public void updByMp(Mp mp){
        baseMapper.updateById(mp);
    }

    /**
     * 依据项的id获取数据项
     * @param id 指定的主键id
     * @return 数据项
     */
    public Mp getById(Integer id){
        return baseMapper.selectById(id);
    }
}
