//package com.junyang.utils;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URL;
//import java.nio.charset.Charset;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//
///**
// * 
// * 1.接口文档（必看）：https://www.xfyun.cn/doc/nlp/niutrans/API.html
//
// */
//
//public class XfWebOTS {	
//	// OTS webapi 接口地址
//    private static final String WebOTS_URL = "https://ntrans.xfyun.cn/v2/ots";
//	// 应用ID（到控制台获取）
//	private static final String APPID = "7afe3e98";
//	// 接口APISercet（到控制台机器翻译服务页面获取）
//	private static final String API_SECRET = "YTZhOThiYmY5NTMzZWQ1MjAyYzAzYjI4";
//	// 接口APIKey（到控制台机器翻译服务页面获取）
//	private static final String API_KEY = "487e6836d4d7680c6b46f6900b168237";
//	
//	
//	// 语种列表参数值请参照接口文档：https://doc.xfyun.cn/rest_api/机器翻译.html
//	// 源语种
//	private static final String FROM = "en";
//	// 目标语种
//	private static final String TO = "cht";	
//
//	/**
//	 * OTS WebAPI 调用示例程序
//	 * 
//	 * @param args
//	 * @throws Exception 
//	 */
//	public static void main(String[] args) throws Exception {
////		if (APPID.equals("") || API_KEY.equals("") || API_SECRET.equals("")) {
////			System.out.println("Appid 或APIKey 或APISecret 为空！请打开demo代码，填写相关信息。");
////			return;
////		}
////		String body = buildHttpBody("你好，我是中国人");
////		//System.out.println("【ITR WebAPI body】\n" + body);
////		Map<String, String> header = buildHttpHeader(body);
////		Map<String, Object> resultMap = XfHttpUtil.doPost2(WebOTS_URL, header, body);
////		if (resultMap != null) {
////			String resultStr = resultMap.get("body").toString();
////			System.out.println("【OTS WebAPI 接口调用结果】\n" + resultStr);
////			//以下仅用于调试
////		    Gson json = new Gson();
////	        ResponseData resultData = json.fromJson(resultStr, ResponseData.class);
////	        int code = resultData.getCode();
////	        if (resultData.getCode() != 0) {
////	    		System.out.println("请前往https://www.xfyun.cn/document/error-code?code=" + code + "查询解决办法");
////	        }
////		} else {
////			System.out.println("调用失败！请根据错误信息检查代码，接口文档：https://www.xfyun.cn/doc/nlp/niutrans/API.html");
////		}     
//		XfWebOTS.enToCht("你好，我是中国人");
//	}
//	
//	public static String enToCht(String TEXT) {
//		try {
//			String body = buildHttpBody(TEXT);
//			Map<String, String> header = buildHttpHeader(body);
//			Map<String, Object> resultMap = XfHttpUtil.doPost2(WebOTS_URL, header, body);
//			if (resultMap != null) {
//				String resultStr = resultMap.get("body").toString();
//			    Gson json = new Gson();
//		        ResponseData resultData = json.fromJson(resultStr, ResponseData.class);
//		        if (resultData.getCode() != 0) {
//		    		return null;
//		        }else {
//		        	String str = resultData.getData().getResult().getTrans_result().getDst();
//		        	return str;
//		        }
//			} else {
//				return "";
//			}     
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException();
//		}
//	}
//
//	/**
//	 * 组装http请求头
//	 */	
//   public static Map<String, String> buildHttpHeader(String body) throws Exception {
//		Map<String, String> header = new HashMap<String, String>();	
//        URL url = new URL(WebOTS_URL);
//        
//        //时间戳
//        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
//        format.setTimeZone(TimeZone.getTimeZone("GMT"));        
//        Date dateD = new Date();
//        String date = format.format(dateD);
//		//System.out.println("【OTS WebAPI date】\n" + date);
//
//		//对body进行sha256签名,生成digest头部，POST请求必须对body验证
//		String digestBase64 = "SHA-256=" + signBody(body);
//		//System.out.println("【OTS WebAPI digestBase64】\n" + digestBase64);
//        
//		//hmacsha256加密原始字符串
//        StringBuilder builder = new StringBuilder("host: ").append(url.getHost()).append("\n").//
//        		append("date: ").append(date).append("\n").//
//                append("POST ").append(url.getPath()).append(" HTTP/1.1").append("\n").//
//                append("digest: ").append(digestBase64);
//		//System.out.println("【OTS WebAPI builder】\n" + builder);
//        String sha = hmacsign(builder.toString(), API_SECRET);
//		//System.out.println("【OTS WebAPI sha】\n" + sha);
//		
//		//组装authorization
//        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", API_KEY, "hmac-sha256", "host date request-line digest", sha);
//        //System.out.println("【OTS WebAPI authorization】\n" + authorization);
//		
//        header.put("Authorization", authorization);
//        header.put("Content-Type", "application/json");
//        header.put("Accept", "application/json,version=1.0");
//        header.put("Host", url.getHost());
//        header.put("Date", date);
//        header.put("Digest", digestBase64);
//		//System.out.println("【OTS WebAPI header】\n" + header);
//		return header;
//    }
//   
//
//	/**
//	 * 组装http请求体
//	 * @param text 
//	 */	
//   public static String buildHttpBody(String text) throws Exception {		
//       JsonObject body = new JsonObject();
//       JsonObject business = new JsonObject();
//       JsonObject common = new JsonObject();
//       JsonObject data = new JsonObject();
//       //填充common
//       common.addProperty("app_id", APPID);
//       //填充business
//       business.addProperty("from", FROM);
//       business.addProperty("to", TO);
//       //填充data
//       //System.out.println("【OTS WebAPI TEXT字个数：】\n" + TEXT.length());
//       byte[] textByte = text.getBytes("UTF-8");
//       String textBase64 = new String(Base64.getEncoder().encodeToString(textByte)); 
//       //System.out.println("【OTS WebAPI textBase64编码后长度：】\n" + textBase64.length());
//       data.addProperty("text", textBase64);
//       //填充body
//       body.add("common", common);
//       body.add("business", business);
//       body.add("data", data);
//       return body.toString();
//   }
//
//   
//	/**
//	 * 对body进行SHA-256加密
//	 */	
//	private static String signBody(String body) throws Exception {
//		MessageDigest messageDigest;
//		String encodestr = "";
//		try {
//			messageDigest = MessageDigest.getInstance("SHA-256");
//			messageDigest.update(body.getBytes("UTF-8"));
//			encodestr = Base64.getEncoder().encodeToString(messageDigest.digest());
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		return encodestr;
//	}
//	
//	/**
//	 * hmacsha256加密
//	 */
//	private static String hmacsign(String signature, String apiSecret) throws Exception {
//		Charset charset = Charset.forName("UTF-8");
//		Mac mac = Mac.getInstance("hmacsha256");
//		SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(charset), "hmacsha256");
//		mac.init(spec);
//		byte[] hexDigits = mac.doFinal(signature.getBytes(charset));
//		return Base64.getEncoder().encodeToString(hexDigits);
//	}
//	  
//	public static class ResponseData {
//	    private int code;
//	    private String message;
//	    private String sid;
//	    private Data data;  // 使用嵌套类来描述 data 字段
//
//	    // Getter and Setter methods
//	    public int getCode() {
//	        return code;
//	    }
//
//	    public String getMessage() {
//	        return message;
//	    }
//
//	    public String getSid() {
//	        return sid;
//	    }
//
//	    public Data getData() {
//	        return data;
//	    }
//
//	    // 内部类 Data 对应 "data" 字段
//	    public static class Data {
//	        private Result result;
//
//	        public Result getResult() {
//	            return result;
//	        }
//
//	        public void setResult(Result result) {
//	            this.result = result;
//	        }
//	    }
//
//	    // 内部类 Result 对应 "result" 字段
//	    public static class Result {
//	        private String from;
//	        private String to;
//	        private TransResult trans_result;
//
//	        public String getFrom() {
//	            return from;
//	        }
//
//	        public void setFrom(String from) {
//	            this.from = from;
//	        }
//
//	        public String getTo() {
//	            return to;
//	        }
//
//	        public void setTo(String to) {
//	            this.to = to;
//	        }
//
//	        public TransResult getTrans_result() {
//	            return trans_result;
//	        }
//
//	        public void setTrans_result(TransResult trans_result) {
//	            this.trans_result = trans_result;
//	        }
//	    }
//
//	    // 内部类 TransResult 对应 "trans_result" 字段
//	    public static class TransResult {
//	        private String dst;
//	        private String src;
//
//	        public String getDst() {
//	            return dst;
//	        }
//
//	        public void setDst(String dst) {
//	            this.dst = dst;
//	        }
//
//	        public String getSrc() {
//	            return src;
//	        }
//
//	        public void setSrc(String src) {
//	            this.src = src;
//	        }
//	    }
//	}
//
//}
