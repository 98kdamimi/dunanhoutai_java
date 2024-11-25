package com.junyang.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1Signature {
	
	public static String calculateSHA1(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(data.getBytes());
        byte[] byteData = md.digest();
        // 转换为十六进制编码串
        StringBuilder sb = new StringBuilder();
        for (byte b : byteData) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            // 示例数据
            String dataToSign = "a03a5f5d8fed0e6a650264b483a7efadappKeyec4d82cbed3d11ebbcdb00163e0c0763imei8986111820803855290a03a5f5d8fed0e6a650264b483a7efad";

            // 计算SHA-1签名
            String signatureResult = calculateSHA1(dataToSign);

            // 打印签名结果
            System.out.println("SHA-1 Signature: " + signatureResult);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
