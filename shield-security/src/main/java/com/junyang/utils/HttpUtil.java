package com.junyang.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import com.junyang.enums.CoingeckoSiteEunms;

import lombok.var;

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

}