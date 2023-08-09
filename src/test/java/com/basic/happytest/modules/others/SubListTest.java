package com.basic.happytest.modules.others;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 一些寻常的不好归类的测试
 * @author lhf
 */

public class SubListTest {

    /**
     * 关于List.subList的坑
     */
    @Test
    public void subListTest() {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            list.add(i);
        }
        System.out.println("原始list: " + list);
        // 这里注意看subList的方法介绍，官方说明很详细
        // 另外，subList方法实际上获取的不过是List的一个内部类（SubList）实例
        // 内部类SubList构造方法里其实把List(也就是父类)作为属性之一进行了记录
        // 所以调用subList这个子列表的方法，其实很多时候都是在调用父类，也就是原来的list的方法
        List<Integer> subList = list.subList(0, 3);
        System.out.println("原始SubList: " + subList);

        Integer removeNum = subList.remove(2);
        System.out.println("被移除的元素：" + removeNum);

        System.out.println("移除一个元素后的SubList: " + subList);
        System.out.println("移除一个元素后的list: " + list);
        // 此时你会发现，对subList进行操作后，不仅subList里的元素发生了变化，而且list里的元素也发生了变化
        // 说明subList并不是拷贝出来的，而更像是一个“对list的截取一部分内容的引用”（官方描述为“原list的一个视图”）
    }

    /**
     * 不想让subList对原list有所关联的解决方案：不直接使用subList的返回值，而是用其用于初始化构造（或者用addAll来赋值也一样）
     */
    @Test
    public void solution() {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            list.add(i);
        }
        System.out.println("原始list: " + list);
        List<Integer> subList = new ArrayList<>(list.subList(0, 3));
        System.out.println("原始SubList: " + subList);

        Integer removeNum = subList.remove(2);
        System.out.println("被移除的元素：" + removeNum);

        System.out.println("移除一个元素后的SubList: " + subList);
        System.out.println("移除一个元素后的list: " + list);
    }
}
