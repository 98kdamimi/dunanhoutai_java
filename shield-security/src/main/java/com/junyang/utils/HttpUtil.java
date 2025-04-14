package com.junyang.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import com.junyang.config.DappConfig;
import lombok.var;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {

	/**
	 * 发送get请求
	 *
	 * @throws Exception
	 */
	public static String get(String url) {

		String result = "";
		InputStream in = null;
		try {
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestMethod("GET");
			// 建立实际的连接
			conn.connect();
			// 定义输入流来读取URL的响应
			in = conn.getInputStream();
			result = StreamUtils.copyToString(in, Charset.forName("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	
	public static String getCoingecko(String url, String cOINGECKO_SIGN) {

		String result = "";
		InputStream in = null;
		try {
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("x-cg-pro-api-key", cOINGECKO_SIGN);
			conn.setRequestMethod("GET");
			// 建立实际的连接
			conn.connect();
			// 定义输入流来读取URL的响应
			in = conn.getInputStream();
			result = StreamUtils.copyToString(in, Charset.forName("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 发送post请求
	 *
	 * @throws Exception
	 */
	public static String post(String url, String paramStr) {
		InputStream in = null;
		OutputStream os = null;
		String result = "";
		try {
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			// 发送POST请求须设置
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			os = conn.getOutputStream();
			// 注意编码格式，防止中文乱码
			if (StringUtils.hasText(paramStr)) {
				os.write(paramStr.getBytes("utf-8"));
				os.close();
			}
			in = conn.getInputStream();
			result = StreamUtils.copyToString(in, Charset.forName("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 下载图片
	 * 
	 * @param url
	 * @param out
	 */
	public static void getImage(String url, OutputStream out) {
		InputStream in = null;
		try {
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "image/jpeg");
			conn.setRequestProperty("Accept", "aimage/jpeg");
			conn.setRequestMethod("GET");
			// 建立实际的连接
			conn.connect();
			// 定义输入流来读取URL的响应
			in = conn.getInputStream();
			StreamUtils.copy(in, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 发送post请求
	 *
	 * @throws Exception
	 */
	public static String sendPost(String url, String paramStr) {
		InputStream in = null;
		OutputStream os = null;
		String result = "";
		try {
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 compatible; MSIE 5.0;Windows NT; DigExt)");// （主要是这一句）
//			conn.setRequestProperty("Accept", "application/json");
			// 发送POST请求须设置
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			os = conn.getOutputStream();
			// 注意编码格式，防止中文乱码
			if (StringUtils.hasText(paramStr)) {
				os.write(paramStr.getBytes("utf-8"));
				os.close();
			}
			in = conn.getInputStream();
			result = StreamUtils.copyToString(in, Charset.forName("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	// post请求
	public static String sendPostRequest(String url, String jsonParam) {
		StringBuilder result = new StringBuilder();
		HttpURLConnection conn = null;

		try {
			// 打开连接
			conn = (HttpURLConnection) new URL(url).openConnection();

			// 设置请求属性
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
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

	/**
	 * @category 获取壁虎上的代币信息
	 * @param coinId
	 * @param contractAddress
	 * @return
	 */
	public static String getTokenData(String coinId, String contractAddress) {
		try {
			// 构造 API 请求 URL
			String urlString = "https://api.coingecko.com/api/v3/coins/" + coinId + "/contract/" + contractAddress;
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");
			// 获取响应代码和输入流
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder response = new StringBuilder();
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				return response.toString();
			} else {
				return "Error: " + responseCode;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
	}

	public static String dappGet(String url) {
        String result = "";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            // 设置通用请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("X-API-KEY", DappConfig.DAPP_KEY);
            conn.setRequestProperty("User-Agent", "PostmanRuntime/7.30.0"); // 模拟Postman的UA
            conn.setRequestMethod("GET");
            conn.connect();
            // 检查 HTTP 响应状态码
            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                try (InputStream errorStream = conn.getErrorStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream, StandardCharsets.UTF_8))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        errorResponse.append(line);
                    }
                    throw new IOException("HTTP error: " + responseCode + ", " + errorResponse.toString());
                }
            }
            // 读取正常响应
            try (InputStream in = conn.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                result = sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
	
	public static String sendWebHookGetRequest(String urlString, String apiKey,Map<String, String> params) {
		 StringBuilder response = new StringBuilder();
	        try {
	            // 构建 URL 并附加参数
	        	StringBuilder urlWithParams = new StringBuilder(urlString);
	        	if (params != null && !params.isEmpty()) {
	                urlWithParams.append("?");
	                for (Map.Entry<String, String> entry : params.entrySet()) {
	                    urlWithParams.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
	                                 .append("=")
	                                 .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
	                                 .append("&");
	                }
	                urlWithParams.deleteCharAt(urlWithParams.length() - 1); // 移除最后的'&'
	            }
	            URL url = new URL(urlWithParams.toString());
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("GET");
	            connection.setRequestProperty("Ok-Access-Key", apiKey);

	            int responseCode = connection.getResponseCode();
	            if (responseCode == HttpURLConnection.HTTP_OK) {
	                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	                String inputLine;
	                while ((inputLine = in.readLine()) != null) {
	                    response.append(inputLine);
	                }
	                in.close();
	            } else {
	                return "GET request failed: " + responseCode;
	            }
	            connection.disconnect();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "Error: " + e.getMessage();
	        }
	        return response.toString();
    }
	
	
	public static String sendWebHookPostRequest(String url, String apiKey, String jsonParam) {
		StringBuilder result = new StringBuilder();
		HttpURLConnection conn = null;

		try {
			// 打开连接
			conn = (HttpURLConnection) new URL(url).openConnection();

			// 设置请求属性
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Ok-Access-Key", apiKey);
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
	
	
	 public static void main(String[] args) {
	        OkHttpClient client = new OkHttpClient();
	        String apiUrl = "https://tonapi.io/v1/wallet/create";  // 这里是示例 API，实际 API 可能不同

	        Request request = new Request.Builder()
	                .url(apiUrl)
	                .get()
	                .addHeader("Content-Type", "application/json")
	                .build();

	        try (Response response = client.newCall(request).execute()) {
	            if (response.isSuccessful() && response.body() != null) {
	                System.out.println("TON Wallet Address: " + response.body().string());
	            } else {
	                System.out.println("Failed to create wallet: " + response.code());
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

}