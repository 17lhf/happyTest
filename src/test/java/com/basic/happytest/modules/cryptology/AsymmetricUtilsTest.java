package com.basic.happytest.modules.cryptology;

import com.basic.happytest.modules.cryptology.entity.CsrInfos;
import com.basic.happytest.modules.cryptology.enums.*;
import com.basic.happytest.modules.fileIO.FileIO;
import com.sun.crypto.provider.SunJCE;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;
import sun.security.x509.X509CertImpl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;


/**
 * 密码学相关内容的测试
 * @author lhf
 */

class AsymmetricUtilsTest {
    // <editor-fold desc="一些会被用到的文件路径">
    // 存储生成的文件的路径
    static String STORE_PATH = "static/cryptologyFiles/storeFiles/";
    // RSA私钥示例1
    static String RSA_PRV_KEY_PKCS1_NO_ENCRYPT = "static/cryptologyFiles/rsaPrivateKey1.key";
    static String RSA_PRV_KEY_PKCS8_NO_ENCRYPT = "static/cryptologyFiles/pkcs8RsaPrivateKey1.key";
    // RSA公钥示例1
    static String RSA_PUB_KEY = "static/cryptologyFiles/rsaPublicKey1.key";
    // RSA私钥示例2
    static String RSA_PRV_KEY_PKCS1_ENCRYPT = "static/cryptologyFiles/rsaProtectedByPwd.key";
    static String RSA_PRV_KEY_PKCS8_ENCRYPT = "static/cryptologyFiles/pkcs8RsaProtectedByPwd.key";
    static String RSA_PRV_KEY_PKCS8_ENCRYPT_PWD = "123456";
    // RSA CSR示例
    static String RSA_CSR_PEM = "static/cryptologyFiles/rsa1.csr";
    static String RSA_CSR_DER = "static/cryptologyFiles/rsa1_der.csr";
    // RSA CERT示例
    static String RSA_CERT_PEM = "static/cryptologyFiles/rsa1.crt";
    // ECC私钥示例1
    static String ECC_PRV_KEY_PKCS1_NO_ENCRYPT = "static/cryptologyFiles/ecPrivateKey1.key";
    static String ECC_PRV_KEY_PKCS1_ENCRYPT = "static/cryptologyFiles/ecProtectedPrvKey1.key";
    static String ECC_PRV_KEY_PKCS1_ENCRYPT_PWD = "123456";
    static String ECC_PRV_KEY_PKCS8_ENCRYPT = "static/cryptologyFiles/ecPkcs8ProtectedPrivateKey1.key";
    static String ECC_PRV_KEY_PKCS8_ENCRYPT_PWD = "123456";
    static String ECC_PRV_KEY_PKCS8_NO_ENCRYPT = "static/cryptologyFiles/ecPkcs8PrivateKey1.key";
    // ECC公钥示例1
    static String ECC_PUB_KEY = "static/cryptologyFiles/ecPublicKey1.key";
    // CA证书示例
    static String CA_CERT_PEM = "static/cryptologyFiles/ca.crt";
    static String CA_CERT_DER = "static/cryptologyFiles/ca.der";
    // CA私钥示例
    static String CA_KEY = "static/cryptologyFiles/ca.key";
    // 比较特殊的一对密钥，其私钥加载后是PEMKeyPair对象。由“OpenSSL 1.1.1h  22 Sep 2020”生成
    static String PEM_KEY_PAIR_PRV_KEY = "static/cryptologyFiles/keypair_pri_1.key";
    static String PEM_KEY_PAIR_PUB_KEY = "static/cryptologyFiles/keypair_pub_1.key";
    // </editor-fold>

    @Test
    void testGetAbsoluteFolderPath() throws IOException {
        FileIO.getAbsolutePath(STORE_PATH);
    }

    @Test
    void generateKeyPair() throws Exception {
        AsymmetricUtils.generateKeyPair(KeyAlgorithmEnum.RSA.getAlgorithm(), KeyLengthEnums.RSA_2048.getLength());
        // AsymmetricUtils.generateKeyPair(KeyAlgorithmEnum.RSA.getAlgorithm(), KeyLengthEnums.RSA_1024.getLength());
        // AsymmetricUtils.generateKeyPair(KeyAlgorithmEnum.DH.getAlgorithm(), KeyLengthEnums.DH_1024.getLength());
        // AsymmetricUtils.generateKeyPair(KeyAlgorithmEnum.DSA.getAlgorithm(), KeyLengthEnums.DSA_1024.getLength());
    }

