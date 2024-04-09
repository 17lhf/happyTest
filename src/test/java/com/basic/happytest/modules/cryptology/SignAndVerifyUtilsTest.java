package com.basic.happytest.modules.cryptology;

import com.basic.happytest.modules.cryptology.enums.DigestTypeEnum;
import com.basic.happytest.modules.cryptology.enums.EncryptAlgorithmEnums;
import com.basic.happytest.modules.cryptology.enums.KeyAlgorithmEnum;
import com.basic.happytest.modules.cryptology.enums.SignAlgorithmEnum;
import com.basic.happytest.modules.fileIO.FileIO;
import com.sun.crypto.provider.SunJCE;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;

class SignAndVerifyUtilsTest {

    // <editor-fold desc="一些会被用到的文件路径">
    // RSA私钥示例1
    static String RSA_PRV_KEY_PKCS1_NO_ENCRYPT = "/static/cryptologyFiles/rsaPrivateKey1.key";
    // RSA公钥示例1
    static String RSA_PUB_KEY = "/static/cryptologyFiles/rsaPublicKey1.key";
    static String ECC_PRV_KEY_PKCS8_NO_ENCRYPT = "/static/cryptologyFiles/ecPkcs8PrivateKey1.key";
    // ECC公钥示例1
    static String ECC_PUB_KEY = "/static/cryptologyFiles/ecPublicKey1.key";
    // </editor-fold>

    @Test
    void signData() throws Exception {
        String data = "a data to be signed";
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);

        // RSA
        PrivateKey privateKey = AsymmetricUtils.loadRSAPKCS1PrivateKey(FileIO.getResourceAbsolutePath(RSA_PRV_KEY_PKCS1_NO_ENCRYPT));
        System.out.println();
        byte[] res1 = SignAndVerifyUtils.signData(privateKey, dataBytes, SignAlgorithmEnum.SHA256_WITH_RSA.getAlgorithm());
        System.out.println();
        byte[] res2 = SignAndVerifyUtils.signData2(privateKey, KeyAlgorithmEnum.RSA.getAlgorithm(), dataBytes, DigestTypeEnum.SHA256.getDigestType());
        System.out.println(Hex.toHexString(res1).equals(Hex.toHexString(res2)));
        PublicKey publicKey = AsymmetricUtils.loadPublicKey(FileIO.getResourceAbsolutePath(RSA_PUB_KEY));
        System.out.println();
        String sunJCEName = new SunJCE().getName();
        EncAndDecUtils.decryptData(publicKey, EncryptAlgorithmEnums.RSA.getAlgorithm(), res1, sunJCEName);
        System.out.println();
        EncAndDecUtils.decryptData(publicKey, EncryptAlgorithmEnums.RSA.getAlgorithm(), res2, sunJCEName);
        System.out.println();
        EncAndDecUtils.decryptData(publicKey, EncryptAlgorithmEnums.RSA_ECB_PKCS1PADDING.getAlgorithm(), res1, sunJCEName);
        System.out.println();
        EncAndDecUtils.decryptData(publicKey, EncryptAlgorithmEnums.RSA_ECB_PKCS1PADDING.getAlgorithm(), res2, sunJCEName);

        // ECC
        PrivateKey ecPrvKey = AsymmetricUtils.loadPrivateKey(FileIO.getResourceAbsolutePath(ECC_PRV_KEY_PKCS8_NO_ENCRYPT));
        System.out.println();
        SignAndVerifyUtils.signData(ecPrvKey, dataBytes, SignAlgorithmEnum.SHA256_WITH_ECDSA.getAlgorithm());
    }

    @Test
    void validSignature() throws Exception {
        String data = "a data to be signed";
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        // RSA
        PrivateKey privateKey = AsymmetricUtils.loadRSAPKCS1PrivateKey(FileIO.getResourceAbsolutePath(RSA_PRV_KEY_PKCS1_NO_ENCRYPT));
        System.out.println();
        byte[] res1 = SignAndVerifyUtils.signData(privateKey, dataBytes, SignAlgorithmEnum.SHA256_WITH_RSA.getAlgorithm());
        PublicKey publicKey = AsymmetricUtils.loadPublicKey(FileIO.getResourceAbsolutePath(RSA_PUB_KEY));
        System.out.println();
        SignAndVerifyUtils.validSignature(publicKey, KeyAlgorithmEnum.RSA.getAlgorithm(), dataBytes, DigestTypeEnum.SHA256.getDigestType(), res1);
        // ECC
        PrivateKey ecPrvKey = AsymmetricUtils.loadPrivateKey(FileIO.getResourceAbsolutePath(ECC_PRV_KEY_PKCS8_NO_ENCRYPT));
        System.out.println();
        byte[] res11 = SignAndVerifyUtils.signData(ecPrvKey, dataBytes, SignAlgorithmEnum.SHA256_WITH_ECDSA.getAlgorithm());
        PublicKey ecPubKey = AsymmetricUtils.loadPublicKey(FileIO.getResourceAbsolutePath(ECC_PUB_KEY));
        SignAndVerifyUtils.validSignature(ecPubKey, KeyAlgorithmEnum.ECDSA.getAlgorithm(), dataBytes, DigestTypeEnum.SHA256.getDigestType(), res11);
    }
}