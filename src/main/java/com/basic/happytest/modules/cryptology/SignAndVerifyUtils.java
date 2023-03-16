package com.basic.happytest.modules.cryptology;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.security.*;

/**
 * 签名验签相关工具
 * @author lhf
 */

public class SignAndVerifyUtils {

    // 解决报错：no such provider: BC
    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 利用密钥对数据进行签名
     * @param prvKey 私钥（签名用的都是私钥）
     * @param data 待签名数据
     * @param signAlgorithm 签名过程中要用到的算法
     * @return 签名值
     * @throws Exception 异常
     */
    public static byte[] signData(PrivateKey prvKey, byte[] data, String signAlgorithm) throws Exception {
        System.out.println("---------------begin sign data---------------");
        // java原生对ECC不支持，使用BC库是为了支持EC，如果不用ECC，那么可以不用设置provider
        Signature signature = Signature.getInstance(signAlgorithm, BouncyCastleProvider.PROVIDER_NAME);
        signature.initSign(prvKey);
        signature.update(data);
        byte[] signatureValue = signature.sign();
        System.out.println("Source data is(Hex-String): " + Hex.toHexString(data));
        System.out.println("Signature algorithm is: " + signAlgorithm);
        System.out.println("Signature data is(Hex-String): " + Hex.toHexString(signatureValue));
        System.out.println("---------------end sign data---------------");
        return signatureValue;
    }

    /**
     * 利用密钥对数据进行签名(分理论步骤进行，慎用！！！)
     * @param prvKey 私钥（签名用的都是私钥）
     * @param keyAlo 密钥的算法, 支持：RSA,DSA
     * @param data 待签名数据
     * @param hashType 签名过程中要用到的摘要算法, 支持：SHA-1(或SHA1),SHA-256(或SHA256) (较常用的是SHA-256)
     * @return 签名值
     * @throws Exception 异常
     */
    public static byte[] signData2(PrivateKey prvKey, String keyAlo, byte[] data, String hashType) throws Exception {
        System.out.println("---------------begin sign data(way two)---------------");
        byte[] hashData = DigestUtils.digestData(data, hashType);
        // 这个头很神奇，其实本质是一个ASN1编码的数据，而且值会因为摘要算法变，这里是一个经验值（ todo 不是特别确定，或许可以通过相关的标准文档来确认）
        // todo 在JCE的实现底层RSACipher里，似乎有一个专门用于签名的Cipher模式，不知道是否可以利用起来以可以自动填充这个未知具体含义的字段
        String header;
        if("SHA-1".equals(hashType) || "SHA1".equals(hashType)){
            header = "3021300906052b0e03021a05000414";
        } else if("SHA-256".equals(hashType) || "SHA256".equals(hashType)){
            header = "3031300d060960864801650304020105000420";
        } else {
            throw new NoSuchAlgorithmException();
        }
        String waitForEncryptData = header + Hex.toHexString(hashData);
        byte[] signatureValue;
        if("RSA".equals(keyAlo)) {
            // 对于RSA密钥，一般签名内部使用的是 RSA/ECB/PKCS1Padding（signature内部就是这么设置，单元测试中可见）
            signatureValue = EncAndDecUtils.encryptData(prvKey, keyAlo + "/ECB/PKCS1Padding", Hex.decode(waitForEncryptData),
                    BouncyCastleProvider.PROVIDER_NAME);
        } else{
            throw new NoSuchAlgorithmException();
        }
        System.out.println("Source data is(Hex-String): " + Hex.toHexString(data));
        System.out.println("Signature algorithm is: " + hashType + "with" + keyAlo);
        System.out.println("Signature data is(Hex-String): " + Hex.toHexString(signatureValue));
        System.out.println("---------------end sign data(way two)----------------");
        return signatureValue;
    }

    /**
     * 利用密钥对数据进行验签
     * @param pubKey 公钥（验签用的都是公钥）
     * @param keyAlo 密钥算法, 支持：RSA,DSA,ECDSA、ECNR
     * @param data 原始数据
     * @param hashType 签名过程中要用到的摘要算法, 支持：SHA1,SHA256 (较常用的是SHA256)
     * @param signatureValue 签名值
     * @return true-正确，false-错误
     * @throws Exception 异常
     */
    public static boolean validSignature(PublicKey pubKey, String keyAlo, byte[] data, String hashType,
                                         byte[] signatureValue) throws Exception {
        System.out.println("---------------begin valid signature---------------");
        String signAlo = hashType + "with" + keyAlo;
        // java原生对ECC不支持，使用BC库是为了支持EC，如果不用ECC，那么可以不用设置provider
        Signature signature = Signature.getInstance(signAlo, BouncyCastleProvider.PROVIDER_NAME);
        signature.initVerify(pubKey);
        signature.update(data);
        boolean isOK = signature.verify(signatureValue);
        System.out.println("Signature valid result is: " + isOK);
        System.out.println("---------------end valid signature---------------");
        return isOK;
    }
}