    @Test
    void getPubKeyFromCert() throws Exception {
        AsymmetricUtils.getPubKeyFromCert(FileIO.getAbsolutePath(CA_CERT_PEM));
        AsymmetricUtils.getPubKeyFromCert(FileIO.getAbsolutePath(CA_CERT_DER));
    }

    @Test
    void generateP10CertRequest() throws Exception {
        CsrInfos csrInfos = new CsrInfos();
        csrInfos.setCountry("CN");
        csrInfos.setState("FuJian");
        csrInfos.setLocal("FuZhou");
        csrInfos.setOrganization("Organization");
        csrInfos.setOrganizationUnit("Organization Unit");
        csrInfos.setCommonName("lhf");
        csrInfos.setEmailAddress("lhf@qq.com");
        KeyPair rsaKeyPair = AsymmetricUtils.generateKeyPair(KeyAlgorithmEnum.RSA.getAlgorithm(), KeyLengthEnums.RSA_2048.getLength());
        AsymmetricUtils.generateP10CertRequest(KeyAlgorithmEnum.RSA.getAlgorithm(), rsaKeyPair, csrInfos);
        KeyPair eccKeyPair = AsymmetricUtils.generateECCKeyPair(KeyLengthEnums.EC_256.getLength());
        AsymmetricUtils.generateP10CertRequest(KeyAlgorithmEnum.EC.getAlgorithm(), eccKeyPair, csrInfos);
    }

    @Test
    void generateECCKeyPair() throws Exception {
        AsymmetricUtils.generateECCKeyPair(KeyLengthEnums.EC_256.getLength());
    }

