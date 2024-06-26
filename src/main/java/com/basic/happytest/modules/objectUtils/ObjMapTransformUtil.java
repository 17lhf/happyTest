package com.basic.happytest.modules.objectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 对象和map键值对的转换工具类
 * @author lhf
 */

public class ObjMapTransformUtil {

    /**
     * 对象转Map键值对（通过反射的方式）
     * @param obj 具体的对象
     * @return Map键值对
     * @throws Exception 异常
     */
    public static<T> Map<String, Object> obj2Map(final T obj) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (obj != null) {
            Class<?> clazz = obj.getClass();
            // 一直循环取值，直到没有父类
            while (clazz != null) {
                for (Field field : clazz.getDeclaredFields()) {
                    // 如果 accessible 标志被设置为true，那么反射对象在使用的时候，不会去检查Java语言权限控制（private之类的）；
                    // 因为一般情况下，我们并不能对类的私有字段进行操作，利用反射也不例外
                    // 如果设置为false(默认值)，反射对象在使用的时候，会检查Java语言权限控制。
                    // 需要注意的是，设置为true会引起安全隐患
                    // 另外，执行安全检查会有性能代价
                    field.setAccessible(true);
                    // 可以通过field获取很多信息
                    System.out.println("field[" + field.getName() + "] modifier :" + Modifier.toString(field.getModifiers())); // 获取对应属性域的声明
                    // 这里并不是调用get方法获取值
                    map.put(field.getName(), field.get(obj));
                }
                // 切换到父类
                clazz = clazz.getSuperclass();
            }
        }
        return map;
    }

    /**
     * Map键值对转对象（通过反射的方式）
     * @param map map键值对
     * @param tClass 目标对象的类
     * @return 对象
     * @param <T> 类模板
     * @throws Exception 异常
     */
    public static<T> T map2Obj(final Map<String, Object> map, Class<T> tClass) throws Exception {
        // 调用无参构造方法构造实例对象
        T obj = tClass.newInstance();
        Class<?> clazz = obj.getClass();
        // 一直循环取值，直到没有父类
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                int mod = field.getModifiers();
                // 排除掉静态变量和final变量，因为这些是不能修改的
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                // 如果 accessible 标志被设置为true，那么反射对象在使用的时候，不会去检查Java语言权限控制（private之类的）；
                // 因为一般情况下，我们并不能对类的私有字段进行操作，利用反射也不例外
                // 如果设置为false(默认值)，反射对象在使用的时候，会检查Java语言权限控制。
                // 需要注意的是，设置为true会引起安全隐患
                // 另外，执行安全检查会有性能代价
                field.setAccessible(true);
                // 有可能map里没有对应的类的属性值可以用于赋值
                if (map.containsKey(field.getName())) {
                    // 这里的赋值不是调用set方法
                    System.out.println("map2Obj, field name: " + field.getName());
                    field.set(obj, map.get(field.getName()));
                }
            }
            // 切换到父类
            clazz = clazz.getSuperclass();
        }
        return obj;
    }
}
