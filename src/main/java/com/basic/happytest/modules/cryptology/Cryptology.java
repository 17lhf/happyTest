package com.basic.happytest.modules.cryptology;

import com.basic.happytest.modules.fileIO.FileIO;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.operator.*;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;
import sun.security.x509.X509CertImpl;

import javax.crypto.Cipher;
import javax.crypto.spec.DHPrivateKeySpec;
import javax.security.auth.x500.X500Principal;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

/**
 * 密码学相关的java操作
 * (注意，部分内容会使用到BC库，特别是ECC算法相关，因为jdk不支持ECC)
 * (注意，java里面很多地方其实都是用的PKCS8标准来处理密钥，所以导入到处都会默认以PKCS8标准)
 * (注意，很多地方异常的处理没有做，请自行解决)
 * (注意，有些类在java安全库中有，在BC库中也有，要注意实际用的是哪一个)
 * (注意，部分方法用到了sun.*的包，可能会导致打包时报找不到的错误(原因请看README.md)（虽然本项目不知为何不会报错）)
 * @author lhf
 */

public class Cryptology {

    // 解决报错：no such provider: BC
    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取BC库支持的密钥算法
     */
    public static void keyAloSupportedInBCLibrary() {
        Provider provider = new org.bouncycastle.jce.provider.BouncyCastleProvider();
        for (Provider.Service service : provider.getServices()) {
            System.out.println(service.getType() + ": " + service.getAlgorithm());
        }
    }

    /**
     * 生成摘要
     * @param data 等待获取摘要的数据
     * @param alo 选择的算法， 可选：MD5、SHA-1(或SHA1)、SHA-256(或SHA256)
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
        System.out.println("Original message(Hex): " + Hex.toHexString(data) + ", Digest Value(HEX): " + Hex.toHexString(digestValue));
        System.out.println("---------------end digest data---------------");
        return digestValue;
    }

    /**
     * 生成密钥对(由于对DH和DSA不熟，所以参数未进行解释)
     * @param alo 生成密钥对使用的算法，可选算法：DiffieHellman(等同于：DH)、DSA、RSA
     * @param keySize RSA时推荐长度为：2048/1024/3096 DH时推荐为：1024 DSA时推荐长度为：1024
     * @return 密钥对
     * @throws NoSuchAlgorithmException 异常
     */
    public static KeyPair generateKeyPair(String alo, Integer keySize) throws Exception {
        System.out.println("---------------begin generate key pair---------------");
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(alo);
        keyPairGenerator.initialize(keySize); // 默认的长度可能会因为算法提供者或版本而发生改变，所以不推荐使用默认的长度
        System.out.println("Provider: " + keyPairGenerator.getProvider());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println("Public key algorithm: " + publicKey.getAlgorithm());
        System.out.println("Private key algorithm: " + privateKey.getAlgorithm());
        System.out.println("Key format = " + privateKey.getFormat()); // java中一直要求的是PKCS8格式的密钥

        KeyFactory keyFactory = KeyFactory.getInstance(alo);
        if(Objects.equals(alo, "RSA")) {
            RSAPrivateKeySpec keySpec= keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
            BigInteger modulus = keySpec.getModulus();
            // RSA密钥的长度实际上指的是公钥模的长度（以Bit为单位）
            int length = modulus.toString(2).length(); // 转换为二进制
            System.out.println("RSA key size: " + length);
        } else if (Objects.equals(alo, "DSA")){
            DSAPrivateKeySpec keySpec = keyFactory.getKeySpec(privateKey, DSAPrivateKeySpec.class);
            System.out.println("P(the private key) = " + keySpec.getP());
            System.out.println("X(the prime) = " + keySpec.getX());
            System.out.println("Q(the sub-prime) = " + keySpec.getQ());
            System.out.println("G(base) = " + keySpec.getG());
            int length = keySpec.getG().toString(2).length();
            System.out.println("DSA key size: " + length);
        } else {
            DHPrivateKeySpec keySpec = keyFactory.getKeySpec(privateKey, DHPrivateKeySpec.class);
            System.out.println("P = " + keySpec.getP());
            System.out.println("X = " + keySpec.getX());
            System.out.println("G = " + keySpec.getG());
            int length = keySpec.getP().toString(2).length();
            System.out.println("DH key size: " + length);
        }
        System.out.println("---------------end generate key pair---------------");
        System.out.println();
        return keyPair;
    }

    /**
     * 生成ECC密钥对
     * @param keySize 密钥长度, 推荐256
     * @return 密钥对
     * @throws NoSuchAlgorithmException 异常
     * @throws NoSuchProviderException 异常
     */
    public static KeyPair generateECCKeyPair(int keySize) throws Exception {
        System.out.println("---------------begin generateECCKeyPair---------------");
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);
        System.out.println("Provider: " + keyPairGenerator.getProvider());
        keyPairGenerator.initialize(keySize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println("Public key algorithm: " + publicKey.getAlgorithm());
        System.out.println("Private key algorithm: " + privateKey.getAlgorithm());

        ECPublicKey ecPublicKey = (ECPublicKey)publicKey;
        ECPoint ecPoint = ecPublicKey.getW();
        BigInteger x = ecPoint.getAffineX();
        BigInteger y = ecPoint.getAffineY();
        System.out.println("基点坐标为（" + x + "," + y + ")"); // 公共点坐标
        ECParameterSpec ecParameterSpec = ecPublicKey.getParams();
        System.out.println("辅因子： " + ecParameterSpec.getCofactor());
        EllipticCurve ellipticCurve = ecParameterSpec.getCurve();
        System.out.println("椭圆曲线参数： a=" + ellipticCurve.getA());
        System.out.println("椭圆曲线参数： b=" + ellipticCurve.getB());
        ECField ecField = ellipticCurve.getField();
        System.out.println("椭圆曲线参数： P=" + ecField.getFieldSize());

        ECPrivateKey ecPrivateKey = (ECPrivateKey)privateKey;
        System.out.println("私钥：k=" + ecPrivateKey.getS());
        System.out.println("---------------end generateECCKeyPair---------------");
        return keyPair;
    }

