package com.junyang.utils;

import com.google.zxing.WriterException;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.ICredentialRepository;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

@Component
public final class GoogleAuthenticatorUtil {
	
	private static final GoogleAuthenticator gAuth = new GoogleAuthenticator();
	
	static {
        // 配置GoogleAuthenticator使用内存存储
        gAuth.setCredentialRepository(new ICredentialRepository() {
			@Override
			public void saveUserCredentials(String userName, String secretKey, int validationCode, List<Integer> scratchCodes) {
				
			}
			@Override
			public String getSecretKey(String userName) {
				return null;
			}
		});
    }

    // 生成用户的密钥
    public static GoogleAuthenticatorKey createKey(String userIdentifier) {
        return gAuth.createCredentials(userIdentifier);
    }

    // 生成二维码URL，用于Google Authenticator
    public static String getQRCodeUrl(String userIdentifier, String secretKey) {
        String encodedKey = URLEncoder.encode(secretKey, StandardCharsets.UTF_8);
        return String.format("otpauth://totp/%s?secret=%s&issuer=YourApp", userIdentifier, encodedKey);
    }

    // 生成二维码并将其转为Base64编码的字符串
    public static String getQRCodeBase64(String secretKey, String userIdentifier) throws IOException, WriterException {
        String url = getQRCodeUrl(userIdentifier, secretKey);
        BufferedImage qrCodeImage = QRCodeGenerator.generateQRCodeImage(url);  // 使用自定义的二维码生成器
        return convertImageToBase64(qrCodeImage);
    }

    // 将图片转换为Base64编码的字符串
    private static String convertImageToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    // 验证用户输入的验证码是否正确
    public static boolean verifyCode(String secretKey, int code) {
        return gAuth.authorize(secretKey, code);
    }
    
    public static void main(String[] args) throws IOException, WriterException {
    	String key = GoogleAuthenticatorUtil.createKey("admin").getKey();
    	System.out.println(key);
    	System.out.println(GoogleAuthenticatorUtil.verifyCode("QAXMNIOPHPEMRSSV", 551108));
    	
	}
    
    
}
