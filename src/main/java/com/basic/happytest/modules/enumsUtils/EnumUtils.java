package com.basic.happytest.modules.enumsUtils;

import java.lang.reflect.Method;
import java.util.function.Predicate;

/**
 * 枚举相关工具类
 * @author : lhf
 */

public class EnumUtils {

    /**
     * Judge whether the given parameter is an attribute of an enum value in the given enum class.
     * @param enumClass Target enum class
     * @param methodName the name of the method to be called
     * @param values the parameters need to be checked
     * @return true if the parameter is an attribute of an enum value in the given enum class, false otherwise
     */
    public static <T> boolean isValidEnumAttribute(Class<T> enumClass, String methodName, Object... values) throws Exception {
        T[] enums = enumClass.getEnumConstants();
        Class<?>[] params = new Class[values.length];
        for (int i = 0; i < values.length; i++) {
            params[i] = values[i].getClass();
        }
        Method method = enumClass.getMethod(methodName, params);
        boolean isEnum = false;
        for (T enumValue : enums) {
            isEnum = (boolean) method.invoke(enumValue, values);
            if (isEnum) break;
        }
        return isEnum;
    }

    /**
     * 检查给定的参数是否为指定枚举类的某个枚举值的属性。（推荐）
     * @param enumClass 枚举类的 Class 对象
     * @param methodReference 方法引用
     * @return true 如果参数是枚举值的属性值，则返回 true；否则返回 false。
     * @param <T> 枚举类型
     */
    public static <T extends Enum<?>> boolean isValidEnumAttribute(Class<T> enumClass, Predicate<T> methodReference) {
        T[] enums = enumClass.getEnumConstants();
        boolean isEnum = false;
        for (T enumValue : enums) {
            isEnum = methodReference.test(enumValue);
            if (isEnum) break;
        }
        return isEnum;
    }
}
