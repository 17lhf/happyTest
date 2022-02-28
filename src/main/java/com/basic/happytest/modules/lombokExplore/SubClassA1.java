package com.basic.happytest.modules.lombokExplore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

// 不加的话toString只会打印子类属性，忽略父类属性
@ToString(callSuper = true)
// 不设置为true的话，子类对象属性值一致，但其继承的父类对象属性值不一致，在比较的时候会出现比较结果不对的情况。
// 它默认使用非静态，非瞬态的属性
// 可通过参数exclude排除一些属性
// 可通过参数of指定仅使用哪些属性
// 它默认仅使用该类中定义的属性且不调用父类的方法
@EqualsAndHashCode(callSuper = false)
@Data
public class SubClassA1 extends ClassA{

    Double d;

    public SubClassA1(){
        super();
        d = 2.0;
    }
}