    @Test
    void loadRSAPrivateKey() throws Exception {
        AsymmetricUtils.loadRSAPKCS1PrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS1_NO_ENCRYPT));
    }

    @Test
    void issueCert() throws Exception {
        String issuerCertPath = FileIO.getAbsolutePath(CA_CERT_PEM);
        String issuerKeyPath = FileIO.getAbsolutePath(CA_KEY);
        CsrInfos csrInfos = new CsrInfos();
        csrInfos.setCountry("CN");
        csrInfos.setState("FuJian");
        csrInfos.setLocal("FuZhou");
        csrInfos.setOrganization("Organization");
        csrInfos.setOrganizationUnit("Organization Unit");
        csrInfos.setCommonName("lhf");
        csrInfos.setEmailAddress("lhf@qq.com");
        KeyPair rsaKeyPair = AsymmetricUtils.generateKeyPair(KeyAlgorithmEnum.RSA.getAlgorithm(), KeyLengthEnums.RSA_2048.getLength());
        PKCS10CertificationRequest csr = AsymmetricUtils.generateP10CertRequest(KeyAlgorithmEnum.RSA.getAlgorithm(), rsaKeyPair, csrInfos);
        AsymmetricUtils.issueCert(csr, issuerCertPath, issuerKeyPath, 3650);
    }

    @Test
    void loadPKCS8EncryptedPrivateKey() throws Exception {
        AsymmetricUtils.loadPKCS8EncryptedPrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS8_ENCRYPT), RSA_PRV_KEY_PKCS8_ENCRYPT_PWD);
        AsymmetricUtils.loadPKCS8EncryptedPrivateKey(FileIO.getAbsolutePath(ECC_PRV_KEY_PKCS8_ENCRYPT), ECC_PRV_KEY_PKCS8_ENCRYPT_PWD);
    }

    @Test
    void loadPrivateKey() throws Exception {
         AsymmetricUtils.loadPrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS8_NO_ENCRYPT));
         AsymmetricUtils.loadPrivateKey(FileIO.getAbsolutePath(ECC_PRV_KEY_PKCS8_NO_ENCRYPT));
         AsymmetricUtils.loadPrivateKey(FileIO.getAbsolutePath(PEM_KEY_PAIR_PRV_KEY));
         AsymmetricUtils.loadPrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS1_NO_ENCRYPT));
    }

    @Test
    void loadPublicKey() throws Exception {
        AsymmetricUtils.loadPublicKey(FileIO.getAbsolutePath(RSA_PUB_KEY));
        AsymmetricUtils.loadPublicKey(FileIO.getAbsolutePath(ECC_PUB_KEY));
    }

    @Test
    void loadPublicKey2() throws Exception {
        AsymmetricUtils.loadPublicKey2(FileIO.getAbsolutePath(RSA_PUB_KEY), KeyAlgorithmEnum.RSA.getAlgorithm());
        AsymmetricUtils.loadPublicKey2(FileIO.getAbsolutePath(ECC_PUB_KEY), KeyAlgorithmEnum.EC.getAlgorithm());
    }

    @Test
    void loadCertFromFile() throws Exception {
        AsymmetricUtils.loadCertFromFile(FileIO.getAbsolutePath(CA_CERT_PEM), EncodeTypeEnum.PEM.getType());
        AsymmetricUtils.loadCertFromFile(FileIO.getAbsolutePath(CA_CERT_DER), EncodeTypeEnum.DER.getType());
    }

    @Test
    void getCertMsg() throws Exception {
        AsymmetricUtils.getCertMsg(AsymmetricUtils.loadCertFromFile(FileIO.getAbsolutePath(RSA_CERT_PEM), EncodeTypeEnum.PEM.getType()));
    }

    @Test
    void getPubKeyFromCsr() throws Exception {
        PKCS10CertificationRequest csr = AsymmetricUtils.loadCsrFromFile(FileIO.getAbsolutePath(RSA_CSR_PEM), EncodeTypeEnum.PEM.getType());
        AsymmetricUtils.getPubKeyFromCsr(csr, KeyAlgorithmEnum.RSA.getAlgorithm());

        CsrInfos csrInfos = new CsrInfos();
        csrInfos.setCountry("CN");
        csrInfos.setState("FuJian");
        csrInfos.setLocal("FuZhou");
        csrInfos.setOrganization("Organization");
        csrInfos.setOrganizationUnit("Organization Unit");
        csrInfos.setCommonName("lhf");
        csrInfos.setEmailAddress("lhf@qq.com");
        KeyPair eccKeyPair = AsymmetricUtils.generateECCKeyPair(KeyLengthEnums.EC_256.getLength());
        AsymmetricUtils.getPubKeyFromCsr(AsymmetricUtils.generateP10CertRequest(KeyAlgorithmEnum.EC.getAlgorithm(),
                eccKeyPair, csrInfos), KeyAlgorithmEnum.EC.getAlgorithm());
    }

    @Test
    void testLoadCsrFromFile() throws Exception {
        AsymmetricUtils.loadCsrFromFile(FileIO.getAbsolutePath(RSA_CSR_PEM), EncodeTypeEnum.PEM.getType());
        AsymmetricUtils.loadCsrFromFile(FileIO.getAbsolutePath(RSA_CSR_DER), EncodeTypeEnum.DER.getType());
    }

    @Test
    void getCsrMsg() throws Exception {
        CsrInfos csrInfos = new CsrInfos();
        csrInfos.setCountry("CN");
        csrInfos.setState("FuJian");
        csrInfos.setLocal("FuZhou");
        csrInfos.setOrganization("Organization");
        csrInfos.setOrganizationUnit("Organization Unit");
        csrInfos.setCommonName("lhf");
        csrInfos.setEmailAddress("lhf@qq.com");
        KeyPair rsaKeyPair = AsymmetricUtils.generateKeyPair(KeyAlgorithmEnum.RSA.getAlgorithm(), KeyLengthEnums.RSA_2048.getLength());
        PKCS10CertificationRequest csr = AsymmetricUtils.generateP10CertRequest(KeyAlgorithmEnum.RSA.getAlgorithm(), rsaKeyPair, csrInfos);
        AsymmetricUtils.getCsrMsg(csr);
    }

    @Test
    void key2PemOutPut() throws Exception {
        AsymmetricUtils.key2PemOutPut(KeyAlgorithmEnum.RSA.getAlgorithm(), KeyLengthEnums.RSA_2048.getLength());
        // AsymmetricUtils.key2PemOutPut(KeyAlgorithmEnum.DSA.getAlgorithm(), KeyLengthEnums.DSA_1024.getLength());
        // AsymmetricUtils.key2PemOutPut(KeyAlgorithmEnum.DH.getAlgorithm(), KeyLengthEnums.DH_1024.getLength());
    }

    @Test
    void key2PemFile() throws Exception {
        AsymmetricUtils.key2PemFile(KeyAlgorithmEnum.RSA.getAlgorithm(), KeyLengthEnums.RSA_2048.getLength(),
                FileIO.getAbsolutePath(STORE_PATH) + "/");
    }

    @Test
    void der2pem() throws Exception {
        /*String pem = AsymmetricUtils.der2pem(Hex.toHexString(AsymmetricUtils.loadPublicKey(FileIO.getAbsolutePath(RSA_PUB_KEY)).getEncoded()), DataTypeEnum.PUB_EY.getType());
        System.out.println(StringUtils.compare(pem, FileIO.getFileContent(FileIO.getAbsolutePath(RSA_PUB_KEY))));*/
        /*String pem = AsymmetricUtils.der2pem(Hex.toHexString(AsymmetricUtils.loadPrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS8_NO_ENCRYPT)).getEncoded()), DataTypeEnum.PRV_KEY.getType());
        System.out.println(StringUtils.compare(pem, FileIO.getFileContent(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS8_NO_ENCRYPT))));*/
        /*String pem = AsymmetricUtils.der2pem(Hex.toHexString(
                AsymmetricUtils.loadCsrFromFile(FileIO.getAbsolutePath(RSA_CSR_PEM), EncodeTypeEnum.PEM.getType())
                        .getEncoded()), DataTypeEnum.CSR.getType());
        System.out.println(StringUtils.compare(pem, FileIO.getFileContent(FileIO.getAbsolutePath(RSA_CSR_PEM))));*/
        String pem = AsymmetricUtils.der2pem(Hex.toHexString(
                AsymmetricUtils.loadCertFromFile(
                        FileIO.getAbsolutePath(CA_CERT_PEM), EncodeTypeEnum.PEM.getType()).getEncoded()), DataTypeEnum.CERT.getType());
        System.out.println(StringUtils.compare(pem, FileIO.getFileContent(FileIO.getAbsolutePath(CA_CERT_PEM))));
    }

    @Test
    void cert2PemFile() throws Exception {
        AsymmetricUtils.cert2PemFile(AsymmetricUtils.loadCertFromFile(FileIO.getAbsolutePath(CA_CERT_PEM), EncodeTypeEnum.PEM.getType()),
                FileIO.getAbsolutePath(STORE_PATH) + "/");
    }

    @Test
    void genCertAnd2PemFile() throws Exception {
        CsrInfos csrInfos = new CsrInfos();
        csrInfos.setCountry("CN");
        csrInfos.setState("FuJian");
        csrInfos.setLocal("FuZhou");
        csrInfos.setOrganization("Organization");
        csrInfos.setOrganizationUnit("Organization Unit");
        csrInfos.setCommonName("GenAttachExtensionCert");
        csrInfos.setEmailAddress("lhf@qq.com");
        KeyPair rsaKeyPair = AsymmetricUtils.generateKeyPair(KeyAlgorithmEnum.RSA.getAlgorithm(), KeyLengthEnums.RSA_2048.getLength());
        PKCS10CertificationRequest csr = AsymmetricUtils.generateP10CertRequest(KeyAlgorithmEnum.RSA.getAlgorithm(), rsaKeyPair, csrInfos);
        String issuerCertPath = FileIO.getAbsolutePath(CA_CERT_PEM);
        String issuerKeyPath = FileIO.getAbsolutePath(CA_KEY);
        AsymmetricUtils.cert2PemFile(AsymmetricUtils.issueCert(csr, issuerCertPath, issuerKeyPath, 3650),
                FileIO.getAbsolutePath(STORE_PATH) + "/");
    }

    @Test
    void certPem2DerHexStr() throws Exception {
        String certInDER = AsymmetricUtils.certPem2DerHexStr(FileIO.getFileContent(FileIO.getAbsolutePath(CA_CERT_PEM)));
        X509Certificate x509Certificate = new X509CertImpl(Hex.decode(certInDER));
        System.out.println(x509Certificate.getSubjectDN().getName());
    }

    @Test
    void generateP12() throws Exception {
        X509Certificate rootCert = AsymmetricUtils.loadCertFromFile(FileIO.getAbsolutePath(CA_CERT_PEM), EncodeTypeEnum.PEM.getType());
        X509Certificate subCert = AsymmetricUtils.loadCertFromFile(FileIO.getAbsolutePath(RSA_CERT_PEM), EncodeTypeEnum.PEM.getType());
        PrivateKey privateKey = AsymmetricUtils.loadPrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS8_NO_ENCRYPT));
        AsymmetricUtils.generateP12(rootCert, "rootCert",
                subCert, "sub",
                privateKey, "654321", "123456", FileIO.getAbsolutePath(STORE_PATH) + "/");
    }

    @Test
    void validCertChain() throws Exception {
        X509Certificate subCert = AsymmetricUtils.loadCertFromFile(FileIO.getAbsolutePath(RSA_CERT_PEM), EncodeTypeEnum.PEM.getType());
        X509Certificate rootCert = AsymmetricUtils.loadCertFromFile(FileIO.getAbsolutePath(CA_CERT_PEM), EncodeTypeEnum.PEM.getType());
        AsymmetricUtils.validCertChain(subCert, rootCert);
        AsymmetricUtils.validCertChain(rootCert, subCert);
    }

    @Test
    void loadAndParseP12() throws Exception {
       AsymmetricUtils.loadAndParseP12(FileIO.getAbsolutePath(STORE_PATH) + "/genByJavaRSA1.p12",
               "123456", "sub", "654321", "sub", "rootCert");
    }

    @Test
    public void genP10() throws Exception{
        PublicKey publicKey = AsymmetricUtils.loadPublicKey(FileIO.getAbsolutePath(RSA_PUB_KEY));
        PrivateKey privateKey = AsymmetricUtils.loadRSAPKCS1PrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS1_NO_ENCRYPT));
        CsrInfos csrInfos = new CsrInfos();
        csrInfos.setCountry("CN");
        csrInfos.setState("FuJian");
        csrInfos.setLocal("FuZhou");
        csrInfos.setOrganization("Organization");
        csrInfos.setOrganizationUnit("Organization Unit");
        csrInfos.setCommonName("genP10");
        csrInfos.setEmailAddress("lhf@qq.com");
        byte[] csrInfoBytes = AsymmetricUtils.getP10CsrInfoToBeSign(csrInfos, publicKey);
        byte[] signature = SignAndVerifyUtils.signData(privateKey, csrInfoBytes, SignAlgorithmEnum.SHA256_WITH_RSA.getAlgorithm());
        PKCS10CertificationRequest csr = AsymmetricUtils.constructP10Csr(signature, csrInfoBytes);
        AsymmetricUtils.csr2PemFile(csr, FileIO.getAbsolutePath(STORE_PATH) + "/");
    }

    @Test
    void encryptP8KeyFromFileAnd2File() throws Exception {
        AsymmetricUtils.encryptP8KeyFromFileAnd2File(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS8_NO_ENCRYPT),
                "123456", FileIO.getAbsolutePath(STORE_PATH) + "/");
    }

    @Test
    void generateAttachExtensionsP10Csr() throws Exception {
        CsrInfos csrInfos = new CsrInfos();
        csrInfos.setCountry("CN");
        csrInfos.setState("FuJian");
        csrInfos.setLocal("FuZhou");
        csrInfos.setOrganization("Organization");
        csrInfos.setOrganizationUnit("Organization Unit");
        csrInfos.setCommonName("lhf");
        csrInfos.setEmailAddress("lhf@qq.com");
        KeyPair rsaKeyPair = AsymmetricUtils.generateKeyPair(KeyAlgorithmEnum.RSA.getAlgorithm(), KeyLengthEnums.RSA_2048.getLength());
        PKCS10CertificationRequest csr = AsymmetricUtils.generateAttachExtensionsP10Csr(KeyAlgorithmEnum.RSA.getAlgorithm(), rsaKeyPair, csrInfos);
        // Cryptology.csr2PemFile(csr, FileIO.getAbsolutePath(STORE_PATH) + "/");
        AsymmetricUtils.getCsrMsg(csr);
    }

    @Test
    void issueAttachExtensionsCert() throws Exception {
        String alo = KeyAlgorithmEnum.RSA.getAlgorithm();
        CsrInfos csrInfos = new CsrInfos();
        csrInfos.setCountry("CN");
        csrInfos.setState("FuJian");
        csrInfos.setLocal("FuZhou");
        csrInfos.setOrganization("Organization");
        csrInfos.setOrganizationUnit("Organization Unit");
        csrInfos.setCommonName("lhf");
        csrInfos.setEmailAddress("lhf@qq.com");
        KeyPair rsaKeyPair = AsymmetricUtils.generateKeyPair(alo, KeyLengthEnums.RSA_2048.getLength());
        // PKCS10CertificationRequest csr = Cryptology.generateAttachExtensionsP10Csr(alo, rsaKeyPair, csrInfos);
        PKCS10CertificationRequest csr = AsymmetricUtils.generateP10CertRequest(alo, rsaKeyPair, csrInfos);
        String issuerCertPath = FileIO.getAbsolutePath(CA_CERT_PEM);
        String issuerKeyPath = FileIO.getAbsolutePath(CA_KEY);
        X509Certificate x509Certificate = AsymmetricUtils.issueAttachExtensionsCert(csr, issuerCertPath, issuerKeyPath,
                3650, false, 0, alo);
        // Cryptology.cert2PemFile(x509Certificate, FileIO.getAbsolutePath(STORE_PATH) + "/");
        AsymmetricUtils.getCertMsg(x509Certificate);
    }

    @Test
    void issueSelfSignV1Cert() throws Exception {
        String alo = KeyAlgorithmEnum.RSA.getAlgorithm();
        CsrInfos csrInfos = new CsrInfos();
        csrInfos.setCountry("CN");
        csrInfos.setState("FuJian");
        csrInfos.setLocal("FuZhou");
        csrInfos.setOrganization("Organization");
        csrInfos.setOrganizationUnit("Organization Unit");
        csrInfos.setCommonName("lhf");
        csrInfos.setEmailAddress("lhf@qq.com");
        KeyPair rsaKeyPair = AsymmetricUtils.generateKeyPair(alo, KeyLengthEnums.RSA_2048.getLength());
        PKCS10CertificationRequest csr = AsymmetricUtils.generateP10CertRequest(alo,rsaKeyPair, csrInfos);
        X509Certificate x509Certificate = AsymmetricUtils.issueSelfSignV1Cert(csr, rsaKeyPair.getPrivate(), 3650);
        AsymmetricUtils.cert2PemFile(x509Certificate, FileIO.getAbsolutePath(STORE_PATH) + "/");
        AsymmetricUtils.getCertMsg(x509Certificate);
    }

    @Test
    void loadRSAPubKeyByPriKeyTest() throws Exception {
        PrivateKey privateKey =
                AsymmetricUtils.loadPrivateKey(FileIO.getAbsolutePath(AsymmetricUtilsTest.RSA_PRV_KEY_PKCS8_NO_ENCRYPT));
        PublicKey publicKey = AsymmetricUtils.loadRSAPubKeyByPriKey(privateKey);
        String s = "abc";
        String sunJCEName = new SunJCE().getName();
        byte[] encData = EncAndDecUtils.encryptData(publicKey, KeyAlgorithmEnum.RSA.getAlgorithm(), s.getBytes(StandardCharsets.UTF_8), sunJCEName);
        byte[] decData = EncAndDecUtils.decryptData(privateKey, KeyAlgorithmEnum.RSA.getAlgorithm(), encData, sunJCEName);
        System.out.println("解密结果：" + new String(decData));
        System.out.println("解密结果是否与原文匹配：" + Arrays.equals(s.getBytes(StandardCharsets.UTF_8), decData));
    }

    @Test
    void prvKey2BCRSAPrvKey() throws Exception {
        KeyPair keyPair = AsymmetricUtils.generateKeyPair(KeyAlgorithmEnum.RSA.getAlgorithm(), KeyLengthEnums.RSA_2048.getLength());
        RSAPrivateKey rsaPrivateKey = AsymmetricUtils.rsaPrvKey2BCRSAPrvKey(keyPair.getPrivate());
        System.out.println(rsaPrivateKey.getModulus().toString(16));
    }

    @Test
    void validRSAKeyPairMatch() throws Exception {
        KeyPair keyPair = AsymmetricUtils.generateKeyPair(KeyAlgorithmEnum.RSA.getAlgorithm(), KeyLengthEnums.RSA_2048.getLength());
        System.out.println("KeyPair is match? " + AsymmetricUtils.validRSAKeyPairMatch(keyPair.getPublic(), keyPair.getPrivate()));
        PrivateKey privateKey = AsymmetricUtils.loadPrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS8_NO_ENCRYPT));
        System.out.println("KeyPair is match? " + AsymmetricUtils.validRSAKeyPairMatch(keyPair.getPublic(), privateKey));
    }

    @Test
    void loadRSAPubKeyOrPrvKeyByExponentAndModule() throws Exception {
        KeyPair keyPair = AsymmetricUtils.generateKeyPair(KeyAlgorithmEnum.RSA.getAlgorithm(), KeyLengthEnums.RSA_2048.getLength());
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        KeyFactory keyFactory = KeyFactory.getInstance(KeyAlgorithmEnum.RSA.getAlgorithm());
        RSAPrivateKeySpec prvKeySpec= keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
        RSAPublicKeySpec pubKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
        PublicKey publicKey1 = AsymmetricUtils.loadRSAPublicKeyByExponentAndModule(pubKeySpec.getPublicExponent(), pubKeySpec.getModulus());
        PrivateKey privateKey1 = AsymmetricUtils.loadRSAPrivateKeyByExponentAndModule(prvKeySpec.getPrivateExponent(), prvKeySpec.getModulus());
        if(Arrays.equals(publicKey1.getEncoded(), publicKey.getEncoded())) {
            System.out.println("public key matches");
        }
        if (Arrays.equals(privateKey.getEncoded(), privateKey1.getEncoded())) {
            System.out.println("private key matches");
        } else {
            System.out.println("Orignal Private Key: " + Hex.toHexString(privateKey.getEncoded()));
            System.out.println("New Private Key: " + Hex.toHexString(privateKey1.getEncoded()));
        }
        System.out.println("Algorithm match? " + AsymmetricUtils.validRSAKeyPairMatch(publicKey1, privateKey1));
        String message = "abcdefg";
        byte[] encryptData = EncAndDecUtils.encryptData(publicKey1, EncryptAlgorithmEnums.RSA.getAlgorithm(),
                message.getBytes(StandardCharsets.UTF_8), ProviderEnums.BC.getProvider());
        byte[] decryptData = EncAndDecUtils.decryptData(privateKey1, EncryptAlgorithmEnums.RSA.getAlgorithm(),
                encryptData, ProviderEnums.BC.getProvider());
        System.out.println("Encrypt match? " + Arrays.equals(message.getBytes(StandardCharsets.UTF_8), decryptData));
        System.out.println("-----private key information-----");
        AsymmetricUtils.printRSAPrvKeyInformation(privateKey1);
    }

    @Test
    void generateRSAKeyPairSpecifiesPubKeyExp() throws Exception {
        KeyPair keyPair = AsymmetricUtils.generateKeyPair(KeyAlgorithmEnum.RSA.getAlgorithm(), KeyLengthEnums.RSA_2048.getLength());
        KeyFactory keyFactory = KeyFactory.getInstance(KeyAlgorithmEnum.RSA.getAlgorithm());
        RSAPublicKeySpec pubKeySpec = keyFactory.getKeySpec(keyPair.getPublic(), RSAPublicKeySpec.class);
        AsymmetricUtils.generateRSAKeyPairSpecifiesPubKeyExp(KeyLengthEnums.RSA_2048.getLength(), pubKeySpec.getPublicExponent());
    }

    @Test
    void p8KeyAndP1Key() throws Exception {
        // 获得PKCS8标准密钥对
        KeyPair keyPair = AsymmetricUtils.generateKeyPair(KeyAlgorithmEnum.RSA.getAlgorithm(), KeyLengthEnums.RSA_2048.getLength());
        PublicKey oriPubKey = keyPair.getPublic();
        PrivateKey oriPrvKey = keyPair.getPrivate();
        System.out.println("Original PKCS8 Public Key: " + Hex.toHexString(oriPubKey.getEncoded()));
        System.out.println("Original PKCS8 Private Key: " + Hex.toHexString(oriPrvKey.getEncoded()));
        // 获得PKCS1标准公钥
        byte[] p1PubKeyBytes = AsymmetricUtils.p8PubKey2P1PubKeyBytes(keyPair.getPublic());
        System.out.println("PKCS1 Public Key: " + Hex.toHexString(p1PubKeyBytes));
        if (Arrays.equals(oriPubKey.getEncoded(), p1PubKeyBytes)) {
            System.out.println("PKCS1 Public Key == Original PKCS8 Public Key");
        } else {
            System.out.println("PKCS1 Public Key != Original PKCS8 Public Key");
        }
        // 获得PKCS1标准私钥
        byte[] p1PrvKeyBytes = AsymmetricUtils.p8PrvKey2P1PrvKeyBytes(keyPair.getPrivate());
        System.out.println("PKCS1 Private Key: " + Hex.toHexString(p1PrvKeyBytes));
        byte[] p1PrvKeyBytes2 = AsymmetricUtils.rsaP8PrvKey2P1PrvKeyBytes2(keyPair.getPrivate());
        System.out.println("PKCS1 Private Key 2: " + Hex.toHexString(p1PrvKeyBytes2));
        if (Arrays.equals(p1PrvKeyBytes, p1PrvKeyBytes2)) {
            System.out.println("PKCS1 Private Key == PKCS1 Private Key 2");
        } else {
            System.out.println("PKCS1 Private Key != PKCS1 Private Key 2");
        }
        // PKCS1私钥转对象
        RSAPrivateKey rsaPrivateKey = AsymmetricUtils.rsaP1PrvKey2Obj(p1PrvKeyBytes);
        System.out.println("PKCS1 Private Key Object: " + rsaPrivateKey.getVersion());
        // PKCS1公钥转PKCS8对象
        PublicKey publicKey = AsymmetricUtils.rsaP1PuKey2P8PubKey(p1PubKeyBytes);
        System.out.println("New PKCS8 Public Key: " + Hex.toHexString(publicKey.getEncoded()));
        if (Arrays.equals(oriPubKey.getEncoded(), publicKey.getEncoded())) {
            System.out.println("New PKCS8 Public Key == Original PKCS8 Public Key");
        } else {
            System.out.println("New PKCS8 Public Key != Original PKCS8 Public Key");
        }
        PublicKey publicKey2 = AsymmetricUtils.rsaP1PuKey2P8PubKey2(p1PubKeyBytes);
        System.out.println("New PKCS8 Public Key 2: " + Hex.toHexString(publicKey2.getEncoded()));
        if (Arrays.equals(oriPubKey.getEncoded(), publicKey2.getEncoded())) {
            System.out.println("New PKCS8 Public Key 2 == Original PKCS8 Public Key");
        } else {
            System.out.println("New PKCS8 Public Key 2 != Original PKCS8 Public Key");
        }
    }

    @Test
    void printParseASN1RsaPubKey() throws Exception {
        // 2048: 30 8 2 0122 30 0d 06 09 2a864886f70d010101 0500 03 8 2 010f 00
        // 1024: 30 8 1 9f   30 0d 06 09 2a864886f70d010101 0500 03 8 1 8d 00


        // 获得PKCS8标准密钥对
        KeyPair keyPair = AsymmetricUtils.generateKeyPair(KeyAlgorithmEnum.RSA.getAlgorithm(), KeyLengthEnums.RSA_2048.getLength());
        PublicKey oriPubKey = keyPair.getPublic();
        byte[] p8PubKeyBytes = oriPubKey.getEncoded();
        // 获得PKCS1标准公钥
        byte[] p1PubKeyBytes = AsymmetricUtils.p8PubKey2P1PubKeyBytes(keyPair.getPublic());
        System.out.println(Hex.toHexString(p8PubKeyBytes));
        System.out.println(Hex.toHexString(p1PubKeyBytes));
        // 输出信息
        AsymmetricUtils.printParseASN1RsaPubKey(Hex.toHexString(p8PubKeyBytes), true);
        System.out.println();
        AsymmetricUtils.printParseASN1RsaPubKey(Hex.toHexString(p1PubKeyBytes), false);
    }
}