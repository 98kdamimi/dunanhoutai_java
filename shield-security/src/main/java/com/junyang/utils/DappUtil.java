package com.junyang.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Random;

public class DappUtil {

	private static final String APP_ID = "20241227002239545";
	private static final String SECURITY_KEY = "Y2nhSJ1XcPZaLLZBYviy";

	public static String translate(String query, String from, String to) {
		String result = "";
		try {
			// 百度翻译 API 的 URL
			String urlStr = "https://fanyi-api.baidu.com/api/trans/vip/translate";

			// 构造参数
			String salt = String.valueOf(new Random().nextInt(10000));
			String sign = md5(APP_ID + query + salt + SECURITY_KEY);
			String params = "q=" + query + "&from=" + from + "&to=" + to + "&appid=" + APP_ID + "&salt=" + salt
					+ "&sign=" + sign;

			// 发送 POST 请求
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			// 发送参数
			try (OutputStream os = conn.getOutputStream()) {
				os.write(params.getBytes("utf-8"));
			}

			// 读取响应
			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				result = response.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static String md5(String string) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(string.getBytes("utf-8"));
			StringBuilder sb = new StringBuilder();
			for (byte b : array) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String unicodeToString(String unicode) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		while (i < unicode.length()) {
			char ch = unicode.charAt(i);
			if (ch == '\\' && i + 1 < unicode.length() && unicode.charAt(i + 1) == 'u') {
				String hex = unicode.substring(i + 2, i + 6);
				sb.append((char) Integer.parseInt(hex, 16));
				i += 6;
			} else {
				sb.append(ch);
				i++;
			}
		}
		return sb.toString();
	}
	
	public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

	public static void main(String[] args) {
		String text = "Hello, how are you?";
		String simplifiedChinese = translate(text, "en", "cht"); // 中文简体
		System.out.println("简体中文: " + unicodeToString(simplifiedChinese));
	}
}