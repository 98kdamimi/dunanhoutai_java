package com.junyang.utils;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

/**
 * @category 设备证书
 * @author coocaa
 *
 */
public class EncryptionUtil {
	
	static {
        Security.addProvider(new BouncyCastleProvider());
    }
	
	static {
        Security.addProvider(new BouncyCastleProvider());
    }
	
	/**
	 * 生成 secp256k1 曲线的 EC 密钥对，返回私钥和公钥。
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidAlgorithmParameterException
	 */
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        try {
        	// 创建 EC 密钥对生成器，指定使用 secp256k1 曲线
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");
            keyPairGenerator.initialize(new ECGenParameterSpec("secp256k1"));
            return keyPairGenerator.generateKeyPair();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
    }
    
    
    /**
     * 将公钥添加到证书 使用私钥签名证书 创建X.509证书
     * @param keyPair  EC 密钥对
     * @param deviceSerialNumber 设备序列号
     * @return
     * @throws Exception
     */
    public static X509Certificate generateCertificate(KeyPair keyPair, String deviceSerialNumber) throws Exception {
        // 证书信息
        X500Name issuer = new X500Name("CN=Issuer");
        X500Name subject = new X500Name("CN=" + deviceSerialNumber);  // 设备序列号作为 CN

        // 生成证书的序列号
        BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());

        // 设置证书有效期
        Date startDate = new Date(System.currentTimeMillis());
        Date endDate = new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000L); // 有效期一年

        // 创建证书构建器
        X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                issuer, serialNumber, startDate, endDate, subject, keyPair.getPublic());

        // 使用私钥签名证书
        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withECDSA")
                .build(keyPair.getPrivate());

        // 生成证书
        X509Certificate certificate = new JcaX509CertificateConverter().getCertificate(certBuilder.build(contentSigner));

        return certificate;
    }
    
    
    /**
     * @category 将证书写入到PEM 格式文件
     * @param certificate
     * @param filePath
     * @throws IOException
     */
    public static void writeCertificateToFile(X509Certificate certificate, String filePath) throws IOException{
    	 // 确保目标文件夹存在
        File file = new File(filePath);
        file.getParentFile().mkdirs();  // 如果目标文件夹不存在，则创建它
        // 将证书写入 PEM 格式文件
        try (FileWriter writer = new FileWriter(filePath);
             JcaPEMWriter pemWriter = new JcaPEMWriter(writer)) {
            pemWriter.writeObject(certificate);
        }
    }
    
    
    
    public static void main(String[] args) {
    	try {
    		KeyPair keyPair = EncryptionUtil.generateKeyPair();
    		System.out.println(keyPair.getPublic());
    		System.out.println(keyPair.getPrivate());
    		X509Certificate certificate = EncryptionUtil.generateCertificate(keyPair, "DS0000000000001");
    		System.out.println(certificate);
    		EncryptionUtil.writeCertificateToFile(certificate, "E:/test/certificate.pem");
    		
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
