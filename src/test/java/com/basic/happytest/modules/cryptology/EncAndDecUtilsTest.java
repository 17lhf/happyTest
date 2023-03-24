package com.basic.happytest.modules.cryptology;

import com.basic.happytest.modules.cryptology.enums.EncryptAlgorithmEnums;
import com.basic.happytest.modules.cryptology.enums.KeyAlgorithmEnum;
import com.basic.happytest.modules.cryptology.enums.KeyLengthEnums;
import com.basic.happytest.modules.cryptology.enums.ProviderEnums;
import com.basic.happytest.modules.fileIO.FileIO;
import com.sun.crypto.provider.SunJCE;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;

class EncAndDecUtilsTest {

    static String RSA_PRV_KEY_PKCS8_NO_ENCRYPT = "static/cryptologyFiles/pkcs8RsaPrivateKey1.key";

    // 比较特殊的一对密钥，其私钥加载后是PEMKeyPair对象。由“OpenSSL 1.1.1h  22 Sep 2020”生成
    static String PEM_KEY_PAIR_PRV_KEY = "static/cryptologyFiles/keypair_pri_1.key";
    static String PEM_KEY_PAIR_PUB_KEY = "static/cryptologyFiles/keypair_pub_1.key";

    @Test
    void encryptAndDecryptData() throws Exception {
        String s = "abc123,.中文";
        byte[] sBytes = s.getBytes(StandardCharsets.UTF_8);

        String ecAlo = EncryptAlgorithmEnums.ECIES.getAlgorithm();
        int ecSize = KeyLengthEnums.EC_256.getLength();
        KeyPair ecKeyPair = AsymmetricUtils.generateECCKeyPair(ecSize);
        byte[] ecEncData = EncAndDecUtils.encryptData(ecKeyPair.getPublic(), ecAlo, sBytes);
        byte[] ecDecData = EncAndDecUtils.decryptData(ecKeyPair.getPrivate(), ecAlo, ecEncData);
        System.out.println(new String(ecDecData, StandardCharsets.UTF_8));

        String alo = KeyAlgorithmEnum.RSA.getAlgorithm();
        Integer size = KeyLengthEnums.RSA_2048.getLength();
        KeyPair keyPair = AsymmetricUtils.generateKeyPair(alo, size);
        byte[] encData1 = EncAndDecUtils.encryptData(keyPair.getPrivate(), alo, sBytes);
        byte[] encData2 = EncAndDecUtils.encryptData(keyPair.getPrivate(), EncryptAlgorithmEnums.RSA_ECB_PKCS1PADDING.getAlgorithm(), sBytes);
        byte[] encData3 = EncAndDecUtils.encryptData(keyPair.getPrivate(), EncryptAlgorithmEnums.RSA_NONE_OAEP_WITH_SHA1_AND_MGF1PADDING.getAlgorithm(), sBytes);
        byte[] encData4 = EncAndDecUtils.encryptData(keyPair.getPrivate(), EncryptAlgorithmEnums.RSA_NONE_OAEP_WITH_SHA256_AND_MGF1PADDING.getAlgorithm(), sBytes);
        byte[] encData5 = EncAndDecUtils.encryptData(keyPair.getPrivate(), EncryptAlgorithmEnums.RSA_ECB_NOPADDING.getAlgorithm(), sBytes);
        byte[] encData6 = EncAndDecUtils.encryptData(keyPair.getPrivate(), EncryptAlgorithmEnums.RSA_NONE_NOPADDING.getAlgorithm(), sBytes);

        byte[] decData1 = EncAndDecUtils.decryptData(keyPair.getPublic(), alo, encData1);
        byte[] decData2 = EncAndDecUtils.decryptData(keyPair.getPublic(), EncryptAlgorithmEnums.RSA_ECB_PKCS1PADDING.getAlgorithm(), encData2);
        byte[] decData3 = EncAndDecUtils.decryptData(keyPair.getPublic(), EncryptAlgorithmEnums.RSA_NONE_OAEP_WITH_SHA1_AND_MGF1PADDING.getAlgorithm(), encData3);
        byte[] decData4 = EncAndDecUtils.decryptData(keyPair.getPublic(), EncryptAlgorithmEnums.RSA_NONE_OAEP_WITH_SHA256_AND_MGF1PADDING.getAlgorithm(), encData4);
        byte[] decData5 = EncAndDecUtils.decryptData(keyPair.getPublic(), EncryptAlgorithmEnums.RSA_ECB_NOPADDING.getAlgorithm(), encData5);
        byte[] decData6 = EncAndDecUtils.decryptData(keyPair.getPublic(), EncryptAlgorithmEnums.RSA_NONE_NOPADDING.getAlgorithm(), encData6);

        System.out.println(new String(decData1, StandardCharsets.UTF_8));
        System.out.println(new String(decData2, StandardCharsets.UTF_8));
        System.out.println(new String(decData3, StandardCharsets.UTF_8));
        System.out.println(new String(decData4, StandardCharsets.UTF_8));
        System.out.println(new String(decData5, StandardCharsets.UTF_8));
        System.out.println(new String(decData6, StandardCharsets.UTF_8));
    }

