package com.basic.happytest.modules.cryptology;

import com.sun.crypto.provider.SunJCE;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.pkcs.*;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.PKCS8Generator;
import org.bouncycastle.openssl.jcajce.*;
import org.bouncycastle.operator.*;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;
import sun.security.x509.AVA;
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
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;
import java.util.*;

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
     * 获取BC库支持的算法
     */
    public static void keyAloSupportedInBCLibrary() {
        Provider provider = new org.bouncycastle.jce.provider.BouncyCastleProvider();
        for (Provider.Service service : provider.getServices()) {
            System.out.println(service.getType() + ": " + service.getAlgorithm());
        }
    }

    /**
     * 展示当前所有的算法提供者和信息
     */
    public static void showProviders() {
        System.out.println("-------当前有这些算法提供者-------");
        for (Provider provider : Security.getProviders()) {
            System.out.println("算法提供者名：" + provider.getName());
            System.out.println("算法提供者的版本：" + provider.getVersion());
            System.out.println("算法提供者的信息：" + provider.getInfo());
            if(Objects.equals(provider.getName(), "SunJCE")){
                for (Provider.Service service : provider.getServices()) {
                    System.out.println(service.getType() + ": " + service.getAlgorithm());
                }
            }
        }
        System.out.println("------------展示完毕------------");
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
        System.out.println("Original message(Hex): " + Hex.toHexString(data) + ", Digest Value(HEX): "
                + Hex.toHexString(digestValue));
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
            // 由私钥获取模 N
            BigInteger modulus = keySpec.getModulus();
            int length = modulus.bitLength();
            // RSA密钥的长度实际上指的是模的长度（以Bit为单位）, 模是私钥和公钥共有的
            System.out.println("RSA key size: " + length);
            // RSA byte长度
            System.out.println("RSA key byte length(by sun.security.rsa.RSACore): " + (length + 7 >> 3));
            System.out.println("The length of RSA key module in decimal: " + modulus.toString(10).length());
            // 私钥的指数d
            System.out.println("RSA private key exponent(d) size: " + keySpec.getPrivateExponent().bitLength());
            // 借助RSAPrivateKey可以获取私钥的所有信息
            RSAPrivateKey rsaPrivateKey = prvKey2BCRSAPrvKey(privateKey);
            System.out.println("RSA private key prime1 length(p): " + rsaPrivateKey.getPrime1().bitLength());
            System.out.println("RSA private key prime2 length(q): " + rsaPrivateKey.getPrime2().bitLength());
            System.out.println("p*q = module: " + (rsaPrivateKey.getPrime1().multiply(rsaPrivateKey.getPrime2()))
                    .equals(rsaPrivateKey.getModulus()));
            // 以下是公钥信息
            RSAPublicKeySpec pubKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
            // 公钥的指数e
            System.out.println("RSA public key exponent(e): " + pubKeySpec.getPublicExponent().toString(10));
            // 由公钥获取模 N
            System.out.println("RSA key size: " + pubKeySpec.getModulus().bitLength());
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
        System.out.println("---------------begin generate ECC key pair---------------");
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
        System.out.println("---------------end generate ECC key pair---------------");
        return keyPair;
    }

    /**
     * 暴力读取RSA私钥文件(pkcs1标准格式的私钥, 且无口令保护, 参考测试用的文件)<br/>
     * （方法使用了sun.*包，可能会导致打包失败，请看readme.md文件描述）
     * @param pemFilePath pem格式私钥文件绝对路径
     * @return PKCS8标准的RSA私钥
     * @throws Exception 异常
     */
    public static PrivateKey loadRSAPKCS1PrivateKey(String pemFilePath) throws Exception {
        System.out.println("---------------begin pem file load private key PKCS1 encoded---------------");
        // PKCS#1 format
        final String PEM_RSA_PRIVATE_START = "-----BEGIN RSA PRIVATE KEY-----";
        final String PEM_RSA_PRIVATE_END = "-----END RSA PRIVATE KEY-----";
        // todo 统一文件内容加载
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
        // 这里的keySpec是以PCKS1标准定义的
        RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp, prime1,
                prime2, exp1, exp2, crtCoef);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = factory.generatePrivate(keySpec);
        System.out.println("Private key algorithm: " + privateKey.getAlgorithm());
        System.out.println("---------------end pem file load private key PKCS1 encoded---------------");
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
        System.out.println("---------------begin load encrypted private key---------------");
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
            System.out.println("---------------end load encrypted private key---------------");
            return privateKey;
        } else {
            throw new Exception();
        }
    }

    /**
     * 从文件加载私钥（无口令保护）(参考测试用的文件)
     * @param filePath 私钥文件路径(支持ECC、RSA)
     * @return PKCS8标准的私钥
     * @throws Exception 异常
     */
    public static PrivateKey loadPrivateKey(String filePath) throws Exception {
        System.out.println("---------------begin load private key---------------");
        try (PEMParser pemParser = new PEMParser(new FileReader(filePath))) {
            Object pem = pemParser.readObject();
            System.out.println("pem object class is: " + pem.getClass());
            if (pem instanceof PrivateKeyInfo) { // PKCS8标准的私钥加载后会是这个对象
                JcaPEMKeyConverter converter = new JcaPEMKeyConverter()
                        .setProvider(BouncyCastleProvider.PROVIDER_NAME);
                PrivateKeyInfo keyInfo = (PrivateKeyInfo) pem;
                PrivateKey privateKey = converter.getPrivateKey(keyInfo);
                System.out.println("Private key algorithm: " + privateKey.getAlgorithm());
                System.out.println("---------------end load private key---------------");
                return privateKey;
            } else if (pem instanceof PEMKeyPair){ // PKCS1标准的私钥加载后会是这个对象
                JcaPEMKeyConverter converter = new JcaPEMKeyConverter()
                        .setProvider(BouncyCastleProvider.PROVIDER_NAME);
                PEMKeyPair pemKeyPair = (PEMKeyPair) pem;
                PrivateKeyInfo keyInfo = pemKeyPair.getPrivateKeyInfo();
                PrivateKey privateKey = converter.getPrivateKey(keyInfo);
                System.out.println("Private key algorithm: " + privateKey.getAlgorithm());
                SubjectPublicKeyInfo publicKeyInfo = pemKeyPair.getPublicKeyInfo();
                System.out.println("Public key algorithm: " + publicKeyInfo.getAlgorithm());
                System.out.println("---------------end load private key---------------");
                return privateKey;
            }
            throw new RuntimeException("invalid key file.");
        }
    }

    /**
     * 从文件中获取PKCS8的无口令保护的私钥，然后转成以指定口令保护的PKCS8私钥，并将结果存入文件
     * @param filePath PKCS8的无口令保护的私钥存储路径
     * @param pwd 即将生成的私钥文件的保护口令
     * @param outputBasePath 结果输出的文件夹路径
     */
    public static void encryptP8KeyFromFileAnd2File(String filePath, String pwd,
                                                    String outputBasePath) throws Exception{
        System.out.println("---------------begin transform P8 key which from file to protected by password and store in file---------------");
        // PKCS8Generator中有很多用于保护密钥的算法可供选择设置
        // 这里是一个加密器构造器，后续将用这个加密器构造器的设置好的内容创建一个加密器
        JceOpenSSLPKCS8EncryptorBuilder encryptorBuilder = new JceOpenSSLPKCS8EncryptorBuilder(PKCS8Generator.PBE_SHA1_RC2_40);
        // SecureRandom该类提供加密强随机数生成器(RNG), 线程安全
        encryptorBuilder.setRandom(new SecureRandom());
        // 设置保护口令
        encryptorBuilder.setPassword(pwd.toCharArray());
        // OutputEncryptor 操作器的常规接口，能够生成将输出加密数据的输出流
        OutputEncryptor encryptor = encryptorBuilder.build();
        // 读取无加密保护的私钥
        PrivateKey privateKey = Cryptology.loadPrivateKey(filePath);
        // PKCS8生成器
        JcaPKCS8Generator gen2 = new JcaPKCS8Generator(privateKey, encryptor);
        PemObject pemObject = gen2.generate();
        // 将结果输出到文件
        File prvFile = new File(outputBasePath + System.currentTimeMillis() + "_protectedPrvKey.key");
        PrintWriter printWriter = new PrintWriter(prvFile);
        PemWriter pemWriter = new PemWriter(printWriter);
        pemWriter.writeObject(pemObject);
        pemWriter.close();
        System.out.println("---------------end transform P8 key which from file to protected by password and store in file---------------");
    }

    /**
     * PKCS8标准的私钥转PCKS1标准的私钥字节数组（或者可以说是从中提取出最原始的密钥算法的那一部分内容）
     * @param privateKey java默认的PKCS8标准的私钥对象
     * @return PCKS1标准的私钥字节数组
     * @throws Exception 异常
     */
    public static byte[] p8PrvKey2P1PrvKeyBytes(PrivateKey privateKey) throws Exception {
        PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKey.getEncoded());
        ASN1Encodable privateKeyPKCS1ASN1Encodable = privateKeyInfo.parsePrivateKey();
        ASN1Primitive asn1Primitive = privateKeyPKCS1ASN1Encodable.toASN1Primitive();
        return asn1Primitive.getEncoded();
    }

    /**
     * 将java.security.PrivateKey转换成org.bouncycastle.asn1.pkcs.RSAPrivateKey对象
     * @param privateKey java.security.PrivateKey对象
     * @return org.bouncycastle.asn1.pkcs.RSAPrivateKey对象
     */
    public static RSAPrivateKey prvKey2BCRSAPrvKey(PrivateKey privateKey) throws Exception {
        // 这里getInstance只接受PKCS1的私钥转换成的byte数组，否则会报错
        // 报错信息：org.bouncycastle.asn1.DLSequence cannot be cast to org.bouncycastle.asn1.ASN1Integer
        // 所以需要先转换成PKCS1的私钥信息，才能输入进来
        // 这里输入asn1Primitive也可以
        RSAPrivateKey rsaPrivateKey = RSAPrivateKey.getInstance(p8PrvKey2P1PrvKeyBytes(privateKey));
        System.out.println("RSA key version is: " + rsaPrivateKey.getVersion());
        return rsaPrivateKey;
    }

    /**
     * 从文件加载公钥
     * @param filePath 公钥文件路径(支持ECC、RSA)
     * @return 公钥对象
     * @throws IOException 异常
     */
    public static PublicKey loadPublicKey(String filePath) throws IOException {
        System.out.println("---------------begin load public key---------------");
        PEMParser pemParser = new PEMParser(new FileReader(filePath));
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter()
                .setProvider(BouncyCastleProvider.PROVIDER_NAME);
        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(pemParser.readObject());
        PublicKey publicKey = converter.getPublicKey(publicKeyInfo);
        System.out.println("Public key algorithm: " + publicKey.getAlgorithm());
        System.out.println("---------------end load public key---------------");
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
        System.out.println("---------------begin load Public Key 2---------------");
        PEMParser pemParser = new PEMParser(new FileReader(filePath));
        PemObject pemObject = pemParser.readPemObject();
        byte[] content = pemObject.getContent();
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
        KeyFactory factory = KeyFactory.getInstance(alo, BouncyCastleProvider.PROVIDER_NAME);
        PublicKey publicKey = factory.generatePublic(pubKeySpec);
        System.out.println("Public key algorithm: " + publicKey.getAlgorithm());
        System.out.println("---------------end load Public Key 2---------------");
        return publicKey;
    }

    /**
     * 依据RSA私钥对象获取公钥对象(仅支持RSA2048/1024长度)
     * @param privateKey 私钥对象
     * @return 公钥对象
     */
    public static PublicKey loadRSAPubKeyByPriKey(PrivateKey privateKey) throws Exception {
        System.out.println("---------------begin load public key from private key---------------");
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey publicKey;
        RSAPrivateKeySpec prvKeySpec = kf.getKeySpec(privateKey, RSAPrivateKeySpec.class);
        // 参数：公钥模，公钥指数
        // 实测发现java生成密钥对，则1024和2048长度的公钥的指数都是用的65537（十进制）
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(prvKeySpec.getModulus(), BigInteger.valueOf(65537));
        publicKey = kf.generatePublic(keySpec);
        System.out.println("Public key algorithm: " + publicKey.getAlgorithm());
        System.out.println("---------------end load public key from private key---------------");
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
     * 输出pem格式的密钥到指定文件中
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
        loadPrivateKey(prvKeyPath);
    }

    /**
     * 生成证书请求
     * @param alo 密钥算法，可选：RSA、EC
     * @param keyPair 密钥对
     * @param csrInfos 证书请求的subject信息，除了emailAddress以外都必填
     * @return 证书请求对象
     * @throws Exception 异常
     */
    public static PKCS10CertificationRequest generateP10CertRequest(String alo, KeyPair keyPair,
                                                                    CsrInfos csrInfos) throws Exception {
        System.out.println("---------------begin generate P10 cert request---------------");
        PublicKey publicKey = null;
        PrivateKey privateKey = null;
        String sigAlo; // 签名算法
        // 区分两种算法
        // RSA
        if (StringUtils.equals(alo, "RSA")){
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
            sigAlo = "SHA256withRSA";
        } else { // ECC
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
            sigAlo = "SHA256withECDSA";
        }
        // 申请者信息
        String subjectStr = "C=" + csrInfos.getCountry() + ",ST="+csrInfos.getState()
                + ",L=" + csrInfos.getLocal() + ",O=" + csrInfos.getOrganization()
                + ",OU=" + csrInfos.getOrganizationUnit() + ",CN="+csrInfos.getCommonName();
        if(StringUtils.isNotBlank(csrInfos.getEmailAddress())){
            subjectStr += (",EmailAddress=" + csrInfos.getEmailAddress());
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
        System.out.println("---------------end generate P10 cert request---------------");
        return csr;
    }

    /**
     * 生成附带一些扩展字段的证书请求(扩展字段方面主要是举例说明，实际使用要做修改)
     * @param alo 密钥算法，可选：RSA、EC
     * @param keyPair 密钥对
     * @param csrInfos 证书请求的subject信息，除了emailAddress以外都必填
     * @return 证书请求对象
     * @throws Exception 异常
     */
    public static PKCS10CertificationRequest generateAttachExtensionsP10Csr(String alo, KeyPair keyPair,
                                                                            CsrInfos csrInfos) throws Exception{
        System.out.println("---------------begin generate attach extensions P10 Csr---------------");
        PublicKey publicKey = null;
        PrivateKey privateKey = null;
        String sigAlo; // 签名算法
        // 区分两种算法
        // RSA
        if (StringUtils.equals(alo, "RSA")){
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
            sigAlo = "SHA256withRSA";
        } else { // ECC
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
            sigAlo = "SHA256withECDSA";
        }
        // 申请者信息
        String subjectStr = "C=" + csrInfos.getCountry() + ",ST="+csrInfos.getState()
                + ",L=" + csrInfos.getLocal() + ",O=" + csrInfos.getOrganization()
                + ",OU=" + csrInfos.getOrganizationUnit() + ",CN="+csrInfos.getCommonName();
        if(StringUtils.isNotBlank(csrInfos.getEmailAddress())){
            subjectStr += (",EmailAddress=" + csrInfos.getEmailAddress());
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
        // 设置扩展字段（一般csr里面不带扩展字段，因为颁证时可以忽略；有带扩展字段的话，一般也就是密钥用途和扩展密钥用途）
        ExtensionsGenerator extensionsGenerator = new ExtensionsGenerator();
        // addExtension 第一个字段表示：扩展字段的类型；第二个参数表示：是否要将本字段设置为critical；第三个参数表示：扩展字段的值
        // 这里是尝试性地添加了一个密钥用途的扩展字段，具体是——不可否认。
        // KeyUsage里有很多可选用的密钥用途可以用来设置
        extensionsGenerator.addExtension(Extension.keyUsage, true,
                new KeyUsage(KeyUsage.nonRepudiation | KeyUsage.keyAgreement));
        // 这里是尝试性随便添加了几个扩展密钥用途的扩展字段
        // KeyPurposeId中有很多可以直接拿来使用
        // 如果要自定义用途，则只能自定义最后一位，我这边用"888"来表示。对应的UID是：2.5.29.37.888,前三位表示的是extendedKeyUsage
        ASN1EncodableVector purposes = new ASN1EncodableVector();
        purposes.add(KeyPurposeId.id_kp_serverAuth);
        purposes.add(KeyPurposeId.id_kp_clientAuth);
        purposes.add(KeyPurposeId.anyExtendedKeyUsage);
        purposes.add(Extension.extendedKeyUsage.branch("888"));
        extensionsGenerator.addExtension(Extension.extendedKeyUsage, true, new DERSequence(purposes));
        // 在csr构造器中设置扩展属性
        p10Builder.addAttribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest, extensionsGenerator.generate());
        // 开始生成PKCS10的证书请求
        PKCS10CertificationRequest csr = p10Builder.build(signer);
        System.out.println("---------------end generate attach extensions P10 Csr---------------");
        // 输出：1.2.840.113549.1.1.11 表示 SHA256withRSA
        // 输出： 1.2.840.10045.4.3.2 表示 SHA256withECDSA
        System.out.println("Signature Algorithm OID: " + csr.getSignatureAlgorithm().getAlgorithm().toString());
        System.out.println("Subject of PKCS10 Certification Request: " + csr.getSubject());
        return csr;
    }

    /**
     * 利用公钥和subject生成csr中待签名的部分（分步进行csr生成环节1）
     * @param csrInfos subject
     * @param publicKey 公钥
     * @return csr中待签名的部分
     * @throws Exception 异常
     */
    public static byte[] getP10CsrInfoToBeSign(CsrInfos csrInfos, PublicKey publicKey) throws Exception {
        System.out.println("---------------begin get P10 Csr info to be signed---------------");
        X500NameBuilder x500NameBuilder = new X500NameBuilder();
        x500NameBuilder.addRDN(BCStyle.C, csrInfos.getCountry())
                .addRDN(BCStyle.ST, csrInfos.getState())
                .addRDN(BCStyle.L, csrInfos.getLocal())
                .addRDN(BCStyle.O, csrInfos.getOrganization())
                .addRDN(BCStyle.OU, csrInfos.getOrganizationUnit())
                .addRDN(BCStyle.CN, csrInfos.getCommonName())
                .addRDN(BCStyle.EmailAddress, csrInfos.getEmailAddress());
        X500Name subject = x500NameBuilder.build();
        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
        //<editor-fold desc="待签名的CSR数据, 这里没有设置附带属性">
        CertificationRequestInfo certificationRequestInfo = new CertificationRequestInfo(subject, publicKeyInfo, new DERSet());
        //</editor-fold>
        System.out.println("---------------end get P10 Csr info to be signed---------------");
        return certificationRequestInfo.getEncoded();
    }

    /**
     * 将获取到的被私钥签名后的签名值与csr中待签名的数据进行拼装，生成csr（分步进行csr生成环节2）
     * @param sign 私钥签名后的签名值
     * @param csrInfoBytes csr中待签名的数据
     * @return csr
     */
    public static PKCS10CertificationRequest constructP10Csr(byte[] sign, byte[] csrInfoBytes){
        System.out.println("---------------begin construct P10 CSR---------------");
        AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.sha256WithRSAEncryption);
        CertificationRequestInfo certificationRequestInfo = CertificationRequestInfo.getInstance(csrInfoBytes);
        //<editor-fold desc="组装成最终的CSR">
        PKCS10CertificationRequest pkcs10CertificationRequest = new PKCS10CertificationRequest(
                new CertificationRequest(certificationRequestInfo, algorithmIdentifier, new DERBitString(sign)));
        //</editor-fold>
        System.out.println("---------------end construct P10 CSR---------------");
        return pkcs10CertificationRequest;
    }

    /**
     * 将csr对象存入pem格式的文件中
     * @param csr csr对象
     * @param basePath 基本路径
     * @throws Exception 异常
     */
    public static void csr2PemFile(PKCS10CertificationRequest csr, String basePath) throws Exception {
        System.out.println("---------------begin store P10 CSR to pem file---------------");
        String csrPath = basePath + System.currentTimeMillis() + "_csr.csr";
        File csrFile = new File(csrPath);
        csrFile.createNewFile();
        PrintWriter printWriter = new PrintWriter(csrFile);
        PemWriter pemWriter = new PemWriter(printWriter);
        PemObject pemObject = new PemObject("CERTIFICATE REQUEST", csr.getEncoded());
        pemWriter.writeObject(pemObject);
        pemWriter.close();
        System.out.println("---------------end store P10 CSR to pem file---------------");
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
     * 获取证书请求中包含的一些信息
     * @param csr 证书请求对象
     */
    public static void getCsrMsg(PKCS10CertificationRequest csr) {
        System.out.println("---------------begin get csr message---------------");
        SubjectPublicKeyInfo subjectPublicKeyInfo = csr.getSubjectPublicKeyInfo();
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
        System.out.println("CSR subject is: " + x500Name.toString());
        // 通过这个方式，可以获取到x500Name对象中所有的subject信息
        for (RDN rdn: x500Name.getRDNs()){
            // 依据指定的ASN1定义的OID（也就是BCStyle.E）获取对应属性值
            // 这里特别地查看了一下能否特地获取某个值，结果显示可以
            if(rdn.getFirst().getType().equals(BCStyle.C)){
                System.out.println("This is country, value is: " + rdn.getFirst().getValue().toString());
            }
            System.out.println("RDN: " + rdn.getFirst().getType().toString() + "(Type OID) : "
                    + rdn.getFirst().getValue().toString() + "(value)");
        }
        // 依据指定的ASN1定义的OID（也就是BCStyle.E）获取对应属性值
        // 这里是获取csr中subject的EmailAddress属性,这里其实还可以获取其他的值
        RDN[] emailAddress = x500Name.getRDNs(BCStyle.E);
        if (emailAddress != null && emailAddress.length > 0) {
            System.out.println("EmailAddress: " + emailAddress[0].getFirst().getValue().toString());
        }
        System.out.println("---------------end get csr message---------------");
    }

    /**
     * 颁发V1自签名证书
     * @param csr 证书请求
     * @param privateKey 私钥
     * @param validDays 有效时间
     * @return V1的证书
     * @throws Exception 异常
     */
    public static X509Certificate issueSelfSignV1Cert(PKCS10CertificationRequest csr, PrivateKey privateKey,
                                                      long validDays) throws Exception {
        System.out.println("---------------begin issue self sign V1 certificate---------------");
        X509v1CertificateBuilder certBuilder = new X509v1CertificateBuilder(csr.getSubject(), // 颁发者subject
                new BigInteger(160, new SecureRandom()),                       // 序列号，最长是20字节
                new Date(),                                                            // 开始生效时间
                new Date(System.currentTimeMillis() + validDays * 24 * 60 * 60 * 1000), // 过期时间
                csr.getSubject(),                                                       // 使用者subject
                csr.getSubjectPublicKeyInfo());
        AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA256withRSA");
        AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);
        ContentSigner contentSigner = new BcRSAContentSignerBuilder(sigAlgId, digAlgId)
                        .build(PrivateKeyFactory.createKey(privateKey.getEncoded()));
        X509CertificateHolder holder = certBuilder.build(contentSigner);
        X509Certificate cert = new JcaX509CertificateConverter().getCertificate(holder);
        System.out.println("---------------end issue self sign V1 certificate---------------");
        return cert;
    }

    /**
     * 颁发V3无扩展属性证书（仅支持RSA证书颁发）
     * @param csr 证书请求
     * @param issuerCertPath 用来颁发证书的父证书（通常是ca证书, 文件可以是der也可以是pem格式）
     * @param issuerPrvKeyPath 用于颁发证书的父证书对应的私钥
     * @param validDays 新证书的有效时间（单位：天）
     * @return 新的证书对象
     * @throws Exception 异常
     */
    public static X509Certificate issueCert(PKCS10CertificationRequest csr, String issuerCertPath,
                                            String issuerPrvKeyPath, long validDays) throws Exception {
        System.out.println("---------------begin issue v3 cert---------------");
        Key caPrivateKey = null;
        CertificateFactory certFactory;
        X509Certificate caCert = null;
        // csr验签
        if(csr.isSignatureValid(new JcaContentVerifierProviderBuilder().build(csr.getSubjectPublicKeyInfo()))){
            System.out.println("CSR is valid!");
        } else {
            System.out.println("CSR is not valid!");
            throw new Exception();
        }
        certFactory = CertificateFactory.getInstance("X.509");
        // 读取Ca证书
        caCert = (X509Certificate) certFactory.generateCertificate(new FileInputStream(issuerCertPath));
        // 读取私钥文件
        caPrivateKey = loadRSAPKCS1PrivateKey(issuerPrvKeyPath);
        // ca的签名算法标识符
        AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA256withRSA");
        // 摘要算法标识符
        AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);
        assert caCert != null;
        // 颁发者信息
        // 不能直接使用X500Name issuer = new X500Name(caCert.getSubjectX500Principal().getName());来获得颁发者subject
        // X500Principal转字符串的subject的各项数据与正常的相反（从E到C,正常的是C到E）,虽然能被openssl和操作系统识别，但是在进行证书链校验时会失败
        // 如果subject信息顺序反了，则openssl进行证书链校验时的报错信息：unable to get local issuer certificate
        X500Name x500Name = new X500Name(caCert.getSubjectX500Principal().getName());
        String issuerDN = "C=" + x500Name.getRDNs(BCStyle.C)[0].getFirst().getValue().toString()
                + ",ST=" + x500Name.getRDNs(BCStyle.ST)[0].getFirst().getValue().toString()
                + ",L=" + x500Name.getRDNs(BCStyle.L)[0].getFirst().getValue().toString()
                + ",O=" + x500Name.getRDNs(BCStyle.O)[0].getFirst().getValue().toString()
                + ",OU=" + x500Name.getRDNs(BCStyle.OU)[0].getFirst().getValue().toString()
                + ",CN=" + x500Name.getRDNs(BCStyle.CN)[0].getFirst().getValue().toString()
                + ",E="+ x500Name.getRDNs(BCStyle.E)[0].getFirst().getValue().toString();
        X500Name issuer = new X500Name(issuerDN);
        // 序列号, 最长是20字节
        BigInteger serial = new BigInteger(160, new SecureRandom());
        // 证书起始生效时间
        Date from = new Date();
        // 证书失效时间
        Date to = new Date(System.currentTimeMillis() + validDays * 24 * 60 * 60 * 1000);
        // 使用x509来组装证书
        X509v3CertificateBuilder certGen = new X509v3CertificateBuilder(issuer, serial, from, to,
                csr.getSubject(), csr.getSubjectPublicKeyInfo());
        // CA端进行签名, 才有具有法律效力
        X509Certificate cert = null;
        ContentSigner signer = new BcRSAContentSignerBuilder(sigAlgId, digAlgId)
                .build(PrivateKeyFactory.createKey(caPrivateKey.getEncoded()));
        // 生成BC结构的证书
        Security.addProvider(new BouncyCastleProvider());
        cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certGen.build(signer));

        System.out.println("新证书的版本： " + cert.getVersion());
        System.out.println("新证书的subject: " + cert.getSubjectX500Principal().getName());
        System.out.println("新证书的颁发者subject: " + cert.getIssuerDN().getName());
        System.out.println("---------------end issue v3 cert---------------");
        return cert;
    }

    /**
     * 颁发V3证书，同时设置一些扩展字段(扩展字段方面主要是举例说明，实际使用要做修改)（仅支持RSA证书颁发）
     * @param csr 证书请求
     * @param issuerCertPath 用来颁发证书的父证书（通常是ca证书）文件存放绝对路径, 注意，文件内容可以是der也可以是pem格式
     * @param issuerPrvKeyPath 用于颁发证书的父证书对应的私钥的文件存放绝对路径, 注意，文件内容必须是pem格式
     * @param validDays 新证书的有效时间（单位：天）
     * @param isCA true-是CA证书，false-不是
     * @param pathLenConstraint 当isCA=true时有效，表示CA证书底下的证书链的最大长度; 应该>=0
     * @param alo 证书请求对应的密钥的算法
     * @return 新的证书对象
     * @throws Exception 异常
     */
    public static X509Certificate issueAttachExtensionsCert(PKCS10CertificationRequest csr, String issuerCertPath,
                                            String issuerPrvKeyPath, long validDays, boolean isCA,
                                            int pathLenConstraint, String alo) throws Exception {
        System.out.println("---------------begin issue attach extensions cert---------------");
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        // 读取Ca证书
        X509Certificate caCert = (X509Certificate) certFactory.generateCertificate(new FileInputStream(issuerCertPath));
        // 读取私钥文件
        Key caPrivateKey = loadRSAPKCS1PrivateKey(issuerPrvKeyPath);
        // 读取csr的公钥
        PublicKey csrPubKey = Cryptology.getPubKeyFromCsr(csr, alo);
        // csr验签
        if(csr.isSignatureValid(new JcaContentVerifierProviderBuilder().build(csr.getSubjectPublicKeyInfo()))){
            System.out.println("CSR is valid!");
        } else {
            System.out.println("CSR is not valid!");
            throw new Exception();
        }
        // ca的签名算法标识符
        AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA256withRSA");
        // 摘要算法标识符
        AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);
        assert caCert != null;
        // 颁发者信息
        // 不能直接使用X500Name issuer = new X500Name(caCert.getSubjectX500Principal().getName());来获得颁发者subject
        // X500Principal转字符串的subject的各项数据与正常的相反（从E到C,正常的是C到E）,虽然能被openssl和操作系统识别，但是在进行证书链校验时会失败
        // 如果subject信息顺序反了，则openssl进行证书链校验时的报错信息：unable to get local issuer certificate
        X500Name x500Name = new X500Name(caCert.getSubjectX500Principal().getName());
        String issuerDN = "C=" + x500Name.getRDNs(BCStyle.C)[0].getFirst().getValue().toString()
                + ",ST=" + x500Name.getRDNs(BCStyle.ST)[0].getFirst().getValue().toString()
                + ",L=" + x500Name.getRDNs(BCStyle.L)[0].getFirst().getValue().toString()
                + ",O=" + x500Name.getRDNs(BCStyle.O)[0].getFirst().getValue().toString()
                + ",OU=" + x500Name.getRDNs(BCStyle.OU)[0].getFirst().getValue().toString()
                + ",CN=" + x500Name.getRDNs(BCStyle.CN)[0].getFirst().getValue().toString()
                + ",E="+ x500Name.getRDNs(BCStyle.E)[0].getFirst().getValue().toString();
        X500Name issuer = new X500Name(issuerDN);
        // 序列号, 最长是20字节=160bit
        BigInteger serial = new BigInteger(160, new SecureRandom());
        // 证书起始生效时间
        Date from = new Date();
        // 证书失效时间
        Date to = new Date(System.currentTimeMillis() + validDays * 24 * 60 * 60 * 1000);
        // 使用x509来组装证书
        X509v3CertificateBuilder certGen = new X509v3CertificateBuilder(issuer, serial, from, to,
                csr.getSubject(), csr.getSubjectPublicKeyInfo());
        // 获取csr中的扩展项（这里只是举例获取密钥用途和扩展密钥用途，实际还可以获取其他类型的属性）
        Extensions csrExtensions = csr.getRequestedExtensions();
        int csrKeyUsageInt = 0;
        KeyPurposeId[] csrExtendedKeyUsagePurposeIds = null;
        // csr可能根本就不包含扩展项
        if(csrExtensions != null) {
            KeyUsage csrKeyUsage = KeyUsage.fromExtensions(csrExtensions);
            if (csrKeyUsage != null) {
                // keyUsage在ASN1格式中是以”000000000“的置位表示是否开启密钥用途的
                for (byte keyUsage : csrKeyUsage.getBytes()) {
                    csrKeyUsageInt |= keyUsage;
                }
            }
            ExtendedKeyUsage csrExtendedKeyUsage = ExtendedKeyUsage.fromExtensions(csrExtensions);
            csrExtendedKeyUsagePurposeIds = csrExtendedKeyUsage.getUsages();
        }
        // 额外添加一些扩展项
        // 注意：
        //     证书中的每个扩展都被指定为关键或非关键。 如果证书使用系统遇到它无法识别的关键扩展或包含无法处理的信息的关键扩展，则必须拒绝证书。
        //     如果无法识别非关键扩展，则可以忽略该扩展，但如果可以识别，则必须对其进行处理。
        //     更具体的注意项需要查阅RFC5280标准文档的定义
        ExtensionsGenerator extensionsGenerator = new ExtensionsGenerator();
        // addExtension 第一个字段表示：扩展字段的类型；第二个参数表示：是否要将本字段设置为critical；第三个参数表示：扩展字段的值
        // BasicConstraints(boolean)的true和false 表示是否要将本证书设置成CA证书。此时默认无证书链长度限制
        // 直接传证书链长度的话，是直接设置成CA证书
        if(isCA){
            if(pathLenConstraint < 0){
                throw new Exception("CA chain length error");
            }
            extensionsGenerator.addExtension(Extension.basicConstraints, false, new BasicConstraints(pathLenConstraint));
        } else {
            extensionsGenerator.addExtension(Extension.basicConstraints, false, new BasicConstraints(false));
        }
        certGen.addExtension(extensionsGenerator.getExtension(Extension.basicConstraints));
        // 这里是尝试性地添加了一个密钥用途的扩展字段，具体是——数字签名。
        // KeyUsage里有很多可选用的密钥用途可以用来设置
        extensionsGenerator.addExtension(Extension.keyUsage, false,
                new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment | csrKeyUsageInt));
        certGen.addExtension(extensionsGenerator.getExtension(Extension.keyUsage));
        // 添加使用者密钥标识
        JcaX509ExtensionUtils jcaX509ExtensionUtils = new JcaX509ExtensionUtils();
        extensionsGenerator.addExtension(Extension.subjectKeyIdentifier, false,
                jcaX509ExtensionUtils.createSubjectKeyIdentifier(csrPubKey));
        certGen.addExtension(extensionsGenerator.getExtension(Extension.subjectKeyIdentifier));
        // 添加颁发者密钥标识
        extensionsGenerator.addExtension(Extension.authorityKeyIdentifier, false,
                jcaX509ExtensionUtils.createAuthorityKeyIdentifier(caCert.getPublicKey()));
        certGen.addExtension(extensionsGenerator.getExtension(Extension.authorityKeyIdentifier));
        // 这里是尝试性随便添加了几个扩展密钥用途的扩展字段
        // KeyPurposeId中有很多可以直接拿来使用
        // 如果要自定义用途，则只能自定义最后一位，我这边用"666"来表示。对应的UID是：2.5.29.37.666,前三位表示的是extendedKeyUsage
        ASN1EncodableVector purposes = new ASN1EncodableVector();
        purposes.add(KeyPurposeId.id_kp_emailProtection);
        purposes.add(Extension.extendedKeyUsage.branch("666"));
        if (csrExtendedKeyUsagePurposeIds != null) {
            purposes.addAll(csrExtendedKeyUsagePurposeIds);
        }
        extensionsGenerator.addExtension(Extension.extendedKeyUsage, true, new DERSequence(purposes));
        certGen.addExtension(extensionsGenerator.getExtension(Extension.extendedKeyUsage));
        // 添加拥有者UID
        // certGen.setSubjectUniqueID(uids);
        // 添加颁发者UID
        // certGen.setIssuerUniqueID(issuerUids);
        // CA端进行签名, 才有具有法律效力
        X509Certificate cert = null;
        ContentSigner signer = new BcRSAContentSignerBuilder(sigAlgId, digAlgId)
                .build(PrivateKeyFactory.createKey(caPrivateKey.getEncoded()));
        // 生成BC结构的证书
        Security.addProvider(new BouncyCastleProvider());
        cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certGen.build(signer));

        System.out.println("新证书的版本： " + cert.getVersion());
        System.out.println("新证书的subject: " + cert.getSubjectX500Principal().getName());
        System.out.println("新证书的颁发者subject: " + cert.getIssuerDN().getName());
        System.out.println("---------------end issue attach extensions cert---------------");
        return cert;
    }

    /**
     * 将一个证书对象以PEM格式存入对应路径的文件中
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
    }

    /**
     * 从证书中读取出公钥（含加载证书, 可以是der也可以是pem格式的证书）
     * @param path 证书文件路径
     * @return 公钥
     * @throws Exception 异常
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
     * 读取证书文件并加载成一个证书对象（复杂式写法, 简单写法看方法getPubKeyFromCert）<br/>
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
            // （注：这种方式比较暴力，在数据含有证书内容信息时会出错，最好用PemParser处理）
            String crtStr = new String(bytes, StandardCharsets.UTF_8);
            // 为避免不同平台的回车换行问题，所以这里只匹配开头的文件类型描述
            crtStr = crtStr.replace("-----BEGIN CERTIFICATE-----", "");
            // 为避免不同平台的回车换行问题，所以这里只匹配结尾的文件类型描述
            crtStr = crtStr.replace("-----END CERTIFICATE-----", "");
            // 去掉换行
            crtStr = crtStr.replaceAll("\n", "");
            // 去掉某些平台带有的回车
            crtStr = crtStr.replaceAll("\r", "");
            x509Certificate = new X509CertImpl(Base64.getDecoder().decode(crtStr));
        } else { // der格式
            x509Certificate = new X509CertImpl(bytes);
        }
        System.out.println("Cert subject: " + x509Certificate.getSubjectX500Principal().getName());
        System.out.println("---------------end load CERT from file(" + certEncodeType + ")---------------");
        return x509Certificate;
    }

    /**
     * 验证子证书是否由父证书颁发(这里其实只做了密钥签名验证，未用sn之类的数据验证)
     * @param subCert 子证书
     * @param rootCert 父证书
     * @return true-是，false-不是
     */
    public static boolean validCertChain(X509Certificate subCert, X509Certificate rootCert){
        boolean isOK;
        try {
            subCert.verify(rootCert.getPublicKey());
            isOK = true;
        } catch (Exception e){
            isOK = false;
        }
        System.out.println("SubCert is issued by rootCert ? " + isOK);
        return isOK;
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
    }

    /**
     * 从DER格式（为方便传输，一般der格式内容会转为十六进制字符串来传递）转成对象
     * @param der 一个Hex-String形式的der格式数据内容
     * @param type 数据类型，支持“CSR","CERT","PRV_KEY","PUB_KEY"
     * @param keyAlo 密钥算法，支持“RSA"，若不是密钥时可为null
     * @throws Exception 异常
     */
    public static void der2Object(String der, String type, String keyAlo) throws Exception {
        System.out.println("---------------begin (" + type + ") DER to Object---------------");
        switch (type){
            case "CSR":
                PKCS10CertificationRequest pkcs10Csr = new PKCS10CertificationRequest(Hex.decode(der));
                System.out.println("Certificate Request: " + pkcs10Csr.getSubject());
                break;
            case "CERT":
                X509Certificate cert = new X509CertImpl(Hex.decode(der));
                System.out.println("Certificate: " + cert.getSubjectDN());
                break;
            case "PRV_KEY":
                PKCS8EncodedKeySpec prvKeySpec = new PKCS8EncodedKeySpec(Hex.decode(der));
                KeyFactory factory = KeyFactory.getInstance(keyAlo, BouncyCastleProvider.PROVIDER_NAME);
                PrivateKey privateKey = factory.generatePrivate(prvKeySpec);
                System.out.println("Private Key: " + privateKey.getAlgorithm());
                break;
            default:
                X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(Hex.decode(der));
                KeyFactory factory2 = KeyFactory.getInstance(keyAlo, BouncyCastleProvider.PROVIDER_NAME);
                PublicKey publicKey = factory2.generatePublic(pubKeySpec);
                System.out.println("Public Key: " + publicKey.getAlgorithm());
        }
        System.out.println("---------------end (" + type + ") DER to Object---------------");
    }

    /**
     * 从PEM格式（为方便传输，一般der格式内容会转为十六进制字符串来传递）转成对象
     * @param pem 一个pem格式的内容
     * @param type 数据类型，支持“CSR","CERT","PRV_KEY","PUB_KEY"
     * @param keyAlo 密钥算法，支持“RSA",若不是密钥时可为null
     * @throws Exception 异常
     */
    public static void pem2Object(String pem, String type, String keyAlo) throws Exception {
        System.out.println("---------------begin (" + type + ") Pem to Object---------------");
        PEMParser pemParser = new PEMParser(new StringReader(pem));
        PemObject pemObject = pemParser.readPemObject();
        byte[] content = pemObject.getContent();
        String der = Hex.toHexString(content);
        der2Object(der, type, keyAlo);
        System.out.println("---------------end (" + type + ") Pem to Object---------------");
    }

    /**
     * 将传进来的pem格式证书字符串转为der格式（Hex-String）（注：这种方式比较暴力，在数据含有证书内容信息时会出错，最好用PemParser处理）
     * @param crtStr pem格式证书字符串
     * @return der格式（Hex-String）证书
     */
    public static String certPem2DerHexStr(String crtStr) {
        System.out.println("---------------begin CERT PEM to DER---------------");
        // 为避免不同平台的回车换行问题，所以这里只匹配开头的文件类型描述
        crtStr = crtStr.replace("-----BEGIN CERTIFICATE-----", "");
        // 为避免不同平台的回车换行问题，所以这里只匹配结尾的文件类型描述
        crtStr = crtStr.replace("-----END CERTIFICATE-----", "");
        // 去掉换行
        crtStr = crtStr.replaceAll("\n", "");
        // 去掉某些平台带有的回车
        crtStr = crtStr.replaceAll("\r", "");
        // 获得der格式编码
        String certDER = Hex.toHexString(Base64.getDecoder().decode(crtStr));
        System.out.println("CERT in der(Hex-String): " + certDER);
        System.out.println("---------------end CERT PEM to DER---------------");
        return certDER;
    }

    /**
     * 依据传入的证书对象，获取证书的所有信息
     * @param x509Certificate 证书对象
     * @throws Exception 异常
     */
    public static void getCertMsg(X509Certificate x509Certificate) throws Exception{
        System.out.println("---------------begin get certificate message---------------");
        // 证书版本，现在一般用的是v1或者v3
        System.out.println("Cert version is: " + x509Certificate.getVersion());
        // 证书的序列号，通常是以十六进制的形式展示
        System.out.println("Cert serial number: " + x509Certificate.getSerialNumber().toString(16));
        // 签名算法
        System.out.println("Cert sign algorithm is: " + x509Certificate.getSigAlgName());
        // 证书签名值
        System.out.println("Cert sign value is: " + Hex.toHexString(x509Certificate.getSignature()));
        // 颁发者的subject各个信息获取方式同自身，所以这里不做重复
        sun.security.x509.X500Name issuerX500Name = sun.security.x509.X500Name.asX500Name(x509Certificate.getIssuerX500Principal());
        System.out.println("Issuer is: " + issuerX500Name.toString());
        // 证书有效期
        Date notBefore = x509Certificate.getNotBefore();
        Date notAfter = x509Certificate.getNotAfter();
        System.out.println("Cert valid time is from " + notBefore.toString() + " to " + notAfter.toString());
        // 检查证书的有效期
        try {
            x509Certificate.checkValidity();
            System.out.println("Certificate is in valid time range.");
        } catch (CertificateExpiredException e) {
            System.out.println("Certificate is expired.");
        } catch (CertificateNotYetValidException e) {
            System.out.println("Certificate is not yet valid.");
        }
        // 证书拥有者信息
        System.out.println("Subject: " + x509Certificate.getSubjectX500Principal().getName());
        System.out.println("Issuer subject: " + x509Certificate.getIssuerX500Principal().getName());
        sun.security.x509.X500Name x500Name = sun.security.x509.X500Name.asX500Name(x509Certificate.getSubjectX500Principal());
        System.out.println("Common Name(CN): " + x500Name.getCommonName());
        System.out.println("Country(C): " + x500Name.getCountry());
        System.out.println("State(S): " + x500Name.getState());
        System.out.println("Locality(L): " + x500Name.getLocality());
        System.out.println("Organization(O): " + x500Name.getOrganization());
        System.out.println("Organization Unit(OU): " + x500Name.getOrganizationalUnit());
        // ava[]这里面包含了所有subject信息，也可以通过这里一个一个获取信息。这里只是举例获取emailAddress
        for (AVA ava: x500Name.allAvas()){
            if(ava.getObjectIdentifier().toString().equals(BCStyle.EmailAddress.toString())){
                System.out.println("EmailAddress(E): " + ava.getValueString());
            }
        }
        // 获取基本约束字段（ X509v3 Basic Constraints），主要就是看是不是CA证书
        // 方法介绍：从关键的 BasicConstraints扩展（OID = 2.5.29.19）获取证书约束路径长度
        int allowChainLength = x509Certificate.getBasicConstraints();
        if(allowChainLength == -1){
            System.out.println("This is not a CA certificate or do not have this extension");
        } else if(allowChainLength == Integer.MAX_VALUE){
            System.out.println("This is a CA certificate and do not limit the issue certificate chain length");
        } else {
            System.out.println("This is a CA certificate and limit the issue certificate chain length max = " + allowChainLength);
        }
        // 获取密钥用途（X509v3 Key Usage）
        boolean[] keyUsage = x509Certificate.getKeyUsage();
        if(keyUsage == null){
            System.out.println("This certificate do not have key usage message");
        } else {
            System.out.print("The key usage is: ");
            for (int i = 0; i < keyUsage.length; i++) {
                if(keyUsage[i]) {
                    switch (i) {
                        case 0:
                            System.out.print("Digital Signature, ");
                            break;
                        case 1:
                            System.out.print("Non Repudiation, ");
                            break;
                        case 2:
                            System.out.print("Key Encipherment, ");
                            break;
                        case 3:
                            System.out.print("Data Encipherment, ");
                            break;
                        case 4:
                            System.out.print("Key Agreement, ");
                            break;
                        case 5:
                            System.out.print("Key CertSign, ");
                            break;
                        case 6:
                            System.out.print("cRLSign, ");
                            break;
                        case 7:
                            System.out.print("EncipherOnly, ");
                            break;
                        case 8:
                            System.out.print("DecipherOnly, ");
                            break;
                    }
                }
            }
            System.out.println();
        }
        // 注: RFC5280（4.1.2.8节）说明：issuerUniquelID和subjectUniqueID仅当版本为 2 或 3 时，这些字段必须显示。 如果版本为 1，
        // 则不得显示这些字段。 证书中存在使用者和颁发者唯一标识符，以处理随着时间的推移重用使用者和/或颁发者名称的可能性。 此配置文件建议不要
        // 对不同的实体重复使用名称，并且 Internet 证书不要使用唯一标识符。 符合此配置文件的 CA 不得生成具有唯一标识符的证书。 符合此配置
        // 文件的应用程序应该能够分析包含唯一标识符的证书，但没有与唯一标识符关联的处理要求
        // todo 获取自身UID，未能颁发出含这个属性的证书，无法校验
        boolean[] subjectUniqueID = x509Certificate.getSubjectUniqueID();
        if(subjectUniqueID == null){
            System.out.println("This certificate do not have subject unique id message");
        } else {
            System.out.print("The subject unique id is: ");
            for (boolean b : subjectUniqueID) {
                System.out.print(b ? 1 : 0);
            }
            System.out.println();
        }
        // todo 获取颁发者UID，未能颁发出含这个属性的证书，无法校验
        boolean[] issuerUniqueID = x509Certificate.getIssuerUniqueID();
        if(issuerUniqueID == null){
            System.out.println("This certificate do not have issuer unique id message");
        } else {
            System.out.print("The issuer unique id is: ");
            for (boolean b : issuerUniqueID) {
                System.out.print(b ? 1 : 0);
            }
            System.out.println();
        }
        // todo 获取自身的认证标识（X509v3 Subject Key Identifier）, 暂时没找到怎么获取
        // 获取扩展密钥用途（X509v3 Extended Key Usage）
        List<String> extendedKeyUsage = x509Certificate.getExtendedKeyUsage();
        if(extendedKeyUsage == null){
            System.out.println("This certificate do not contain extended key usage");
        } else {
            System.out.println("This certificate extended key usage is: " + extendedKeyUsage);
        }
        // 获取证书中被签名部分的原文
        System.out.println("To be signed certificate content is(Hex-String): " + Hex.toHexString(x509Certificate.getTBSCertificate()));
        // 从SubjectAltName扩展 (OID = 2.5.29.17) 获取不可变的主题备用名称集合
        // x509Certificate.getSubjectAlternativeNames();
        System.out.println("---------------end get certificate message---------------");
    }

    /**
     * 加密（对长度有要求）<br/>
     * RSA算法原理上，其实要求的明文和密文都是： 0<明文或密文大小<密钥的模大小，其实也对应了我们常说的“0<明文或密文的长度<密钥的模的长度”(长度相等时要额外多一步比较大小)，
     * 实际上一些加密工具之类的会对明文长度为0的时候进行特殊处理，
     * 另外，虽然最大可被加密的明文长度是密钥的长度，但是具体的实现工具或规范(如PKCS规范)都有进行限制（特别是当有填充时）。
     * 所以具体情况要看你用的工具/库的限制是怎样的。
     * 密文长度也要看工具/库，通常情况下都是模的长度，
     * <br/>
     * Java 默认的 RSA加密实现不允许明文长度超过密钥长度(单位是字节，也就是 byte)。
     * 也就是说，如果我们定义的密钥(如：java.security.KeyPairGenerator.initialize(int keySize) 来定义密钥长度)长度为 1024(单位是位，也就是 bit)。
     * 生成的密钥长度就是 1024位 /（8位/字节） = 128字节，那么我们需要加密的明文长度不能超过 128字节。
     * 也就是说，我们最大能将 128 字节长度的明文进行加密，否则会抛异常<br/>
     * 实测“RSA/ECB/PKCS1Padding”时，2048RSA是最大245，其实PKCS1Padding这种填充方式本来就是要占用至少11的长度，也就是明文只能是256-11=245了。
     * <br/>
     * BC库的话，也是密钥长度， 如128，256.超过的话，报错：org.bouncycastle.crypto.DataLengthException: input too large for RSA cipher。
     * 但是，上面BC库时所说的，都是NoPadding的情况，如果有Padding，则blockSize大小就不一样。
     * 具体限制可见org.bouncycastle.crypto.AsymmetricBlockCipher的各个实现类的getInputBlockSize方法
     * <br/>
     * 正常情况下，NoPadding占用大小是0，也就是你提供的明文可以是当前密钥最大的支持长度。PKCS1Padding占用的位数大小是11，也就是明文长度只能
     * 是当前密钥最大支持长度-11
     * <p>
     * 关于填充：<br/>
     * OAEP is less vulnerable to padding oracle attacks than PKCS#1 v1.5 padding. GCM is also protected against padding oracle attacks。<br/>
     * 其实ECB是对称密码的一种分组模式，对非对称密钥没啥用，其实用None就行，例如RSA/None/PKCS1Padding，像RSA加密是不对数据进行分组的。<br/>
     * 但是，实测发现，sun库要求得带ECB，None的话，得用BC库才支持<br/>
     * </p>
     * 注意：如果是ECC，则只能用公钥加密，不支持私钥加密数据 <br/>
     * 注意：ECC本身并没有真正定义任何加密/解密操作，构建在椭圆曲线上的算法确实如此（todo 待确认） <br/>
     * @param key 用来加密的密钥
     * @param alo 密钥对应的算法/模式/填充模式，支持”RSA“（这么写的话，模式和填充模式依赖于算法提供者怎么设置默认值,BC库的话等同于“RSA/ECB/NoPadding”）、
     *            “RSA/ECB/PKCS1Padding”、”RSA/ECB/OAEPWithSHA-1AndMGF1Padding“、”RSA/ECB/OAEPWithSHA-256AndMGF1Padding“、
     *            “ECIES"(ECIES表示ECC)。
     * @param data 等待被加密的数据，数据不能太长
     * @param provider 指定算法提供者，支持“BC”、“SunJCE”、null(null表示由系统自动选择匹配的算法提供者)
     * @return 密文数据
     * @throws Exception 异常
     */
    public static byte[] encryptData(Key key, String alo, byte[] data, String provider) throws Exception{
        System.out.println("---------------begin encrypt data---------------");
        System.out.println("alo is: " + alo);
        System.out.println("data length is: " + data.length);
        Cipher cipher;
        if(provider == null){
            // 一旦类有引入BC provider，则即便这里没有指定使用BC库，一旦使用的算法Sun原生库不支持，则会自动调用BC库
            cipher = Cipher.getInstance(alo);
        } else if(provider.equals(BouncyCastleProvider.PROVIDER_NAME) || provider.equals("SunJCE")){
            // 输入“BC”表示强制使用BC库，输入“SunJCE”表示强制使用SunJCE
            cipher = Cipher.getInstance(alo, provider);
        } else {
            System.out.println("-----------No such provider: " + provider + "!!!-----------------");
            throw new Exception();
        }
        System.out.println("Provider: " + cipher.getProvider());
        // 使用默认的随机数生成器（未指定第三个参数）
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] res = cipher.doFinal(data);
        System.out.println("Hex String type encrypted result data is: " + Hex.toHexString(res));
        System.out.println("Base64 String type encrypted result data is: " + Base64.getEncoder().encodeToString(res));
        System.out.println("---------------end encrypt data---------------");
        return res;
    }

    /**
     * 解密（对长度有要求，例如2048长度的RSA只能解密256长度的数据，1024的RSA只能解密128长度的数据）<br/>
     * <p>
     *     BC库的输出大小具体可见org.bouncycastle.crypto.AsymmetricBlockCipher的各个实现类的getOutputBlockSize方法
     * </p>
     * 注意：如果是ECC，则只能用私钥解密，不支持公钥解密数据<br/>
     * 注意：ECC本身并没有真正定义任何加密/解密操作，构建在椭圆曲线上的算法确实如此（todo 待确认）<br/>
     * @param key 用来解密的密钥
     * @param alo 密钥对应的算法/模式/填充模式，要和加密时使用的配置一致，
     *            支持”RSA“（这么写的话，模式和填充模式依赖于算法提供者怎么设置默认值,BC库的话等同于“RSA/ECB/NoPadding”）、
     *            “RSA/ECB/PKCS1Padding”、”RSA/ECB/OAEPWithSHA-1AndMGF1Padding“、”RSA/ECB/OAEPWithSHA-256AndMGF1Padding“、
     *            “ECIES"(ECIES表示ECC)
     * @param encData 等待被解密的密文数据
     * @param provider 指定算法提供者，支持“BC”、“SunJCE”、null(null表示由系统自动选择匹配的算法提供者)
     * @return 明文数据
     * @throws Exception 异常
     */
    public static byte[] decryptData(Key key, String alo, byte[] encData, String provider) throws Exception{
        System.out.println("---------------begin decrypt data---------------");
        System.out.println("alo is: " + alo);
        System.out.println("encrypted data length is: " + encData.length);
        Cipher cipher;
        if(provider == null){
            // 一旦类有引入BC provider，则即便这里没有指定使用BC库，一旦使用的算法Sun原生库不支持，则会自动调用BC库
            cipher = Cipher.getInstance(alo);
        } else if(provider.equals(BouncyCastleProvider.PROVIDER_NAME) || provider.equals(new SunJCE().getName())){
            // 输入“BC”表示强制使用BC库，输入“SunJCE”表示强制使用SunJCE
            cipher = Cipher.getInstance(alo, provider);
        } else {
            System.out.println("-----------No such provider: " + provider + "!!!-----------------");
            throw new Exception();
        }
        cipher.init(Cipher.DECRYPT_MODE, key);
        System.out.println("Provider: " + cipher.getProvider());
        byte[] res = cipher.doFinal(encData);
        System.out.println("Hex String type decrypt result data is: " + Hex.toHexString(res));
        System.out.println("Base64 String type decrypt result data is: " + Base64.getEncoder().encodeToString(res));
        System.out.println("---------------end decrypt data---------------");
        return res;
    }

    /**
     * 分段加密（仅支持RSA）<br/>
     * 更多内容，详见encryptData方法的说明<br/>
     * <br/>
     * 另外，每次加密生成密文的长度等于密钥长度，如1024bit的密钥，加密一次出来的密文长度是128字节（byte），
     * 所以分段加密的最终的密文长度，必然是密钥长度的正整数倍（如：256字节、512字节）<br/>
     * @param key 用于加密的密钥
     * @param data 待加密的数据
     * @param alo 密钥对应的算法/模式/填充模式，支持”RSA“（这么写的话，模式和填充模式依赖于算法提供者怎么设置默认值,BC库的话等同于“RSA/ECB/NoPadding”）、
     *      “RSA/ECB/PKCS1Padding”、”RSA/ECB/OAEPWithSHA-1AndMGF1Padding“、”RSA/ECB/OAEPWithSHA-256AndMGF1Padding“)
     * @param blockSize 分段的块的大小，如果密钥大小是2048，NoPadding时最大的块是256，如果是1024则最大的块是128。否则会报错。
     * @param provider 指定算法提供者，支持“BC”、“SunJCE”、null(null表示由系统自动选择匹配的算法提供者)
     * @return 加密后的密文
     * @throws Exception 异常
     */
    public static byte[] rsaBlockEncrypt(byte[] data, String alo, Key key, int blockSize, String provider) throws Exception {
        System.out.println("---------------begin rsa block encrypt data---------------");
        System.out.println("Key algorithm is: " + key.getAlgorithm());
        Cipher cipher;
        if(provider == null){
            // 一旦类有引入BC provider，则即便这里没有指定使用BC库，一旦使用的算法Sun原生库不支持，则会自动调用BC库
            cipher = Cipher.getInstance(alo);
        } else if(provider.equals(BouncyCastleProvider.PROVIDER_NAME) || provider.equals(new SunJCE().getName())){
            // 输入“BC”表示强制使用BC库，输入“SunJCE”表示强制使用SunJCE
            cipher = Cipher.getInstance(alo, provider);
        } else {
            System.out.println("-----------No such provider: " + provider + "!!!-----------------");
            throw new Exception();
        }
        // 使用加密模式，并传入密钥
        cipher.init(Cipher.ENCRYPT_MODE, key);
        System.out.println("Provider: " + cipher.getProvider());
        // 获取Cipher块的大小（以字节为单位），如果基础算法不是块 cipher，则返回 0
        // 如果用的是java原生的，则是0
        // 如果用的是BC库，则2048的RSA默认是255, 1024的RSA默认是127。所以这时候其实也可以就直接使用这个默认值。如：int blockSize = cipher.getBlockSize()
        // BC库，使用“RSA/ECB/PKCS1Padding”，则2048RSA默认是245
        System.out.println("Cipher default block size: " + cipher.getBlockSize());
        // 在给定了输入长度 inputLen（以字节为单位）的情况下，返回用于保存下一个 update 或 doFinal 操作结果所需的输出缓冲区长度的字节数。
        // 此调用还考虑到来自上一个 update 调用的未处理（已缓存）数据和填充。
        // 下一个 update 或 doFinal 调用的实际输出长度可能小于此方法返回的长度。
        // 2048的RSA则对应的是256, 1024的RSA对应的是128
        System.out.println("Cipher output size: " + cipher.getOutputSize(cipher.getBlockSize()));
        // 避免等下陷入死循环等问题
        if(blockSize < 1){
            throw new Exception("Block size can't be less than 1!");
        }
        // 分段加密
        int inputLen = data.length;
        // 偏移量
        int offLen = 0;
        // 暂存结果
        ByteArrayOutputStream bops = new ByteArrayOutputStream();
        // 开始循环加密
        while(inputLen - offLen > 0) {
            byte [] cache;
            if(inputLen - offLen > blockSize) {
                cache = cipher.doFinal(data, offLen, blockSize);
            } else {
                cache = cipher.doFinal(data, offLen,inputLen - offLen);
            }
            bops.write(cache);
            offLen += blockSize;
        }
        bops.close();
        // 获得加密结果
        byte[] encryptedData = bops.toByteArray();
        System.out.println("Encrypted data in hex format: " + Hex.toHexString(encryptedData));
        System.out.println("---------------end rsa block encrypt data---------------");
        return encryptedData;
    }

    /**
     * 分段解密（仅支持RSA）<br/>
     * 更多内容，详见encryptData方法的说明<br/>
     * @param encData 待解密的密文
     * @param alo 密钥对应的算法/模式/填充模式，支持”RSA“（这么写的话，模式和填充模式依赖于算法提供者怎么设置默认值,BC库的话等同于“RSA/ECB/NoPadding”）、
     *       “RSA/ECB/PKCS1Padding”、”RSA/ECB/OAEPWithSHA-1AndMGF1Padding“、”RSA/ECB/OAEPWithSHA-256AndMGF1Padding“)
     *       要求必须与加密时使用的一致，否则解密结果会与原文不匹配
     * @param key 与加密密钥相对应的解密的密钥
     * @param blockSize 解密分块的大小，如果密钥大小是2048则块大小是256，如果是1024则最大的块是128。
     *                  此处的256和128其实对应的就是加密时每段被加密后的密文的大小。
     *                  如果是用BC库，若输入的size不是解密分块大小，则不会报错，但是解密结果与原文对不上。
     *                  如果是用JAVA库，若输入的size不是解密分块大小，则doFinal会报错javax.crypto.BadPaddingException: Decryption error。
     * @param provider 指定算法提供者，支持“BC”、“SunJCE”、null(null表示由系统自动选择匹配的算法提供者)
     * @return 解密后的明文
     * @throws Exception 异常
     */
    public static byte[] rsaBlockDecrypt(byte[] encData, String alo, Key key, int blockSize, String provider) throws Exception {
        System.out.println("---------------begin rsa block decrypt data---------------");
        System.out.println("Key algorithm is: " + key.getAlgorithm());
        Cipher cipher;
        if(provider == null){
            // 一旦类有引入BC provider，则即便这里没有指定使用BC库，一旦使用的算法Sun原生库不支持，则会自动调用BC库
            cipher = Cipher.getInstance(alo);
        } else if(provider.equals(BouncyCastleProvider.PROVIDER_NAME) || provider.equals(new SunJCE().getName())){
            // 输入“BC”表示强制使用BC库，输入“SunJCE”表示强制使用SunJCE库
            cipher = Cipher.getInstance(alo, provider);
        } else {
            System.out.println("-----------No such provider: " + provider + "!!!-----------------");
            throw new Exception();
        }
        cipher.init(Cipher.DECRYPT_MODE, key);
        System.out.println("Provider: " + cipher.getProvider());
        int inputLen = encData.length;
        // 避免等下陷入死循环等问题
        if(blockSize < 1){
            throw new Exception("Block size can't be less than 1!");
        } else if(inputLen % blockSize != 0){ // 正常被RSA加密后的密文，肯定是单次加密结果的整数倍，也就是解密时要分块的整数倍
            throw new Exception("Data length is error!");
        }
        // 偏移量
        int offLen = 0;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while(inputLen - offLen > 0){
            byte[] cache;
            if(inputLen - offLen > blockSize){
                cache = cipher.doFinal(encData, offLen, blockSize);
            } else{
                cache = cipher.doFinal(encData, offLen,inputLen - offLen);
            }
            byteArrayOutputStream.write(cache);
            offLen += blockSize;
        }
        byteArrayOutputStream.close();
        byte[] plainData = byteArrayOutputStream.toByteArray();
        System.out.println("Plain data in hex format: " + Hex.toHexString(plainData));
        System.out.println("---------------end rsa block decrypt data---------------");
        return plainData;
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
            signatureValue = encryptData(prvKey, keyAlo + "/ECB/PKCS1Padding", Hex.decode(waitForEncryptData),
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

    /**
     * 验证一个公钥和一个私钥是否匹配，一般就是加密一个数据，然后解密，看看解密后的结果与原始数据是否一致。 <br/>
     * 否则就像这样利用算法原理，去验证RSA公私钥是否匹配
     * @param publicKey 公钥
     * @param privateKey 私钥
     * @return true-匹配，false-不匹配
     * @throws Exception 异常
     */
    public static boolean validRSAKeyPairMatch(PublicKey publicKey, PrivateKey privateKey) throws Exception {
        // 必须把私钥转成BC库里的RSA私钥对象，才是PKCS1标准的私钥形式，此时才能依据私钥获取一些理论算法中提及的各种密钥参数
        RSAPrivateKey rsaPrivateKey = prvKey2BCRSAPrvKey(privateKey);
        // 从私钥中获取公钥的指数
        BigInteger prvE = rsaPrivateKey.getPublicExponent();
        // 从私钥中获取密钥对共用的模数
        BigInteger prvM = rsaPrivateKey.getModulus();
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        // 把公钥加载成RSA公钥spec对象，以此获取公钥的指数
        RSAPublicKeySpec pubKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
        // 依据公钥获取公钥的指数
        BigInteger pubE = pubKeySpec.getPublicExponent();
        // 依据公钥获取密钥对共用的模数
        BigInteger pubM = pubKeySpec.getModulus();
        // 如果 利用私钥推算出的公钥的参数 和 公钥自身的参数 二者是一致的，说明这个公钥和这个私钥是匹配的，是一对的
        return prvE != null && prvM != null && prvE.equals(pubE) && prvM.equals(pubM);
    }

    // 验证一个私钥与一本证书是否匹配，其实就是先从证书中提取公钥，然后就是和上一个一样的流程（目前没有找到更便捷的流程）

    /**
     * 生成p12文件到指定文件夹中<br/>
     * 这种方式生成的p12文件等同于在openssl1.1.1版本中生成的p12文件<br/>
     * 在openssl3.0处进行解析验证时，会出现无法正常显示证书的问题<br/>
     * 因为使用的加密方式是比较旧的RC2-40-CBC，该加密方式已被认为是不安全的，于是openssl在3.0中进行了剔除，3.0之前版本的openssl可以照常解析<br/>
     * @param rootCert 根证书(可为null)
     * @param rootCertAlias 根证书别名(可为null)
     * @param subCert 子证书
     * @param prvKeyAndSubCertAlias 私钥及子证书的别名
     * @param subKey 子证书对应的私钥
     * @param keyPwd 保护私钥的密码
     * @param p12Pwd 生成的p12文件的口令
     * @param outputFolderPath 指定的p12文件输出文件夹
     * @throws Exception 异常
     */
    public static void generateP12(java.security.cert.Certificate rootCert, String rootCertAlias,
                                   java.security.cert.Certificate subCert,
                                   String prvKeyAndSubCertAlias, PrivateKey subKey, String keyPwd,
                                   String p12Pwd, String outputFolderPath) throws Exception {
        System.out.println("---------------begin generate P12---------------");
        System.out.println("keyStore default type is: " + KeyStore.getDefaultType());
        // 此处设置存储的格式为pkcs12,实际上KeyStore还支持JKS(默认)
        // 用F4看KeyStore源码可以看到介绍和示例
        KeyStore keyStore = KeyStore.getInstance("PKCS12",  new BouncyCastleProvider());
        keyStore.load(null, null);
        java.security.cert.Certificate[] certificates = new java.security.cert.Certificate[1];
        certificates[0] = subCert;
        // 将给定的密钥分配给给定的别名，并使用给定的密码对其进行保护。
        // 如果给定的密钥类型为 java.security.PrivateKey，它必须附带一个证书链来验证相应的公钥。
        // 如果给定的别名已存在，则与其关联的密钥库信息将被给定的密钥（可能还有证书链）覆盖
        // 此处用来校验的证书的certificates的数组第一个元素（即一个证书对象），会被用别名来保存进p12，所以推荐只放一个私钥对应的证书
        // 别名是否区分大小写取决于实现。为避免出现问题，建议不要在密钥库中使用仅在大小写上不同的别名
        // 这里设置的keyPwd是保护私钥的密码，不是keyStore的
        keyStore.setKeyEntry(prvKeyAndSubCertAlias, subKey, keyPwd.toCharArray(), certificates);
        // 如果准备在p12中放证书链，则可以把ca证书放进去
        if(rootCert != null || StringUtils.isNotBlank(rootCertAlias)){
            keyStore.setCertificateEntry(rootCertAlias, rootCert);
        }
        // 将生成的p12输出到一个文件中
        keyStore.store(new FileOutputStream(outputFolderPath + System.currentTimeMillis() + ".p12"), p12Pwd.toCharArray());
        System.out.println("---------------end generate P12---------------");
        // 验证
        if(rootCert != null || StringUtils.isNotBlank(rootCertAlias)){
            Cryptology.getCertMsg((X509Certificate) keyStore.getCertificate(rootCertAlias));
        }
        Cryptology.getCertMsg((X509Certificate) keyStore.getCertificate(prvKeyAndSubCertAlias));
    }

    /**
     * 从文件中加载p12,并试着解析并获取其中的数据
     * @param path p12存储的绝对路径
     * @param p12Pwd p12的保护口令
     * @param prvKeyAlias 私钥的别名
     * @param keyPwd 保护私钥的口令
     * @param subCertAlias 子证书的别名
     * @param rootCertAlias 根证书的别名（如果没有则输入为null）
     * @throws Exception 异常
     */
    public static void loadAndParseP12(String path, String p12Pwd, String prvKeyAlias, String keyPwd, String subCertAlias,
                                String rootCertAlias) throws Exception{
        System.out.println("---------------begin parse P12---------------");
        File file = new File(path);
        if (!file.exists()){
            throw new FileNotFoundException();
        }
        InputStream inputStream = new FileInputStream(file);
        KeyStore keyStore = KeyStore.getInstance("PKCS12",  new BouncyCastleProvider());
        // 要输入keyStore的口令才能读取
        keyStore.load(inputStream, p12Pwd.toCharArray());
        // 注意这里读取私钥时要提供保护私钥的密码
        // 如果密码提供错误，则可能会报错：java.security.UnrecoverableKeyException: Cannot recover key
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(prvKeyAlias, keyPwd.toCharArray());
        System.out.println("Private key algorithm is: " + privateKey.getAlgorithm());
        System.out.println("Private key be created to this object date is: " + keyStore.getCreationDate(prvKeyAlias).toString());
        X509Certificate subCert = (X509Certificate) keyStore.getCertificate(subCertAlias);
        Cryptology.getCertMsg(subCert);
        System.out.println("SubCert be created to this object date is: " + keyStore.getCreationDate(subCertAlias));
        if(StringUtils.isNotBlank(rootCertAlias)){
            X509Certificate rootCert = (X509Certificate) keyStore.getCertificate(rootCertAlias);
            Cryptology.getCertMsg(rootCert);
        }
        System.out.println("SubCert alias is: " + keyStore.getCertificateAlias(subCert));
        System.out.println("---------------begin parse P12---------------");
    }
}
