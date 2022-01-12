package com.basic.happytest.modules.unboundedWildcardsAndGenerics;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 测试泛型和无界通配符的区别
 * @author lhf
 */

@Getter
@Setter
public class Box<T> {
    private List<T> list;
    private List<?> list2;

    Box(){
    }

    Box(List<T> list, List<?> list2){
        this.list = list;
        this.list2 = list2;
    }

    /**
     * 用box的list的值来设置list的值
     * @param box box对象
     */
    public void getSet(Box<?> box){
        // box.setList(box.getList()); // 直接这样操作会报错
        helper(box);
    }

    // helper()函数辅助getSet()方法存取元素
    public <V> void helper(Box<V> box){
        box.setList(box.getList());
    }
}