    /**
     * 读取RSA私钥文件(pkcs1标准格式的私钥, 且无口令保护, 参考测试用的文件)
     * （方法使用了sun.*包，可能会导致打包失败，请看readme.md文件描述）
     * @param pemFilePath 文件路径
     * @return RSA私钥
     * @throws GeneralSecurityException 异常
     * @throws IOException 异常
     */
    public static PrivateKey loadRSAPKCS1PrivateKey(String pemFilePath) throws Exception {
        System.out.println("---------------begin pemFileLoadPrivateKeyPkcs1Encoded---------------");
        // PKCS#1 format
        final String PEM_RSA_PRIVATE_START = "-----BEGIN RSA PRIVATE KEY-----";
        final String PEM_RSA_PRIVATE_END = "-----END RSA PRIVATE KEY-----";
        Path path = Paths.get(pemFilePath);
        String privateKeyPem = new String(Files.readAllBytes(path));
        // java库解析时，只要中间的内容，头尾不要，换行也不要
        privateKeyPem = privateKeyPem.replace(PEM_RSA_PRIVATE_START, "").replace(PEM_RSA_PRIVATE_END, "");
        privateKeyPem = privateKeyPem.replaceAll("\\s", "");
        // 开始通过流来读取
        DerInputStream derReader = new DerInputStream(Base64.getDecoder().decode(privateKeyPem));
        DerValue[] seq = derReader.getSequence(0);
        if (seq.length < 9) {
            throw new GeneralSecurityException("Could not parse a PKCS1 private key.");
        }
        // skip version seq[0];
        BigInteger modulus = seq[1].getBigInteger();
        BigInteger publicExp = seq[2].getBigInteger();
        BigInteger privateExp = seq[3].getBigInteger();
        BigInteger prime1 = seq[4].getBigInteger();
        BigInteger prime2 = seq[5].getBigInteger();
        BigInteger exp1 = seq[6].getBigInteger();
        BigInteger exp2 = seq[7].getBigInteger();
        BigInteger crtCoef = seq[8].getBigInteger();
        RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp, prime1, prime2, exp1, exp2, crtCoef);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = factory.generatePrivate(keySpec);
        System.out.println("Private key algorithm: " + privateKey.getAlgorithm());
        System.out.println("---------------end pemFileLoadPrivateKeyPkcs1Encoded---------------");
        return privateKey;
    }

    /**
     * 从文件加载受口令保护的私钥(pkcs8标准格式，参考测试用的文件)
     * @param filePath 私钥文件路径(支持ECC、RSA)
     * @param password 口令
     * @return 私钥
     * @throws Exception 异常
     */
    public static PrivateKey loadPKCS8EncryptedPrivateKey(String filePath, String password) throws Exception {
        System.out.println("---------------begin loadEncryptedPrivateKey---------------");
        PEMParser pemParser = new PEMParser(new FileReader(filePath));
        Object pem = pemParser.readObject();
        if (pem instanceof PKCS8EncryptedPrivateKeyInfo) {
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME);
            PKCS8EncryptedPrivateKeyInfo keyInfo = (PKCS8EncryptedPrivateKeyInfo) pem;
            InputDecryptorProvider pkcs8Prov = new JceOpenSSLPKCS8DecryptorProviderBuilder()
                    .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                    .build(password.toCharArray());
            PrivateKey privateKey = converter.getPrivateKey(keyInfo.decryptPrivateKeyInfo(pkcs8Prov));
            System.out.println("Private key algorithm: " + privateKey.getAlgorithm());
            System.out.println("---------------end loadEncryptedPrivateKey---------------");
            return privateKey;
        } else {
            throw new Exception();
        }
    }

    /**
     * 从文件加载私钥（无口令保护）(pkcs8标准格式，参考测试用的文件)
     * @param filePath 私钥文件路径(支持ECC、RSA)
     * @return 私钥
     * @throws Exception 异常
     */
    public static PrivateKey loadPKCS8PrivateKey(String filePath) throws Exception {
        System.out.println("---------------begin loadPrivateKey---------------");
        try (PEMParser pemParser = new PEMParser(new FileReader(filePath))) {
            Object pem = pemParser.readObject();
            if (pem instanceof PrivateKeyInfo) {
                JcaPEMKeyConverter converter = new JcaPEMKeyConverter()
                        .setProvider(BouncyCastleProvider.PROVIDER_NAME);
                PrivateKeyInfo keyInfo = (PrivateKeyInfo) pem;
                PrivateKey privateKey = converter.getPrivateKey(keyInfo);
                System.out.println("Private key algorithm: " + privateKey.getAlgorithm());
                System.out.println("---------------end loadPrivateKey---------------");
                return privateKey;
            }
            throw new RuntimeException("invalid key file.");
        }
    }

    /**
     * 从文件加载公钥
     * @param filePath 公钥文件路径(支持ECC、RSA)
     * @return 私钥
     * @throws IOException 异常
     */
    public static PublicKey loadPublicKey(String filePath) throws IOException {
        System.out.println("---------------begin loadPublicKey---------------");
        PEMParser pemParser = new PEMParser(new FileReader(filePath));
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter()
                .setProvider(BouncyCastleProvider.PROVIDER_NAME);
        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(pemParser.readObject());
        PublicKey publicKey = converter.getPublicKey(publicKeyInfo);
        System.out.println("Public key algorithm: " + publicKey.getAlgorithm());
        System.out.println("---------------end loadPublicKey---------------");
        return publicKey;
    }

    /**
     * 从文件加载公钥2
     * @param filePath 公钥文件路径(支持ECC和RSA)
     * @param alo 公钥对应的算法（EC、RSA）
     * @return 公钥
     * @throws Exception 异常
     */
    public static PublicKey loadPublicKey2(String filePath, String alo) throws Exception{
        System.out.println("---------------begin loadPublicKey2---------------");
        PEMParser pemParser = new PEMParser(new FileReader(filePath));
        PemObject pemObject = pemParser.readPemObject();
        byte[] content = pemObject.getContent();
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
        KeyFactory factory = KeyFactory.getInstance(alo, BouncyCastleProvider.PROVIDER_NAME);
        PublicKey publicKey = factory.generatePublic(pubKeySpec);
        System.out.println("Public key algorithm: " + publicKey.getAlgorithm());
        System.out.println("---------------end loadPublicKey2---------------");
        return publicKey;
    }

    /**
     * 输出pem格式的密钥内容到控制台
     * @param alo 生成密钥对使用的算法，可选算法：DiffieHellman(等同于：DH)、DSA、RSA
     * @param keySize RSA时推荐长度为：2048/1024/3096 DH时推荐为：1024 DSA时推荐长度为：1024
     * @throws Exception 异常
     */
    public static void key2PemOutPut(String alo, Integer keySize) throws Exception {
        KeyPair keyPair = generateKeyPair(alo, keySize);
        System.out.println("---------------begin out put pem format key---------------");
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        StringWriter output = new StringWriter(keySize);
        PemWriter pemWriter = new PemWriter(output);
        PemObject pemObject = new PemObject("PRIVATE KEY", privateKey.getEncoded());
        pemWriter.writeObject(pemObject);
        pemWriter.close();
        System.out.println(output.getBuffer());

        StringWriter output2 = new StringWriter(keySize);
        PemWriter pemWriter2 = new PemWriter(output2);
        PemObject pemObject2 = new PemObject("PUBLIC KEY", publicKey.getEncoded());
        pemWriter2.writeObject(pemObject2);
        pemWriter2.close();
        System.out.println();
        System.out.println(output2);
        System.out.println("---------------end out put pem format key---------------");
        System.out.println();
    }

    /**
     * 输入pem格式的密钥到指定文件中
     * @param alo 生成密钥对使用的算法，可选算法：DiffieHellman(等同于：DH)、DSA、RSA
     * @param keySize RSA时推荐长度为：2048/1024/3096 DH时推荐为：1024 DSA时推荐长度为：1024
     * @param basePath 输出密钥文件到的文件夹目录路径
     * @throws Exception 异常
     */
    public static void key2PemFile(String alo, Integer keySize, String basePath) throws Exception {
        KeyPair keyPair = generateKeyPair(alo, keySize);
        System.out.println("---------------begin out put pem format key to file---------------");
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        String prvKeyPath = basePath + System.currentTimeMillis() + "_prvKey.key";
        File prvFile = new File(prvKeyPath);
        prvFile.createNewFile();
        PrintWriter printWriter = new PrintWriter(prvFile);
        PemWriter pemWriter = new PemWriter(printWriter);
        PemObject pemObject = new PemObject("PRIVATE KEY", privateKey.getEncoded());
        pemWriter.writeObject(pemObject);
        pemWriter.close();

        String pubKeyPath = basePath + System.currentTimeMillis() + "_pubKey.key";
        File pubFile = new File(pubKeyPath);
        PrintWriter printWriter2 = new PrintWriter(pubFile);
        PemWriter pemWriter2 = new PemWriter(printWriter2);
        PemObject pemObject2 = new PemObject("PUBLIC KEY", publicKey.getEncoded());
        pemWriter2.writeObject(pemObject2);
        pemWriter2.close();
        System.out.println("---------------end out put pem format key to file---------------");
        loadPublicKey(pubKeyPath);
        loadPKCS8PrivateKey(prvKeyPath);
    }

    /**
     * 生成密钥对并生成证书请求
     * @param alo 算法，可选：RSA、EC
     * @param keySize 密钥长度，RSA时推荐2048，EC时推荐256
     * @param csrInfos 证书请求的subject信息，除了emailAddress以外都必填
     * @return 证书请求对象
     * @throws Exception 异常
     */
    public static PKCS10CertificationRequest generateP10CertRequest(String alo, int keySize, CsrInfos csrInfos) throws Exception {
        System.out.println("---------------begin generateP10CertRequest---------------");
        PublicKey publicKey = null;
        PrivateKey privateKey = null;
        String sigAlo; // 签名算法
        // 区分两种算法的密钥生成
        // RSA
        if (StringUtils.equals(alo, "RSA")){
            KeyPair keyPair = generateKeyPair("RSA", keySize);
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
            sigAlo = "SHA256withRSA";
        } else { // ECC
            KeyPair keyPair = generateECCKeyPair(keySize);
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
            sigAlo = "SHA256withECDSA";
        }
        // 申请者信息
        String subjectStr = "C=" + csrInfos.getCountry() + ",ST="+csrInfos.getState()
                + ",L=" + csrInfos.getLocal() + ",O=" + csrInfos.getOrganization()
                + ",OU=" + csrInfos.getOrganizationUnit() + ",CN="+csrInfos.getCommonName();
        if(StringUtils.isBlank(csrInfos.getEmailAddress())){
            subjectStr += (",E=" + csrInfos.getEmailAddress());
        }
        // 开始构造CSR
        X500Principal subject = new X500Principal(subjectStr);
        PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(subject, publicKey);
        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder(sigAlo); // 签名算法
        ContentSigner signer = null;
        try {
            signer = csBuilder.build(privateKey);
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        }
        PKCS10CertificationRequest csr = p10Builder.build(signer); // PKCS10的证书请求
        // 输出：1.2.840.113549.1.1.11 表示 SHA256withRSA
        // 输出： 1.2.840.10045.4.3.2 表示 SHA256withECDSA
        System.out.println("Signature Algorithm OID: " + csr.getSignatureAlgorithm().getAlgorithm().toString());
        System.out.println("Subject of PKCS10 Certification Request: " + csr.getSubject());
        System.out.println("---------------begin generateP10CertRequest---------------");
        return csr;
    }

    /**
     * todo 生成密钥对并生成附带一些扩展字段的证书请求
     * @param alo 算法，可选：RSA、EC
     * @param csrInfos 证书请求的subject信息，除了emailAddress以外都必填
     * @return 证书请求对象
     * @throws Exception 异常
     */
    public static PKCS10CertificationRequest generateAttachExtensionsP10Csr(String alo, CsrInfos csrInfos) throws Exception{
        System.out.println("---------------begin generate attach Extensions P10 Csr---------------");
        PublicKey publicKey = null;
        PrivateKey privateKey = null;
        String sigAlo; // 签名算法
        // 区分两种算法的密钥生成
        // RSA
        if (StringUtils.equals(alo, "RSA")){
            KeyPair keyPair = generateKeyPair("RSA", 2048);
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
            sigAlo = "SHA256withRSA";
        } else { // ECC
            KeyPair keyPair = generateECCKeyPair(256);
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
            sigAlo = "SHA256withECDSA";
        }
        // 申请者信息
        String subjectStr = "C=" + csrInfos.getCountry() + ",ST="+csrInfos.getState()
                + ",L=" + csrInfos.getLocal() + ",O=" + csrInfos.getOrganization()
                + ",OU=" + csrInfos.getOrganizationUnit() + ",CN="+csrInfos.getCommonName();
        if(StringUtils.isBlank(csrInfos.getEmailAddress())){
            subjectStr += (",E=" + csrInfos.getEmailAddress());
        }
        // 开始构造CSR
        X500Principal subject = new X500Principal(subjectStr);
        PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(subject, publicKey);
        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder(sigAlo); // 签名算法
        ContentSigner signer = null;
        try {
            signer = csBuilder.build(privateKey);
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        }
        PKCS10CertificationRequest csr = p10Builder.build(signer); // PKCS10的证书请求
        // 输出：1.2.840.113549.1.1.11 表示 SHA256withRSA
        // 输出： 1.2.840.10045.4.3.2 表示 SHA256withECDSA
        System.out.println("Signature Algorithm OID: " + csr.getSignatureAlgorithm().getAlgorithm().toString());
        System.out.println("Subject of PKCS10 Certification Request: " + csr.getSubject());
        System.out.println("---------------end generate attach Extensions P10 Csr---------------");
        return csr;
    }

    /**
     * 读取证书请求文件，并加载成一个证书请求对象
     * @param path 证书请求路径
     * @param csrEncodeType 证书请求文件的编码方式，支持“PEM”，“DER”
     * @return 证书请求对象
     * @throws Exception 异常
     */
    public static PKCS10CertificationRequest loadCsrFromFile(String path, String csrEncodeType) throws Exception{
        System.out.println("---------------begin load CSR from file(" + csrEncodeType + ")---------------");
        // 开始依据不同的编码类型来加载证书请求
        PKCS10CertificationRequest csr;
        if("PEM".equals(csrEncodeType)) { // pem格式
            PEMParser pemParser = new PEMParser(new FileReader(path));
            PemObject pemObject = pemParser.readPemObject();
            byte[] content = pemObject.getContent();
            csr = new PKCS10CertificationRequest(content);
        } else { // der格式
            FileInputStream inputStream = new FileInputStream(path);
            int length = inputStream.available();
            byte[] bytes = new byte[length];
            inputStream.read(bytes);
            inputStream.close();
            csr = new PKCS10CertificationRequest(bytes);
        }
        System.out.println("CSR subject: " + csr.getSubject().toString());
        System.out.println("---------------end load CSR from file(" + csrEncodeType + ")---------------");
        return csr;
    }

    /**
     * 从证书请求中获取公钥
     * @param csr 证书请求对象
     * @param alo 算法，可选：RSA、EC
     * @return 证书请求里的公钥
     * @throws IOException 异常
     */
    public static PublicKey getPubKeyFromCsr(PKCS10CertificationRequest csr, String alo) throws Exception {
        System.out.println("---------------begin get public key from CSR---------------");
        StringWriter output = new StringWriter();
        PemWriter pemWriter = new PemWriter(output);
        PemObject pkPemObject = new PemObject("PUBLIC KEY", csr.getSubjectPublicKeyInfo().getEncoded());
        pemWriter.writeObject(pkPemObject);
        pemWriter.close();
        System.out.println("PEM String is: \n" + output.getBuffer());
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pkPemObject.getContent());
        KeyFactory factory = KeyFactory.getInstance(alo, BouncyCastleProvider.PROVIDER_NAME);
        PublicKey publicKey = factory.generatePublic(pubKeySpec);
        System.out.println("Public key algorithm is " + publicKey.getAlgorithm());
        System.out.println("---------------end get public key from CSR---------------");
        return publicKey;
    }

    /**
     * todo 获取证书请求中包含的一些信息
     * @param csr 证书请求对象
     */
    public static void getCsrMsg(PKCS10CertificationRequest csr) {
        System.out.println("---------------begin get csr message---------------");
        SubjectPublicKeyInfo subjectPublicKeyInfo= csr.getSubjectPublicKeyInfo();
        AlgorithmIdentifier algorithmIdentifier = subjectPublicKeyInfo.getAlgorithm();
        System.out.println("Public key algorithm identifier is : " + algorithmIdentifier.getAlgorithm().toString());
        System.out.println("Csr signature is : " + Hex.toHexString(csr.getSignature()));
        Extensions extensions = csr.getRequestedExtensions();
        // 其实挺多时候用纯软生成证书请求时，没有设置扩展字段
        if(extensions != null) {
            ASN1ObjectIdentifier[] extensionOIDs = extensions.getExtensionOIDs();
            for (int i = 0; i < extensionOIDs.length; i++) {
                System.out.println("ExtensionOIDs[" + i + "]: " + extensionOIDs[i]);
            }
            ASN1ObjectIdentifier[] criticalExtensionOIDs = extensions.getCriticalExtensionOIDs();
            for (int i = 0; i < criticalExtensionOIDs.length; i++) {
                System.out.println("CriticalExtensionOIDs[" + i + "]: " + criticalExtensionOIDs[i]);
            }
            ASN1ObjectIdentifier[] nonCriticalExtensionOIDs = extensions.getNonCriticalExtensionOIDs();
            for (int i = 0; i < nonCriticalExtensionOIDs.length; i++) {
                System.out.println("NonCriticalExtensionOIDs[" + i + "]: " + nonCriticalExtensionOIDs[i]);
            }
        }
        X500Name x500Name = csr.getSubject();
        System.out.println("Csr subject is: " + x500Name.toString());
        System.out.println("---------------end get csr message---------------");
    }

    /**
     * 颁发证书
     * @param csr 证书请求
     * @param issuerCertPath 用来颁发证书的父证书（通常是ca证书）
     * @param issuerPrvKeyPath 用于颁发证书的父证书对应的私钥
     * @param validDays 新证书的有效时间（单位：天）
     * @return 新的证书对象
     * @throws Exception 异常
     */
    public static X509Certificate issueCert(PKCS10CertificationRequest csr, String issuerCertPath,
                                            String issuerPrvKeyPath, long validDays) throws Exception {
        System.out.println("---------------begin issueCert---------------");
        Key caPrivateKey = null;
        CertificateFactory certFactory;
        X509Certificate caCert = null;

        certFactory = CertificateFactory.getInstance("X.509");
        caCert = (X509Certificate) certFactory.generateCertificate(new FileInputStream(issuerCertPath));  // 读取Ca证书
        caPrivateKey = loadRSAPKCS1PrivateKey(issuerPrvKeyPath); // 读取私钥文件

        AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA256withRSA"); // ca的签名算法标识符
        AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId); // 摘要算法标识符
        assert caCert != null;
        X500Name issuer = new X500Name(caCert.getSubjectX500Principal().getName()); // 颁发者信息
        BigInteger serial = new BigInteger(32, new SecureRandom()); // 序列号
        Date from = new Date(); // 证书起始生效时间
        Date to = new Date(System.currentTimeMillis() + validDays * 24 * 60 * 60 * 1000); // 证书失效时间

        // 使用x509来组装证书
        X509v3CertificateBuilder certGen = new X509v3CertificateBuilder(issuer, serial, from, to,
                csr.getSubject(), csr.getSubjectPublicKeyInfo());
        // CA端进行签名, 才有具有法律效力
        X509Certificate cert = null;
        ContentSigner signer = new BcRSAContentSignerBuilder(sigAlgId, digAlgId).build(PrivateKeyFactory.createKey(caPrivateKey.getEncoded()));
        // 生成BC结构的证书
        Security.addProvider(new BouncyCastleProvider());
        cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certGen.build(signer));

        System.out.println("新证书的版本： " + cert.getVersion());
        System.out.println("新证书的subject: " + cert.getSubjectX500Principal().getName());
        System.out.println("新证书的颁发者subject: " + cert.getIssuerDN().getName());
        System.out.println("---------------end issueCert---------------");
        return cert;
    }

    // todo 颁发证书，同时设置一些扩展字段

    /**
     * 将一个证书对象以PEM格式存入对应路径的文件中(本方法最后会删除生成文件，使用时注意去掉这个操作)
     * @param x509Certificate 证书对象
     * @param basePath 要存放的路径目录
     * @throws Exception 异常
     */
    public static void cert2PemFile(X509Certificate x509Certificate, String basePath) throws Exception{
        System.out.println("---------------begin store certificate object to pem file---------------");
        String certPath = basePath + System.currentTimeMillis() + "_cert.crt";
        File certFile = new File(certPath);
        certFile.createNewFile();
        PrintWriter printWriter = new PrintWriter(certFile);
        PemWriter pemWriter = new PemWriter(printWriter);
        PemObject pemObject = new PemObject("CERTIFICATE", x509Certificate.getEncoded());
        pemWriter.writeObject(pemObject);
        pemWriter.close();
        System.out.println("---------------end store certificate object to pem file---------------");
        // 校验生成的文件是否正确
        getPubKeyFromCert(certPath);
        // 删除生成的文件
        FileIO.deleteFile(certPath);
    }

    /**
     * 从证书中读取出公钥（含加载pem格式证书）
     * @param path 证书文件路径
     * @return 公钥
     * @throws CertificateException 异常
     * @throws FileNotFoundException 异常
     */
    public static PublicKey getPubKeyFromCert(String path) throws Exception {
        System.out.println("---------------begin get public key from CERT---------------");
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(new FileInputStream(path));
        PublicKey publicKey = certificate.getPublicKey();
        System.out.println("Public key algorithm: " + publicKey.getAlgorithm());
        System.out.println("---------------end get public key from CERT---------------");
        return publicKey;
    }

    /**
     * 读取证书文件并加载成一个证书对象（复杂式写法）
     * （方法使用了sun.*包，可能会导致打包失败，请看readme.md文件描述）
     * @param path 证书存储路径
     * @param certEncodeType 证书编码类型，支持“PEM"和”DER“
     * @return 证书对象
     * @throws Exception 异常
     */
    public static X509Certificate loadCertFromFile(String path, String certEncodeType) throws Exception{
        System.out.println("---------------begin load CERT from file(" + certEncodeType + ")---------------");
        FileInputStream inputStream = new FileInputStream(path);
        int length = inputStream.available();
        byte[] bytes = new byte[length];
        inputStream.read(bytes);
        inputStream.close();
        // 开始依据不同的编码类型来加载证书
        X509Certificate x509Certificate;
        if("PEM".equals(certEncodeType)) { // pem格式
            String crtStr = new String(bytes, StandardCharsets.UTF_8);
            crtStr = crtStr.replace("-----BEGIN CERTIFICATE-----", ""); // 为避免不同平台的回车换行问题，所以这里只匹配开头的文件类型描述
            crtStr = crtStr.replace("-----END CERTIFICATE-----", "");   // 为避免不同平台的回车换行问题，所以这里只匹配结尾的文件类型描述
            crtStr = crtStr.replaceAll("\n", "");      // 去掉换行
            crtStr = crtStr.replaceAll("\r", "");      // 去掉某些平台带有的回车
            x509Certificate = new X509CertImpl(Base64.getDecoder().decode(crtStr));
        } else { // der格式
            x509Certificate = new X509CertImpl(bytes);
        }
        System.out.println("Cert subject: " + x509Certificate.getSubjectX500Principal().getName());
        System.out.println("---------------end load CERT from file(" + certEncodeType + ")---------------");
        return x509Certificate;
    }

    /**
     * 从der格式(为了方便传递，一般der格式内容会转为十六进制串来传递)转换成pem格式
     * @param der 一个Hex-String形式的der格式证书请求内容
     * @param type 文件类型，支持“CSR","CERT","PRV_KEY","PUB_KEY"
     * @return pem格式的字符串
     */
    public static String der2pem(String der, String type){
        System.out.println("---------------begin (" + type + ") DER to PEM---------------");
        PemObject pemObject;
        byte[] derBytes = Hex.decode(der);
        switch (type){
            case "CSR":
                pemObject = new PemObject("CERTIFICATE REQUEST", derBytes);
                break;
            case "CERT":
                pemObject = new PemObject("CERTIFICATE", derBytes);
                break;
            case "PRV_KEY":
                pemObject = new PemObject("PRIVATE KEY", derBytes);
                break;
            default:
                pemObject = new PemObject("PUBLIC KEY", derBytes);
        }
        StringWriter stringWriter = new StringWriter();
        JcaPEMWriter pemWriter = new JcaPEMWriter(stringWriter);
        try {
            pemWriter.writeObject(pemObject);
            pemWriter.flush();
        } catch (IOException e) {}
        finally {
            try {
                pemWriter.close();
            } catch (IOException e) {}
        }
        String pem = stringWriter.toString();
        System.out.println(type + " in pem: \n" + pem);
        System.out.println("---------------begin (" + type + ") DER to PEM---------------");
        return pem;
    };

    /**
     * 将传进来的pem格式证书字符串转为der格式（Hex-String）
     * （方法使用了sun.*包，可能会导致打包失败，请看readme.md文件描述）
     * @param crtStr pem格式证书字符串
     * @return der格式（Hex-String）证书
     * @throws Exception 异常
     */
    public static String certPem2DerHexStr(String crtStr) throws Exception {
        System.out.println("---------------begin CERT PEM to DER---------------");

        crtStr = crtStr.replace("-----BEGIN CERTIFICATE-----", ""); // 为避免不同平台的回车换行问题，所以这里只匹配开头的文件类型描述
        crtStr = crtStr.replace("-----END CERTIFICATE-----", "");   // 为避免不同平台的回车换行问题，所以这里只匹配结尾的文件类型描述
        crtStr = crtStr.replaceAll("\n", "");      // 去掉换行
        crtStr = crtStr.replaceAll("\r", "");      // 去掉某些平台带有的回车
        // 转为证书对象
        X509Certificate x509Certificate = new X509CertImpl(Base64.getDecoder().decode(crtStr));
        byte[] bytes = x509Certificate.getEncoded();
        // 获得der格式编码
        String certDER = Hex.toHexString(bytes);
        System.out.println("CERT in der(Hex-String): " + certDER);
        System.out.println("---------------end CERT PEM to DER---------------");
        return certDER;
    }

    /**
     * 依据传入的证书对象，获取证书的所有信息
     * @param x509Certificate 证书对象
     * @throws IOException 异常
     */
    public static void getCertMsg(X509Certificate x509Certificate) throws IOException {
        System.out.println("---------------begin get certificate message---------------");
        System.out.println("Cert version is " + x509Certificate.getVersion());
        System.out.println("Cert serial number: " + x509Certificate.getSerialNumber());
        System.out.println("Cert sign algorithm is " + x509Certificate.getSigAlgName());
        System.out.println("Cert hash value is " + Hex.toHexString(x509Certificate.getSignature()));

        Date notBefore = x509Certificate.getNotBefore();
        Date notAfter = x509Certificate.getNotAfter();
        System.out.println("Cert valid time is from " + notBefore.toString() + " to " + notAfter.toString());

        System.out.println("Subject: " + x509Certificate.getSubjectX500Principal().getName());
        System.out.println("Issuer subject: " + x509Certificate.getIssuerX500Principal().getName());
        sun.security.x509.X500Name x500Name = sun.security.x509.X500Name.asX500Name(x509Certificate.getSubjectX500Principal());
        System.out.println("Common Name(CN): " + x500Name.getCommonName());
        System.out.println("Country(C): " + x500Name.getCountry());
        System.out.println("State(S): " + x500Name.getState());
        System.out.println("Locality(L): " + x500Name.getLocality());
        System.out.println("Organization(O): " + x500Name.getOrganization());
        System.out.println("Organization Unit(OU): " + x500Name.getOrganizationalUnit());
        // 由于邮箱地址实际上创建证书请求时是选填的，所以好像没有提供获取EmailAddress(E)的方法
        sun.security.x509.X500Name issuerX500Name = sun.security.x509.X500Name.asX500Name(x509Certificate.getIssuerX500Principal());
        System.out.println("Issuer CN: " + issuerX500Name.getCommonName()); // 颁发者的subject信息获取方式同自身，所以这里不做重复
        System.out.println("---------------end get certificate message---------------");
    }

    /**
     * 加密
     * 注意：如果是ECC，则只能用公钥加密，不支持私钥加密数据
     * 注意：ECC本身并没有真正定义任何加密/解密操作，构建在椭圆曲线上的算法确实如此（todo 待确认）
     * @param key 用来加密的密钥
     * @param alo 密钥对应的算法/模式/填充模式，支持”RSA“（这么写的话，模式和填充模式依赖于算法提供者怎么设置默认值,BC库的话等同于“RSA/ECB/NoPadding”）、
     *            “RSA/ECB/PKCS1Padding”、”RSA/ECB/OAEPWithSHA-1AndMGF1Padding“、”RSA/ECB/OAEPWithSHA-256AndMGF1Padding“、
     *            “ECIES"(ECIES表示ECC)
     * @param data 等待被加密的数据，数据不能太长
     * @return 密文数据
     * @throws Exception 异常
     */
    public static byte[] encryptData(Key key, String alo, byte[] data) throws Exception{
        System.out.println("---------------begin encrypt data---------------");
        System.out.println("alo is: " + alo);
        System.out.println("data length is: " + data.length);
        Cipher cipher = Cipher.getInstance(alo, BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] res = cipher.doFinal(data);
        System.out.println("Hex String type encrypted result data is: " + Hex.toHexString(res));
        System.out.println("Base64 String type encrypted result data is: " + Base64.getEncoder().encodeToString(res));
        System.out.println("---------------end encrypt data---------------");
        return res;
    }

    /**
     * 解密
     * 注意：如果是ECC，则只能用私钥解密，不支持公钥解密数据
     * 注意：ECC本身并没有真正定义任何加密/解密操作，构建在椭圆曲线上的算法确实如此（todo 待确认）
     * @param key 用来解密的密钥
     * @param alo 密钥对应的算法/模式/填充模式，要和加密时使用的配置一致，
     *            支持”RSA“（这么写的话，模式和填充模式依赖于算法提供者怎么设置默认值,BC库的话等同于“RSA/ECB/NoPadding”）、
     *            “RSA/ECB/PKCS1Padding”、”RSA/ECB/OAEPWithSHA-1AndMGF1Padding“、”RSA/ECB/OAEPWithSHA-256AndMGF1Padding“、
     *            “ECIES"(ECIES表示ECC)
     * @param encData 等待被解密的密文数据
     * @return 明文数据
     * @throws Exception 异常
     */
    public static byte[] decryptData(Key key, String alo, byte[] encData) throws Exception{
        System.out.println("---------------begin decrypt data---------------");
        System.out.println("alo is: " + alo);
        System.out.println("encrypted data length is: " + encData.length);
        Cipher cipher = Cipher.getInstance(alo, BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] res = cipher.doFinal(encData);
        System.out.println("Hex String type decrypt result data is: " + Hex.toHexString(res));
        System.out.println("Base64 String type decrypt result data is: " + Base64.getEncoder().encodeToString(res));
        System.out.println("---------------end decrypt data---------------");
        return res;
    }

    /**
     * 利用密钥对数据进行签名
     * @param prvKey 私钥（签名用的都是私钥）
     * @param keyAlo 密钥的算法, 支持：RSA,DSA,ECDSA、ECNR
     * @param data 待签名数据
     * @param hashType 签名过程中要用到的摘要算法, 支持：SHA1,SHA256 (较常用的是SHA256)
     * @return 签名值
     * @throws Exception 异常
     */
    public static byte[] signData(PrivateKey prvKey, String keyAlo, byte[] data, String hashType) throws Exception {
        System.out.println("---------------begin sign data---------------");
        String signAlo = hashType + "with" + keyAlo;
        // java原生对ECC不支持，使用BC库是为了支持EC，如果不用ECC，那么可以不用设置provider
        Signature signature = Signature.getInstance(signAlo, BouncyCastleProvider.PROVIDER_NAME);
        signature.initSign(prvKey);
        signature.update(data);
        byte[] signatureValue = signature.sign();
        System.out.println("Source data is(Hex-String): " + Hex.toHexString(data));
        System.out.println("Signature algorithm is: " + signAlo);
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
        byte[] hashData = Cryptology.digestData(data, hashType);
        // 这个头很神奇，其实本质是一个ASN1编码的数据，而且值会因为摘要算法变，这里是一个经验值（不是特别确定）
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
            signatureValue = encryptData(prvKey, keyAlo + "/ECB/PKCS1Padding", Hex.decode(waitForEncryptData));
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
     * @return 是否正确
     * @throws Exception 异常
     */
    public static boolean validSignature(PublicKey pubKey, String keyAlo, byte[] data, String hashType, byte[] signatureValue) throws Exception {
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

    // 验证一个公钥和一个私钥是否匹配，其实就是加密一个数据，然后解密，看看解密后的结果与原始数据是否一致（目前没有找到更便捷的流程）
    // 验证一个私钥与一本证书是否匹配，其实就是先从证书中提取公钥，然后就是和上一个一样的流程（目前没有找到更便捷的流程）

    /**
     * 生成p12文件到指定文件夹中
     * 这种方式生成的p12文件等同于在openssl1.1.1版本中生成的p12文件
     * 在openssl3.0处进行解析验证时，会出现无法正常显示证书的问题
     * 因为使用的加密方式是比较旧的RC2-40-CBC，该加密方式已被认为是不安全的，于是openssl在3.0中进行了剔除，3.0之前版本的openssl可以照常解析
     * @param rootCert 根证书
     * @param subCert 子证书
     * @param subKey 子证书对应的私钥
     * @param p12Pwd 生成的p12文件的口令
     * @param outputFolderPath 指定的p12文件输出文件夹
     * @throws Exception 异常
     */
    public static void generateP12(java.security.cert.Certificate rootCert, java.security.cert.Certificate subCert, PrivateKey subKey,
                                   String p12Pwd, String outputFolderPath) throws Exception{
        System.out.println("---------------begin generate P12---------------");
        System.out.println("keyStore default type is: " + KeyStore.getDefaultType());
        KeyStore keyStore = KeyStore.getInstance("PKCS12",  new BouncyCastleProvider());
        keyStore.load(null, null);
        java.security.cert.Certificate[] certificates = new java.security.cert.Certificate[1];
        certificates[0] = subCert;
        // 将给定的密钥分配给给定的别名，并使用给定的密码对其进行保护。
        // 如果给定的密钥类型为 java.security.PrivateKey，它必须附带一个证书链来验证相应的公钥。
        // 如果给定的别名已存在，则与其关联的密钥库信息将被给定的密钥（可能还有证书链）覆盖
        // 此处用来校验的证书的certificates的数组第一个元素（即一个证书对象），会被用别名来保存进p12，所以推荐只放一个私钥对应的证书
        keyStore.setKeyEntry("sub", subKey, p12Pwd.toCharArray(), certificates);
        // 如果准备在p12中放证书链，则可以把ca证书放进去
        keyStore.setCertificateEntry("rootCert", rootCert);
        // 将生成的p12输出到一个文件中
        keyStore.store(new FileOutputStream(outputFolderPath + System.currentTimeMillis() + ".p12"), p12Pwd.toCharArray());
        System.out.println("---------------end generate P12---------------");
        // 验证
        Cryptology.getCertMsg((X509Certificate) keyStore.getCertificate("rootCert"));
        Cryptology.getCertMsg((X509Certificate) keyStore.getCertificate("sub"));
    }

    // todo 解析p12
    // todo 验证证书链是否正确
}
