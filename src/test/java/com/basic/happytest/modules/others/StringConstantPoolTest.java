package com.basic.happytest.modules.others;

import org.junit.jupiter.api.Test;

/**
 * 字符串常量池（string pool）测试
 * stack overflow: https://stackoverflow.com/questions/78342153/java-string-constant-pool-confused?noredirect=1#comment138116214_78342153
 * 注：不必深究，因为多数情况需要考虑Java的版本因素，所以只要不使用 == 来比较就好。
 * @author : lhf
 */

public class StringConstantPoolTest {

    /**
     * 类里面的字符常量，不会随着类的解析加载一起加载到字符串常量池，而是使用时再加载进去
     */
    @Test
    public void test1() {
        String s = "abc";
        String s1 = new String("abc");
        String s2 = s1.intern();
        String s3 = new String(new char[]{'a','b','c'});
        String s4 = "a" + "bc";
        String s5 = new String("abc");

        System.out.println("s == s1 ? " + (s == s1)); // false
        System.out.println("s == s2 ? " + (s == s2)); // true
        System.out.println("s == s3 ? " + (s == s3)); // false
        System.out.println("s == s4 ? " + (s == s4)); // true
        System.out.println("s1 == s2 ? " + (s1 == s2)); // false
        System.out.println("s4 == s5 ? " + (s4 == s5)); // false

        String ss = new String("def");
        String ss1 = ss.intern();
        System.out.println("ss == ss1 ? " + (ss == ss1)); // false

        String sa = new String("lmn");
        String sa1 = new String("opq");
        StringBuilder stringBuilder = new StringBuilder().append(sa).append(sa1);
        String sa2 = stringBuilder.toString();
        String sa3 = sa2.intern(); // 运行到这里时，“lmnopq” 会被加载进字符串常量池
        String sa4 = "lmnopq";    // 直接使用字符串常量池中的地址
        System.out.println("sa2 == sa3 ? " + (sa2 == sa3)); // true?
        System.out.println("sa3 == sa4 ? " + (sa3 == sa4)); // true

        String sss = new String("ghi") + new String("hjk"); // 字节码里实际上转变成了 StringBuilder来拼接字符串(依赖Java版本的处理方式)
        String sss1 = sss.intern();
        System.out.println("sss == sss1 ? " + (sss == sss1)); // true?

        String sc = new String("rszt") + new String("zuvw");
        String sc1 = "rsztzuvw";
        String sc2 = sc.intern();
        System.out.println("sc == sc1 ? " + (sc == sc1)); // false?
        System.out.println("sc == sc2 ? " + (sc == sc2)); // false?
        System.out.println("sc1 == sc2 ? " + (sc1 == sc2)); // true
    }
}
