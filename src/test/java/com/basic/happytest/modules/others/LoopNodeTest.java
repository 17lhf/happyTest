package com.basic.happytest.modules.others;

import org.junit.jupiter.api.Test;

/**
 * 带标签的循环（Labeled loop）的测试 <br />
 * 在 java.io.BufferedReader.readLine() 里也有这种写法 <br />
 * 非嵌套循环场景，其实不太需要这种写法
 * @author : lhf
 */

public class LoopNodeTest {

    @Test
    public void test() {
        loop1: // 设置标记
        for (int i = 0; i < 5; i++) {
            loop2: // 设置标记
            for (int j = 0; j <= i; j++) {
                if (i < 2) {
                    break; // 不使用标记（unlabeled break）: 常规的跳出循环，是跳出最内层的一个循环
                }
                if (j == 3){
                    break loop1; // 使用标记（labeled break），指定跳出循环 loop1
                }
                if (i < 3) {
                    continue loop1; // 使用标记（labeled continue），指定继续执行循环 loop1
                }
                System.out.println("i = " + i + ", j = " + j);
            }
        }
        loop3: // 设置标记
        do {
        } while (false);

        loop4: // 设置标记
        while (true) {
            break loop4;
        }
    }
}
