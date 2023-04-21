package com.basic.happytest.modules.cryptology;

import com.basic.happytest.modules.cryptology.enums.KeyAlgorithmEnum;
import com.basic.happytest.modules.cryptology.enums.KeyLengthEnums;
import com.basic.happytest.modules.cryptology.enums.ProviderEnums;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

/**
 * 对称密钥相关工具
 * @author lhf
 */

public class SymmetricUtils {

    // 解决报错：no such provider: BC
    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成对称密钥，使用Sun库（此方法不适用与2DES密钥生成）
     * @param algorithm 算法
     * @param keySize 密钥长度
     * @return 密钥
     * @throws Exception 异常
     */
    public static Key generateKey(String algorithm, int keySize) throws Exception {
        return generateKey(algorithm, keySize, ProviderEnums.SUN.getProvider());
    }

    /**
     * 生成对称密钥
     * @param algorithm 算法
     * @param keySize 密钥长度
     * @param provider 算法提供者(SUN时不适用于生成2DES, BC时可以，但是128长的DES不能用SUN库进行加解密操作)
     * @return 密钥
     * @throws Exception 异常
     */
    public static Key generateKey(String algorithm, int keySize, String provider) throws Exception {
        System.out.println("---------------begin generate sysmmetric key---------------");
        // 创建一个密钥生成器对象
        KeyGenerator keyGen = KeyGenerator.getInstance(algorithm, provider);
        // 创建一个安全随机数对象
        SecureRandom random = new SecureRandom();
        // 初始化密钥生成器对象
        keyGen.init(keySize, random);
        // 生成一个密钥对象
        SecretKey secretKey = keyGen.generateKey();
        // 打印密钥信息
        printSecretKeyMessage(secretKey, algorithm);
        System.out.println("---------------end generate sysmmetric key---------------");
        // 返回密钥对象
        return secretKey;
    }

    /**
     * 打印密钥的信息
     * @param key 密钥
     * @param algorithm 密钥的算法
     */
    public static void printSecretKeyMessage(Key key, String algorithm) {
        // 将密钥对象转换为SecretKeySpec类型
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getEncoded(), algorithm);
        System.out.println("Key algorithm: " + secretKeySpec.getAlgorithm());
        System.out.println("Key(Hex): " + Hex.toHexString(secretKeySpec.getEncoded()));
        System.out.println("Key format: " + secretKeySpec.getFormat());
        System.out.println("Key length: " + secretKeySpec.getEncoded().length * 8 + " bits");
    }

    /**
     * 获得标准的算法定义上的2DES的密钥明文数据 <br/>
     * Java中的标准实现(SUN库)不直接支持双倍长度的三重DES密钥，所以生成的2DES密钥实际是通过将密钥的第一部分重复为第三部分来获得：k1 || k2 || k1
     * @param key 由JAVA标准（SUN库）实现生成的2DES密钥对象
     * @return 标准的算法定义上的2DES的密钥明文数据（16进制形式）
     */
    public static String get2DESStandardKey(Key key) {
        printSecretKeyMessage(key, KeyAlgorithmEnum.TDES.getAlgorithm());
        /**
         * Java中的标准实现不直接支持双倍长度的三重DES密钥，但是您可以通过将密钥的第一部分重复为第三部分来获得相同的效果：k1 || k2 || k1
         */
        byte[] bytes = Arrays.copyOf(key.getEncoded(), KeyLengthEnums.TDES_ORIGINAL_128.getLength() / 8);
        String standardKey = Hex.toHexString(bytes);
        System.out.println("2DES standard key(Hex): " + standardKey);
        return standardKey;
    }
}