    @Test
    void encryptAndDecryptData2() throws Exception {
        int length = 256;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = '1';
        }
        String alo = KeyAlgorithmEnum.RSA.getAlgorithm();
        Integer size = KeyLengthEnums.RSA_2048.getLength();
        String sunJCEName = new SunJCE().getName();
        KeyPair keyPair = AsymmetricUtils.generateKeyPair(alo, size);
        /*KeyFactory keyFactory = KeyFactory.getInstance(alo);
        RSAPublicKeySpec pubKeySpec = keyFactory.getKeySpec(keyPair.getPublic(), RSAPublicKeySpec.class);
        byte[] bytes = pubKeySpec.getModulus().toByteArray(); // 此时待加密数据就是密钥的模*/
        /*byte[] encData = EncAndDecUtils.encryptData(keyPair.getPublic(), EncryptAlgorithmEnums.RSA_ECB_PKCS1PADDING.getAlgorithm(), bytes, sunJCEName);
        byte[] decData = EncAndDecUtils.decryptData(keyPair.getPrivate(), EncryptAlgorithmEnums.RSA_ECB_PKCS1PADDING.getAlgorithm(), encData, sunJCEName);*/
        byte[] encData = EncAndDecUtils.encryptData(keyPair.getPublic(), alo, bytes, ProviderEnums.BC.getProvider());
        byte[] decData = EncAndDecUtils.decryptData(keyPair.getPrivate(), alo, encData, ProviderEnums.BC.getProvider());
        System.out.println(Arrays.equals(bytes, decData));
    }

    /**
     * 关于使用指定填充模式的加解密测试（OAEP）
     * @throws Exception 异常
     */
    @Test
    void oaepEncryptAndDecryptData2() throws Exception {
        String s = "abc123,.中文";
        byte[] sBytes = s.getBytes(StandardCharsets.UTF_8);

        Integer size = KeyLengthEnums.RSA_2048.getLength();
        String alo = KeyAlgorithmEnum.RSA.getAlgorithm();
        KeyPair keyPair = AsymmetricUtils.generateKeyPair(alo, size);

        String encAlo = EncryptAlgorithmEnums.RSA_ECB_OAEP_WITH_SHA1_AND_MGF1PADDING.getAlgorithm();
        OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA1", "MGF1", new MGF1ParameterSpec("SHA-1"), PSource.PSpecified.DEFAULT);

        // SUN JCE
        byte[] encData = EncAndDecUtils.encryptData(keyPair.getPublic(), encAlo, sBytes, ProviderEnums.SUN.getProvider());
        byte[] decData = EncAndDecUtils.decryptData(keyPair.getPrivate(), encAlo, encData, ProviderEnums.SUN.getProvider());
        System.out.println(new String(decData, StandardCharsets.UTF_8));
        // SUN JCE OAEPParameterSpec
        byte[] encData1 = EncAndDecUtils.encryptData(keyPair.getPublic(), encAlo, sBytes, ProviderEnums.SUN.getProvider(), oaepParams);
        byte[] decData1 = EncAndDecUtils.decryptData(keyPair.getPrivate(), encAlo, encData1, ProviderEnums.SUN.getProvider(), oaepParams);
        System.out.println(new String(decData1, StandardCharsets.UTF_8));
        // BC
        byte[] encData2 = EncAndDecUtils.encryptData(keyPair.getPublic(), encAlo, sBytes, ProviderEnums.BC.getProvider());
        byte[] decData2 = EncAndDecUtils.decryptData(keyPair.getPrivate(), encAlo, encData2, ProviderEnums.BC.getProvider());
        System.out.println(new String(decData2, StandardCharsets.UTF_8));
        // BC OAEPParameterSpec
        byte[] encData3 = EncAndDecUtils.encryptData(keyPair.getPublic(), encAlo, sBytes, ProviderEnums.BC.getProvider(), oaepParams);
        byte[] decData3 = EncAndDecUtils.decryptData(keyPair.getPrivate(), encAlo, encData3, ProviderEnums.BC.getProvider(), oaepParams);
        System.out.println(new String(decData3, StandardCharsets.UTF_8));
        // BC NONE OAEPParameterSpec
        String encAlo1 = EncryptAlgorithmEnums.RSA_NONE_OAEP_WITH_SHA1_AND_MGF1PADDING.getAlgorithm();
        byte[] encData4 = EncAndDecUtils.encryptData(keyPair.getPublic(), encAlo1, sBytes, ProviderEnums.BC.getProvider(), oaepParams);
        byte[] decData4 = EncAndDecUtils.decryptData(keyPair.getPrivate(), encAlo1, encData4, ProviderEnums.BC.getProvider(), oaepParams);
        System.out.println(new String(decData4, StandardCharsets.UTF_8));
    }

    /**
     * 测试分段加解密
     * @throws Exception 异常
     */
    @Test
    public void testBlockEncAndDec() throws Exception {
        // 获得待加密的数据
         /*PrivateKey privateKey =
                AsymmetricUtils.loadPrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS8_NO_ENCRYPT));
        byte[] data = privateKey.getEncoded();*/
        byte[] data = new byte[500];
        for (int i = 0; i < data.length; i++) {
            data[i] = '1';
        }
        String sunJCEName = new SunJCE().getName();
        // 生成密钥对，用于加解密
        KeyPair keyPair = AsymmetricUtils.generateKeyPair(KeyAlgorithmEnum.RSA.getAlgorithm(), KeyLengthEnums.RSA_2048.getLength());
        // 公钥加密
        byte[] encData = EncAndDecUtils.rsaBlockEncrypt(data, EncryptAlgorithmEnums.RSA_ECB_NOPADDING.getAlgorithm(), keyPair.getPublic(), 256,
                ProviderEnums.BC.getProvider());
        // 私钥解密
        byte[] decData = EncAndDecUtils.rsaBlockDecrypt(encData, EncryptAlgorithmEnums.RSA_ECB_NOPADDING.getAlgorithm(), keyPair.getPrivate(), 256,
                ProviderEnums.BC.getProvider());

        // 查看解密后的明文与原文是否匹配
        System.out.println("解密后的明文与原文是否匹配: " + Arrays.equals(data, decData));

        // 对特殊的密钥对来尝试分段加解密
        /*PrivateKey specPrvKey = AsymmetricUtils.loadPrivateKey(FileIO.getAbsolutePath(PEM_KEY_PAIR_PRV_KEY));
        PublicKey specPubKey = AsymmetricUtils.loadPublicKey(FileIO.getAbsolutePath(PEM_KEY_PAIR_PUB_KEY));*/
        // 公钥加密
        /*byte[] encData = EncAndDecUtils.rsaBlockEncrypt(data, KeyAlgorithmEnum.RSA.getAlgorithm(), specPubKey, 255, sunJCEName);
        // 私钥解密
        byte[] decData = EncAndDecUtils.rsaBlockDecrypt(encData, KeyAlgorithmEnum.RSA.getAlgorithm(), specPrvKey, 256, sunJCEName);*/

        // 私钥加密
        /*byte[] encData = EncAndDecUtils.rsaBlockEncrypt(data, EncryptAlgorithmEnums.RSA_NONE_PKCS1PADDING.getAlgorithm(), specPrvKey, 245, sunJCEName);
        // 公钥解密
        byte[] decData = EncAndDecUtils.rsaBlockDecrypt(encData, EncryptAlgorithmEnums.RSA_NONE_PKCS1PADDING.getAlgorithm(), specPubKey, 256, sunJCEName);*/

        // 查看解密后的明文与原文是否匹配
        // System.out.println("解密后的明文与原文是否匹配: " + Arrays.equals(data, decData));
    }

    /**
     * 使用BC库加解密（填充方式选择NoPadding），在待加密的值开头是至少有两个0时（十六进制文本），解密后会丢失最开始的两个0，目测是在内部处理时
     * 因为某种处理方式导致被删除掉了，于是解密结果与原文对不上。 <br/>
     * 神奇的是，这个特殊的内部处理，只在开头大于等于两个0（十六进制文本）的情况下出问题，如果是1个0或者开头压根就不是0的时候，就一切正常。
     * 其实，解决办法也很简单，就是改为加解密都使用PKCS1Padding就没问题了
     * @throws Exception 异常
     */
    @Test
    void testBCDescSpecialCondition() throws Exception {
        KeyPair keyPair = AsymmetricUtils.generateKeyPair(KeyAlgorithmEnum.RSA.getAlgorithm(), KeyLengthEnums.RSA_2048.getLength());

        String tbsInHex = "00873031f2fe44f860ba4545e0445bbea1e84bf79b4ca0d0e03e8536881d36ac";
        byte[] encData = EncAndDecUtils.encryptData(keyPair.getPrivate(), EncryptAlgorithmEnums.RSA_ECB_PKCS1PADDING.getAlgorithm(), Hex.decode(tbsInHex), BouncyCastleProvider.PROVIDER_NAME);
        byte[] decData = EncAndDecUtils.decryptData(keyPair.getPublic(), EncryptAlgorithmEnums.RSA_ECB_PKCS1PADDING.getAlgorithm(), encData, BouncyCastleProvider.PROVIDER_NAME);
        String decDataInHex = Hex.toHexString(decData);
        System.out.println("解密后：" + decDataInHex);
        if (tbsInHex.equals(decDataInHex)) {
            System.out.println("解密后的明文与原文匹配");
        } else {
            System.out.println("解密后的明文与原文不匹配");
        }
    }
}