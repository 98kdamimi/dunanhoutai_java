package com.junyang.utils;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import lombok.var;
import com.alibaba.fastjson.JSON;
import com.junyang.entity.system.PushNotificationRequest;


@Configuration
public class JpushUtilTwo {
	
	private static String url = "https://api.jpush.cn/v3/push";
	
    
    private static String APP_KEY = "2c315ff1d01a2ca7905b0b92";
    private static String MASTER_SECRET = "e80477941e8435b9ff672646";
 
    
 // post请求
 	public static String sendPostRequest(String url, String jsonParam) {
 		StringBuilder result = new StringBuilder();
 		HttpURLConnection conn = null;
 		try {
 			String temp = APP_KEY+":"+MASTER_SECRET;
 			String encoded = Base64.getEncoder().encodeToString(temp.getBytes());
 			System.out.println(encoded);
 			String head = "Basic "+encoded;
 			// 打开连接
 			conn = (HttpURLConnection) new URL(url).openConnection();

 			// 设置请求属性
 			conn.setRequestProperty("Accept", "application/json");
 			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
 			conn.setRequestProperty("Authorization", head);
 			conn.setRequestMethod("POST");
 			conn.setDoOutput(true);

 			// 将 JSON 数据写入请求体
 			try (OutputStream os = conn.getOutputStream()) {
 				os.write(jsonParam.getBytes(StandardCharsets.UTF_8));
 			}

 			// 读取响应
 			try (var in = conn.getInputStream();
 					var reader = new java.io.BufferedReader(
 							new java.io.InputStreamReader(in, StandardCharsets.UTF_8))) {
 				String line;
 				while ((line = reader.readLine()) != null) {
 					result.append(line);
 				}
 			}
 		} catch (Exception e) {
 			e.printStackTrace();
 		} finally {
 			if (conn != null) {
 				conn.disconnect();
 			}
 		}

 		return result.toString();
 	}
 	
 	public static void send(List<String> registrationIds, String title, String content) {
 		PushNotificationRequest request  = new PushNotificationRequest();
        
        // 设置平台
        request.setPlatform("all");
        // 设置 Audience
        PushNotificationRequest.Audience audience = new PushNotificationRequest.Audience();
        audience.setRegistration_id(registrationIds);
        request.setAudience(audience);
        
        // 设置 Notification
        PushNotificationRequest.Notification notification = new PushNotificationRequest.Notification();
        notification.setAlert(content);
        request.setNotification(notification);
        
        // 设置 Options
        PushNotificationRequest.Options options = new PushNotificationRequest.Options();
        options.setTime_to_live(86400);
        options.setApns_production(true);
        request.setOptions(options);
    	
        String jsonParam = JSON.toJSONString(request);
        
        String response = sendPostRequest(url, jsonParam);
    	System.out.println(response);

 	}
    
    
    public static void main(String[] args) {
    	
    	
    	
	}

}
