package com.basic.happytest.modules.multithreadSync.service;

import com.basic.happytest.modules.multithreadSync.entity.MultithreadSync;

public interface MultithreadSyncService {

    /**
     * 以某一初始值，添加一个新项
     * @param initValue 初始值
     * @param desc 对新项的用途描述
     */
    void add(Integer initValue, String desc);

    /**
     * 对id = id的数据进行value的+1递增测试
     * @param id 项的id
     */
    void increaseValue(Integer id);

    /**
     * 获取id对应的项
     * @param id id
     */
    MultithreadSync selectObjectById(Integer id);

    /**
     * 更新对应的信息
     * @param multithreadSync
     */
    void updateValueById(MultithreadSync multithreadSync);

    /**
     * value递增操作（先查，修改，再更新） 同时用@Transactional和Synchronization
     * @param id id
     */
    void increaseTS(Integer id);

    /**
     * value递增操作（先查，修改，再更新）仅用Synchronization
     * @param id id
     */
    void increaseS(Integer id);

    /**
     * value递增操作（先查，修改，再更新） 同时用@Transactional和Synchronization和mybatisPlus
     * @param id id
     */
    void increaseMB(Integer id);

    /**
     * value递增操作（直接更新） 同时用@Transactional和Synchronization
     * @param id id
     */
    void increaseNoSelect(Integer id);
}
