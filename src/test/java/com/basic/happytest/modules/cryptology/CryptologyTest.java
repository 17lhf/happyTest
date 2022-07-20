package com.basic.happytest.modules.cryptology;

import com.basic.happytest.modules.fileIO.FileIO;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;
import sun.security.x509.X509CertImpl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;


/**
 * 密码学相关内容的测试
 * @author lhf
 */

class CryptologyTest {
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
        Cryptology.generateKeyPair("RSA", 2048);
        // Cryptology.generateKeyPair("RSA", 1024);
        Cryptology.generateKeyPair("DH", 1024);
        Cryptology.generateKeyPair("DSA", 1024);
    }

    @Test
    void getPubKeyFromCert() throws Exception {
       Cryptology.getPubKeyFromCert(FileIO.getAbsolutePath(CA_CERT_PEM));
    }

    @Test
    void getDigestValue() throws NoSuchAlgorithmException {
        Cryptology.digestData("Message".getBytes(StandardCharsets.UTF_8), "MD5");
        Cryptology.digestData("Message".getBytes(StandardCharsets.UTF_8), "SHA-1");
        Cryptology.digestData("Message".getBytes(StandardCharsets.UTF_8), "SHA-256");
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
        KeyPair rsaKeyPair = Cryptology.generateKeyPair("RSA", 2048);
        Cryptology.generateP10CertRequest("RSA", rsaKeyPair, csrInfos);
        KeyPair eccKeyPair = Cryptology.generateECCKeyPair(256);
        Cryptology.generateP10CertRequest("EC", eccKeyPair, csrInfos);
    }

    @Test
    void generateECCKeyPair() throws Exception {
        Cryptology.generateECCKeyPair(256);
    }

    @Test
    void loadRSAPrivateKey() throws Exception {
        Cryptology.loadRSAPKCS1PrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS1_NO_ENCRYPT));
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
        KeyPair rsaKeyPair = Cryptology.generateKeyPair("RSA", 2048);
        PKCS10CertificationRequest csr = Cryptology.generateP10CertRequest("RSA", rsaKeyPair, csrInfos);
        Cryptology.issueCert(csr, issuerCertPath, issuerKeyPath, 3650);
    }

    @Test
    void loadPKCS8EncryptedPrivateKey() throws Exception {
        Cryptology.loadPKCS8EncryptedPrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS8_ENCRYPT), RSA_PRV_KEY_PKCS8_ENCRYPT_PWD);
        Cryptology.loadPKCS8EncryptedPrivateKey(FileIO.getAbsolutePath(ECC_PRV_KEY_PKCS8_ENCRYPT), ECC_PRV_KEY_PKCS8_ENCRYPT_PWD);
    }

    @Test
    void loadPrivateKey() throws Exception {
         Cryptology.loadPKCS8PrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS8_NO_ENCRYPT));
         Cryptology.loadPKCS8PrivateKey(FileIO.getAbsolutePath(ECC_PRV_KEY_PKCS8_NO_ENCRYPT));
         Cryptology.loadPKCS8PrivateKey(FileIO.getAbsolutePath(PEM_KEY_PAIR_PRV_KEY));
    }

    @Test
    void loadPublicKey() throws Exception {
        Cryptology.loadPublicKey(FileIO.getAbsolutePath(RSA_PUB_KEY));
        Cryptology.loadPublicKey(FileIO.getAbsolutePath(ECC_PUB_KEY));
    }

    @Test
    void loadPublicKey2() throws Exception {
        Cryptology.loadPublicKey2(FileIO.getAbsolutePath(RSA_PUB_KEY), "RSA");
        Cryptology.loadPublicKey2(FileIO.getAbsolutePath(ECC_PUB_KEY), "EC");
    }

    @Test
    void loadCertFromFile() throws Exception {
        Cryptology.loadCertFromFile(FileIO.getAbsolutePath(CA_CERT_PEM), "PEM");
        Cryptology.loadCertFromFile(FileIO.getAbsolutePath(CA_CERT_DER), "DER");
    }

    @Test
    void getCertMsg() throws Exception {
        Cryptology.getCertMsg(Cryptology.loadCertFromFile(FileIO.getAbsolutePath(RSA_CERT_PEM), "PEM"));
    }

    @Test
    void getPubKeyFromCsr() throws Exception {
        PKCS10CertificationRequest csr = Cryptology.loadCsrFromFile(FileIO.getAbsolutePath(RSA_CSR_PEM), "PEM");
        Cryptology.getPubKeyFromCsr(csr, "RSA");

        CsrInfos csrInfos = new CsrInfos();
        csrInfos.setCountry("CN");
        csrInfos.setState("FuJian");
        csrInfos.setLocal("FuZhou");
        csrInfos.setOrganization("Organization");
        csrInfos.setOrganizationUnit("Organization Unit");
        csrInfos.setCommonName("lhf");
        csrInfos.setEmailAddress("lhf@qq.com");
        KeyPair eccKeyPair = Cryptology.generateECCKeyPair(256);
        Cryptology.getPubKeyFromCsr(Cryptology.generateP10CertRequest("EC", eccKeyPair, csrInfos), "EC");
    }

    @Test
    void testLoadCsrFromFile() throws Exception {
        Cryptology.loadCsrFromFile(FileIO.getAbsolutePath(RSA_CSR_PEM), "PEM");
        Cryptology.loadCsrFromFile(FileIO.getAbsolutePath(RSA_CSR_DER), "DER");
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
        KeyPair rsaKeyPair = Cryptology.generateKeyPair("RSA", 2048);
        PKCS10CertificationRequest csr = Cryptology.generateP10CertRequest("RSA",rsaKeyPair, csrInfos);
        Cryptology.getCsrMsg(csr);
    }

    @Test
    void key2PemOutPut() throws Exception {
        Cryptology.key2PemOutPut("RSA", 2048);
        // Cryptology.key2PemOutPut("DSA", 1024);
        // Cryptology.key2PemOutPut("DH", 1024);
    }

    @Test
    void key2PemFile() throws Exception {
        Cryptology.key2PemFile("RSA", 2048, FileIO.getAbsolutePath(STORE_PATH) + "/");
    }

    @Test
    void der2pem() throws Exception {
        /*String pem = Cryptology.der2pem(Hex.toHexString(Cryptology.loadPublicKey(FileIO.getAbsolutePath(RSA_PUB_KEY)).getEncoded()), "PUB_KEY");
        System.out.println(StringUtils.compare(pem, FileIO.getFileContent(FileIO.getAbsolutePath(RSA_PUB_KEY))));*/
        String pem = Cryptology.der2pem(Hex.toHexString(Cryptology.loadPKCS8PrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS8_NO_ENCRYPT)).getEncoded()), "PRV_KEY");
        System.out.println(StringUtils.compare(pem, FileIO.getFileContent(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS8_NO_ENCRYPT))));
        /*String pem = Cryptology.der2pem(Hex.toHexString(Cryptology.loadCsrFromFile(FileIO.getAbsolutePath(RSA_CSR_PEM), "PEM").getEncoded()), "CSR");
        System.out.println(StringUtils.compare(pem, FileIO.getFileContent(FileIO.getAbsolutePath(RSA_CSR_PEM))));*/
        /*String pem = Cryptology.der2pem(Hex.toHexString(Cryptology.loadCertFromFile(FileIO.getAbsolutePath(), "PEM").getEncoded()), "CERT");
        System.out.println(StringUtils.compare(pem, FileIO.getFileContent(FileIO.getAbsolutePath())));*/
    }

    @Test
    void cert2PemFile() throws Exception {
        Cryptology.cert2PemFile(Cryptology.loadCertFromFile(FileIO.getAbsolutePath(CA_CERT_PEM), "PEM"),
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
        KeyPair rsaKeyPair = Cryptology.generateKeyPair("RSA", 2048);
        PKCS10CertificationRequest csr = Cryptology.generateP10CertRequest("RSA",rsaKeyPair, csrInfos);
        String issuerCertPath = FileIO.getAbsolutePath(CA_CERT_PEM);
        String issuerKeyPath = FileIO.getAbsolutePath(CA_KEY);
        Cryptology.cert2PemFile(Cryptology.issueCert(csr, issuerCertPath, issuerKeyPath, 3650),
                FileIO.getAbsolutePath(STORE_PATH) + "/");
    }

    @Test
    void encryptAndDecryptData() throws Exception {
        String s = "abc123,.中文";
        byte[] sBytes = s.getBytes(StandardCharsets.UTF_8);


        String ecAlo = "ECIES";
        int ecSize = 256;
        KeyPair ecKeyPair = Cryptology.generateECCKeyPair(ecSize);
        byte[] ecEncData = Cryptology.encryptData(ecKeyPair.getPublic(), ecAlo, sBytes);
        byte[] ecDecData = Cryptology.decryptData(ecKeyPair.getPrivate(), ecAlo, ecEncData);
        System.out.println(new String(ecDecData, StandardCharsets.UTF_8));

        String alo = "RSA";
        Integer size = 2048;
        KeyPair keyPair = Cryptology.generateKeyPair(alo, size);
        byte[] encData1 = Cryptology.encryptData(keyPair.getPrivate(), alo, sBytes);
        byte[] encData2 = Cryptology.encryptData(keyPair.getPrivate(), "RSA/ECB/PKCS1Padding", sBytes);
        byte[] encData3 = Cryptology.encryptData(keyPair.getPrivate(), "RSA/ECB/OAEPWithSHA-1AndMGF1Padding", sBytes);
        byte[] encData4 = Cryptology.encryptData(keyPair.getPrivate(), "RSA/ECB/OAEPWithSHA-256AndMGF1Padding", sBytes);
        byte[] encData5 = Cryptology.encryptData(keyPair.getPrivate(), "RSA/ECB/NoPadding", sBytes);
        byte[] decData1 = Cryptology.decryptData(keyPair.getPublic(), alo, encData1);
        byte[] decData2 = Cryptology.decryptData(keyPair.getPublic(), "RSA/ECB/PKCS1Padding", encData2);
        byte[] decData3 = Cryptology.decryptData(keyPair.getPublic(), "RSA/ECB/OAEPWithSHA-1AndMGF1Padding", encData3);
        byte[] decData4 = Cryptology.decryptData(keyPair.getPublic(), "RSA/ECB/OAEPWithSHA-256AndMGF1Padding", encData4);
        byte[] decData5 = Cryptology.decryptData(keyPair.getPublic(), "RSA/ECB/NoPadding", encData5);
        System.out.println(new String(decData1, StandardCharsets.UTF_8));
        System.out.println(new String(decData2, StandardCharsets.UTF_8));
        System.out.println(new String(decData3, StandardCharsets.UTF_8));
        System.out.println(new String(decData4, StandardCharsets.UTF_8));
        System.out.println(new String(decData5, StandardCharsets.UTF_8));
    }

    @Test
    void keyAloSupportedInBCLibrary() {
        Cryptology.keyAloSupportedInBCLibrary();
    }

    @Test
    void certPem2DerHexStr() throws Exception {
        String certInDER = Cryptology.certPem2DerHexStr(FileIO.getFileContent(FileIO.getAbsolutePath(CA_CERT_PEM)));
        X509Certificate x509Certificate = new X509CertImpl(Hex.decode(certInDER));
        System.out.println(x509Certificate.getSubjectDN().getName());
    }

    @Test
    void generateP12() throws Exception {
        X509Certificate rootCert = Cryptology.loadCertFromFile(FileIO.getAbsolutePath(CA_CERT_PEM), "PEM");
        X509Certificate subCert = Cryptology.loadCertFromFile(FileIO.getAbsolutePath(RSA_CERT_PEM), "PEM");
        PrivateKey privateKey = Cryptology.loadPKCS8PrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS8_NO_ENCRYPT));
        Cryptology.generateP12(rootCert, "rootCert",
                subCert, "sub",
                privateKey, "654321", "123456", FileIO.getAbsolutePath(STORE_PATH) + "/");
    }

    @Test
    void signData() throws Exception {
        String data = "a data to be signed";
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);

        // RSA
        PrivateKey privateKey = Cryptology.loadRSAPKCS1PrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS1_NO_ENCRYPT));
        System.out.println();
        byte[] res1 = Cryptology.signData(privateKey, "RSA", dataBytes, "SHA256");
        System.out.println();
        byte[] res2 = Cryptology.signData2(privateKey, "RSA", dataBytes, "SHA256");
        System.out.println(Hex.toHexString(res1).equals(Hex.toHexString(res2)));
        PublicKey publicKey = Cryptology.loadPublicKey(FileIO.getAbsolutePath(RSA_PUB_KEY));
        System.out.println();
        /*Cryptology.decryptData(publicKey, "RSA", res1);
        System.out.println();
        Cryptology.decryptData(publicKey, "RSA", res2);
        System.out.println();*/
        Cryptology.decryptData(publicKey, "RSA/ECB/PKCS1Padding", res1);
        System.out.println();
        Cryptology.decryptData(publicKey, "RSA/ECB/PKCS1Padding", res2);

        // ECC
        PrivateKey ecPrvKey = Cryptology.loadPKCS8PrivateKey(FileIO.getAbsolutePath(ECC_PRV_KEY_PKCS8_NO_ENCRYPT));
        System.out.println();
        Cryptology.signData(ecPrvKey, "ECDSA", dataBytes, "SHA256");
    }

    @Test
    void validSignature() throws Exception {
        String data = "a data to be signed";
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        // RSA
        PrivateKey privateKey = Cryptology.loadRSAPKCS1PrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS1_NO_ENCRYPT));
        System.out.println();
        byte[] res1 = Cryptology.signData(privateKey, "RSA", dataBytes, "SHA256");
        PublicKey publicKey = Cryptology.loadPublicKey(FileIO.getAbsolutePath(RSA_PUB_KEY));
        System.out.println();
        Cryptology.validSignature(publicKey, "RSA", dataBytes, "SHA256", res1);
        // ECC
        PrivateKey ecPrvKey = Cryptology.loadPKCS8PrivateKey(FileIO.getAbsolutePath(ECC_PRV_KEY_PKCS8_NO_ENCRYPT));
        System.out.println();
        byte[] res11 = Cryptology.signData(ecPrvKey, "ECDSA", dataBytes, "SHA256");
        PublicKey ecPubKey = Cryptology.loadPublicKey(FileIO.getAbsolutePath(ECC_PUB_KEY));
        Cryptology.validSignature(ecPubKey, "ECDSA", dataBytes, "SHA256", res11);
    }

    @Test
    void validCertChain() throws Exception {
        X509Certificate subCert = Cryptology.loadCertFromFile(FileIO.getAbsolutePath(RSA_CERT_PEM), "PEM");
        X509Certificate rootCert = Cryptology.loadCertFromFile(FileIO.getAbsolutePath(CA_CERT_PEM), "PEM");
        Cryptology.validCertChain(subCert, rootCert);
        Cryptology.validCertChain(rootCert, subCert);
    }

    @Test
    void loadAndParseP12() throws Exception {
       Cryptology.loadAndParseP12(FileIO.getAbsolutePath(STORE_PATH) + "/genByJavaRSA1.p12",
               "123456", "sub", "654321", "sub", "rootCert");
    }

    @Test
    public void genP10() throws Exception{
        PublicKey publicKey = Cryptology.loadPublicKey(FileIO.getAbsolutePath(RSA_PUB_KEY));
        PrivateKey privateKey = Cryptology.loadRSAPKCS1PrivateKey(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS1_NO_ENCRYPT));
        CsrInfos csrInfos = new CsrInfos();
        csrInfos.setCountry("CN");
        csrInfos.setState("FuJian");
        csrInfos.setLocal("FuZhou");
        csrInfos.setOrganization("Organization");
        csrInfos.setOrganizationUnit("Organization Unit");
        csrInfos.setCommonName("genP10");
        csrInfos.setEmailAddress("lhf@qq.com");
        byte[] csrInfoBytes = Cryptology.getP10CsrInfoToBeSign(csrInfos, publicKey);
        byte[] signature = Cryptology.signData(privateKey, "RSA", csrInfoBytes, "SHA256");
        PKCS10CertificationRequest csr = Cryptology.constructP10Csr(signature, csrInfoBytes);
        Cryptology.csr2PemFile(csr, FileIO.getAbsolutePath(STORE_PATH) + "/");
    }

    @Test
    void encryptP8KeyFromFileAnd2File() throws Exception {
        Cryptology.encryptP8KeyFromFileAnd2File(FileIO.getAbsolutePath(RSA_PRV_KEY_PKCS8_NO_ENCRYPT),
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
        KeyPair rsaKeyPair = Cryptology.generateKeyPair("RSA", 2048);
        PKCS10CertificationRequest csr = Cryptology.generateAttachExtensionsP10Csr("RSA",rsaKeyPair, csrInfos);
        // Cryptology.csr2PemFile(csr, FileIO.getAbsolutePath(STORE_PATH) + "/");
        Cryptology.getCsrMsg(csr);
    }

    @Test
    void issueAttachExtensionsCert() throws Exception {
        String alo = "RSA";
        CsrInfos csrInfos = new CsrInfos();
        csrInfos.setCountry("CN");
        csrInfos.setState("FuJian");
        csrInfos.setLocal("FuZhou");
        csrInfos.setOrganization("Organization");
        csrInfos.setOrganizationUnit("Organization Unit");
        csrInfos.setCommonName("lhf");
        csrInfos.setEmailAddress("lhf@qq.com");
        KeyPair rsaKeyPair = Cryptology.generateKeyPair("RSA", 2048);
        // PKCS10CertificationRequest csr = Cryptology.generateAttachExtensionsP10Csr(alo, rsaKeyPair, csrInfos);
        PKCS10CertificationRequest csr = Cryptology.generateP10CertRequest(alo, rsaKeyPair, csrInfos);
        String issuerCertPath = FileIO.getAbsolutePath(CA_CERT_PEM);
        String issuerKeyPath = FileIO.getAbsolutePath(CA_KEY);
        X509Certificate x509Certificate = Cryptology.issueAttachExtensionsCert(csr, issuerCertPath, issuerKeyPath,
                3650, false, 0, alo);
        // Cryptology.cert2PemFile(x509Certificate, FileIO.getAbsolutePath(STORE_PATH) + "/");
        Cryptology.getCertMsg(x509Certificate);
    }

    @Test
    void issueSelfSignV1Cert() throws Exception {
        String alo = "RSA";
        CsrInfos csrInfos = new CsrInfos();
        csrInfos.setCountry("CN");
        csrInfos.setState("FuJian");
        csrInfos.setLocal("FuZhou");
        csrInfos.setOrganization("Organization");
        csrInfos.setOrganizationUnit("Organization Unit");
        csrInfos.setCommonName("lhf");
        csrInfos.setEmailAddress("lhf@qq.com");
        KeyPair rsaKeyPair = Cryptology.generateKeyPair("RSA", 2048);
        PKCS10CertificationRequest csr = Cryptology.generateP10CertRequest(alo,rsaKeyPair, csrInfos);
        X509Certificate x509Certificate = Cryptology.issueSelfSignV1Cert(csr, rsaKeyPair.getPrivate(), 3650);
        Cryptology.cert2PemFile(x509Certificate, FileIO.getAbsolutePath(STORE_PATH) + "/");
        Cryptology.getCertMsg(x509Certificate);
    }

    @Test
    void loadRSAPubKeyByPriKeyTest() throws Exception {
        PrivateKey privateKey =
                Cryptology.loadPKCS8PrivateKey(FileIO.getAbsolutePath(CryptologyTest.RSA_PRV_KEY_PKCS8_NO_ENCRYPT));
        PublicKey publicKey = Cryptology.loadRSAPubKeyByPriKey(privateKey);
        String s = "abc";
        byte[] encData = Cryptology.encryptData(publicKey, "RSA", s.getBytes(StandardCharsets.UTF_8));
        byte[] decData = Cryptology.decryptData(privateKey, "RSA", encData);
        System.out.println("解密结果：" + new String(decData));
        System.out.println("解密结果是否与原文匹配：" + Arrays.equals(s.getBytes(StandardCharsets.UTF_8), decData));
    }

    /**
     * 测试分段加解密
     * @throws Exception 异常
     */
    @Test
    public void testBlockEncAndDec() throws Exception {
        // 获得待加密的数据
        PrivateKey privateKey =
                Cryptology.loadPKCS8PrivateKey(FileIO.getAbsolutePath(CryptologyTest.RSA_PRV_KEY_PKCS8_NO_ENCRYPT));
        byte[] data = privateKey.getEncoded();
        // 生成密钥对，用于加解密
        KeyPair keyPair = Cryptology.generateKeyPair("RSA", 1024);
        // 公钥加密
        byte[] encData = Cryptology.rsaBlockEncrypt(data, "RSA", keyPair.getPublic(), 117);
        // 私钥解密
        byte[] decData = Cryptology.rsaBlockDecrypt(encData, "RSA", keyPair.getPrivate(), 128);

        // 私钥加密
        /*byte[] encData = Cryptology.rsaBlockEncrypt(data, "RSA", keyPair.getPrivate(), 117);
        // 公钥解密
        byte[] decData = Cryptology.rsaBlockDecrypt(encData, "RSA", keyPair.getPublic(), 128);*/

        // 查看解密后的明文与原文是否匹配
        System.out.println("解密后的明文与原文是否匹配: " + Arrays.equals(data, decData));
    }
}