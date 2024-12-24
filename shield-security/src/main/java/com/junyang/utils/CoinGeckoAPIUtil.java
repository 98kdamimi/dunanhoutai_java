package com.junyang.utils;
//
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//
public class CoinGeckoAPIUtil {
//	
//	// CoinGecko API URL，获取市场代币信息，USD 作为货币单位
//    private static final String COINGECKO_API_URL = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd";
//
//    // 获取代币市场数据
    public static String fetchTokenData() {
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(COINGECKO_API_URL))  // 设置 API 请求地址
//                .build();
//        try {
//            // 发送请求并获取响应
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            return response.body().toString();
//            
////
////            // 检查响应状态码
////            if (response.statusCode() == 200) {
////                // 解析 JSON 响应
////                JSONArray coins = new JSONArray(response.body());
////                for (int i = 0; i < coins.length(); i++) {
////                    JSONObject coin = coins.getJSONObject(i);
////                    String name = coin.getString("name");   // 代币名称
////                    String symbol = coin.getString("symbol");  // 代币符号
////                    double currentPrice = coin.getDouble("current_price");  // 当前价格
////                    
////                    // 输出代币信息
////                    System.out.println(coin);
////                }
////            } else {
////                System.out.println("Error: Unable to fetch data from CoinGecko.");
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
		return null;
//        
    }
//    
//    public static void main(String[] args) {
//    	CoinGeckoAPIUtil.fetchTokenData();
//	}

}
