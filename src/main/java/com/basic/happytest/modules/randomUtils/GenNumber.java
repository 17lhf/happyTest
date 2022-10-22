package com.basic.happytest.modules.randomUtils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 生成指定范围内的随机数 <br/>
 * 此类只进行理论上的推敲验证，无法实质性验证
 * @author lhf
 */

public class GenNumber {

    /**
     * 生成指定范围的随机double数字
     * @param min 最小值
     * @param minReach 最小值是否可以取到（true-可以）
     * @param max 最大值
     * @param maxReach 最大值是否可以取到（true-可以）
     * @return 符合要求的随机数
     */
    public static double genDoubleNumber(double min, boolean minReach, double max, boolean maxReach) {
        double num;
        if(minReach) {
            // [min, max]
            if(maxReach) {
                // todo 虽然涉及循环，但是目前没有找到不降低取值密度的写法
                // 使用取余数之类带整型的写法，基本都会降低取值密度
                do {
                    // 该公式的取值范围是：[min, max + 0.001)
                    num = ThreadLocalRandom.current().nextDouble(min, max + 0.001);
                } while(num > max); // 排除掉取值大于max的情况，剩下的就是符合要求的值了
            } else { // [min, max)
                // num = min + Math.random() * (max - min);
                num = ThreadLocalRandom.current().nextDouble(min, max);
            }
        } else {
            // (min, max]
            if(maxReach) {
                num = max - Math.random() * (max - min);
            } else { // (min, max)
                // (max - 1 - Math.random() * (max - min - 1)) ∈ (min, max - 1]
                // Math.random() ∈ [0, 1)
                // 两者相加，可知最终结果取值范围就是 (min, max)
                // 注意，不要尝试使用乘法结合律
                num = Math.random() + (max - 1 - Math.random() * (max - min - 1));
            }
        }
        return num;
    }

    /**
     * 获取指定范围内的整型随机数
     * @param min 最小值
     * @param minReach 是否取到最小值
     * @param max 最大值
     * @param maxReach 是否取到最大值
     * @return 符合要求的随机整型数字
     */
    public static int getIntegerNumber(Integer min, boolean minReach, Integer max, boolean maxReach) {
        int num;
        Random rand = new Random();
        if (minReach) {
            // [min, max]
            if (maxReach) {
                // num = min + rand.nextInt(max - min + 1);
                num = ThreadLocalRandom.current().nextInt(min, max + 1);
            } else { // [min, max)
                // num = min + rand.nextInt(max - min);
                num = ThreadLocalRandom.current().nextInt(min, max);
            }
        } else {
            // (min, max]
            if (maxReach) {
                num = max - rand.nextInt(max - min);
            } else { // (min, max) = [min + 1, max)
                num = min + 1 + rand.nextInt(max - min - 1);
            }
        }
        return num;
    }
}
