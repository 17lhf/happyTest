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
public class Box<T> { // 泛型才能用，且上界类型不能有冲突的方法，此时需要同时满足两个上界，多重上界通配符 Box<T extends Number & Comparable<T>>

    private List<T> list;

    // 无界通配符（Unbounded Wildcard） = private List<? extends Object> list2;
    private List<?> list2;

    // extends 上界通配符（Upper Bounded Wildcard）存在一个最上级的界限，即指定一个最高级别的父类，它表示对于该上界类型以及其子类都适用
    // 泛型也能用
    private List<? extends Number> list3;

    // super 下界通配符 （Lower Bounded Wildcard）存在一个最低级的界限，即指定一个最低级别的子类，它表示对于该下界类型以及其父类都适用。
    // 泛型不支持
    private List<? super Integer> list4;

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