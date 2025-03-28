package com.basic.happytest.modules.stream;

/**
 * 对象转map的接口
 * @author : lhf
 */

public interface Obj2MapInterface<T, V> {

    /**
     * 获取key
     * @return Map的key
     */
    T obtainKey();

    /**
     * 获取value
     * @return Map的value
     */
    V obtainValue();

}
