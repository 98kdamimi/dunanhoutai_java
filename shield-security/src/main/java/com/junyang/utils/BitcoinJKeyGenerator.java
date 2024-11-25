//package com.junyang.utils;
//import org.bitcoinj.core.ECKey;
//import org.bitcoinj.core.Utils;
//import org.bouncycastle.asn1.x500.X500Name;
//import org.bouncycastle.cert.X509v3CertificateBuilder;
//import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.bouncycastle.operator.ContentSigner;
//import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.math.BigInteger;
//import java.security.*;
//import java.security.cert.X509Certificate;
//import java.security.interfaces.ECPrivateKey;
//import java.security.interfaces.ECPublicKey;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.Date;
//
//public class BitcoinJKeyGenerator {
//
//    static {
//        // 添加 BouncyCastle 提供的安全提供者
//        Security.addProvider(new BouncyCastleProvider());
//    }
//
////    public static void main(String[] args) throws Exception {
////        // 1. 生成 secp256k1 密钥对
////        ECKey key = new ECKey();
////        byte[] privateKeyBytes = key.getPrivKeyBytes();
////        byte[] publicKeyBytes = key.getPubKey();
////
////        // 打印私钥和公钥（可选）
////        System.out.println("Private Key: " + Utils.HEX.encode(privateKeyBytes));
////        System.out.println("Public Key: " + Utils.HEX.encode(publicKeyBytes));
////
////        // 2. 保存私钥和公钥为 PEM 格式
////        saveKeyToPem("private_key.pem", privateKeyBytes);
////        saveKeyToPem("public_key.pem", publicKeyBytes);
////
////        // 3. 生成证书
////        X509Certificate certificate = generateCertificate(privateKeyBytes, publicKeyBytes);
////
////        // 4. 打印证书信息
////        System.out.println("Generated Certificate: ");
////        System.out.println(certificate);
////    }
//
//    // 将密钥保存为 PEM 格式的文件
//    private static void saveKeyToPem(String fileName, byte[] keyBytes) throws IOException {
//        File file = new File(fileName);
//        try (FileWriter writer = new FileWriter(file)) {
//            writer.write("-----BEGIN PRIVATE KEY-----\n");
//            writer.write(Utils.HEX.encode(keyBytes));
//            writer.write("\n-----END PRIVATE KEY-----\n");
//        }
//    }
//
////    // 生成 X.509 证书
////    private static X509Certificate generateCertificate(byte[] privateKeyBytes, byte[] publicKeyBytes) throws Exception {
////        // 1. 解析密钥参数
////        ECKey key = ECKey.fromPrivate(new BigInteger(1, privateKeyBytes));
////        ECPublicKey publicKey = (ECPublicKey) KeyFactory.getInstance("EC").generatePublic(new X509EncodedKeySpec(publicKeyBytes));
////        ECPrivateKey privateKey = (ECPrivateKey) KeyFactory.getInstance("EC").generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
////
////        // 2. 创建 X.509v3 证书生成器
////        X500Name issuer = new X500Name("CN=DS0000000000001");
////        X500Name subject = new X500Name("CN=DS0000000000001");
////        BigInteger serialNumber = BigInteger.valueOf(1);
////        Date notBefore = new Date();
////        Date notAfter = new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000L); // 1年有效期
////
////        // 使用公钥创建 ECPublicKeyParameters
////        ECPublicKeyParameters publicKeyParameters = new ECPublicKeyParameters(publicKey.getW(), ECKey.CURVE);
////
////        X509v3CertificateBuilder certificateBuilder = new X509v3CertificateBuilder(
////                issuer, serialNumber, notBefore, notAfter, subject, publicKeyParameters
////        );
////
////        // 3. 使用私钥签名证书
////        ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(new BigInteger(1, privateKeyBytes), ECKey.CURVE);
////        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withECDSA").build(privateKeyParameters);
////        X509Certificate certificate = new JcaX509CertificateConverter().getCertificate(certificateBuilder.build(contentSigner));
////
////        return certificate;
////    }
//}
