package com.basic.happytest.modules.cryptology;

import org.bouncycastle.util.encoders.Hex;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;

/**
 * 对称密钥相关工具
 * @author lhf
 */

public class SysmmetricUtils {

    /**
     * 生成对称密钥
     * @param algorithm 算法
     * @param keySize 密钥长度
     * @return 密钥
     * @throws Exception 异常
     */
    public static Key generateKey(String algorithm, int keySize) throws Exception {
        System.out.println("---------------begin generate sysmmetric key---------------");
        // 创建一个密钥生成器对象
        KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
        // 创建一个安全随机数对象
        SecureRandom random = new SecureRandom();
        // 初始化密钥生成器对象
        keyGen.init(keySize, random);
        // 生成一个密钥对象
        SecretKey secretKey = keyGen.generateKey();
        // 将密钥对象转换为SecretKeySpec类型
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), algorithm);
        System.out.println("Key algorithm: " + secretKeySpec.getAlgorithm());
        System.out.println("Key(Hex): " + Hex.toHexString(secretKeySpec.getEncoded()));
        System.out.println("Key format: " + secretKeySpec.getFormat());
        System.out.println("Key length: " + secretKeySpec.getEncoded().length * 8 + " bits");
        System.out.println("---------------end generate sysmmetric key---------------");
        // 返回密钥对象
        return secretKey;
    }
}
