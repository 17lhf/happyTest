package com.basic.happytest.modules.enumsUtils;

import com.basic.happytest.modules.enumsUtils.enums.Sample2Enum;
import com.basic.happytest.modules.enumsUtils.enums.SampleEnum;
import org.junit.jupiter.api.Test;


/**
 * 枚举工具类测试
 * 建议阅读：https://blog.csdn.net/m0_61213696/article/details/130558823
 * @author : lhf
 */
class EnumUtilsTest {

    @Test
    void getEnumName() {
        System.out.println("SampleEnum.A name: " + SampleEnum.A.name());
        // 输出：SampleEnum.A name: A
    }

    @Test
    void getEnumOrdinal() {
        System.out.println("SampleEnum.A ordinal: " + SampleEnum.A.ordinal());
        System.out.println("SampleEnum.B ordinal: " + SampleEnum.B.ordinal());
        System.out.println("SampleEnum.C ordinal: " + SampleEnum.C.ordinal());
        // 输出：
        // SampleEnum.A ordinal: 0
        // SampleEnum.B ordinal: 1
        // SampleEnum.C ordinal: 2
    }

    @Test
    void tetIsEnumValue() throws Exception {
        System.out.println(EnumUtils.isValidEnumAttribute(Sample2Enum.class, "isAttributeEquals2", "a"));
        System.out.println(EnumUtils.isValidEnumAttribute(Sample2Enum.class, x -> x.isAttributeEquals2("a")));
        System.out.println(EnumUtils.isValidEnumAttribute(Sample2Enum.class, x -> x.isAttributeEquals2("b")));
        System.out.println(EnumUtils.isValidEnumAttribute(Sample2Enum.class, x -> x.isAttributeEquals3("b")));
    }
}