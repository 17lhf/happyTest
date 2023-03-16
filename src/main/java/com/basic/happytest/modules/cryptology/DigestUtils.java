package com.basic.happytest.modules.cryptology;

import org.bouncycastle.util.encoders.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 摘要相关工具
 * @author lhf
 */

public class DigestUtils {

    /**
     * 生成摘要
     * @param data 等待获取摘要的数据
     * @param alo 选择的算法， 可选：MD5、SHA-1(或SHA1)、SHA-256
     * @return 摘要值
     * @throws NoSuchAlgorithmException 异常
     */
    public static byte[] digestData(byte[] data, String alo) throws NoSuchAlgorithmException {
        System.out.println("---------------begin digest data---------------");
        MessageDigest digest = MessageDigest.getInstance(alo);
        // 更新要计算的内容
        digest.update(data);
        // 完成哈希计算，得到摘要
        byte[] digestValue = digest.digest();
        System.out.println("Original message(Hex): " + Hex.toHexString(data) + ", Digest Value(HEX): "
                + Hex.toHexString(digestValue));
        System.out.println("---------------end digest data---------------");
        return digestValue;
    }
}
