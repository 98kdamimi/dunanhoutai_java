package com.junyang.utils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

public class IpUtils {
    /**
     * 解析ip地址信息
     *
     */
    public static String parseIp2Location(String ip) {
        if (ip == null || ip.startsWith("192")) {
            return null;
        }
        try {
            // 上传图片请求xx
            CloseableHttpClient httpClient = HttpClients.createDefault();
            // 设置请求的URL路径
            URIBuilder urlBuilder = new URIBuilder()
                    .setScheme("https")
                    .setHost("www.fkcoder.com")
                    .setPath("/ip")
                    .addParameter("ip", ip);
            // 设置请求的连接超时时间
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(1000)
                    .setConnectTimeout(1000)
                    .build();
                URI url = urlBuilder.build();
                // 无参数GET请求方式
                HttpGet httpGet = new HttpGet(url);
                httpGet.setConfig(requestConfig);
                httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
                // 获取返回值
                CloseableHttpResponse apiRes = httpClient.execute(httpGet);
                HttpEntity entity = apiRes.getEntity();
                String content = EntityUtils.toString(entity);
//                log.info("接收到了返回信息:" + content);
                return content;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getIpAddress(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        //注意本地测试时，浏览器请求不要用localhost，要用本机IP访问项目地址，不然这里取不到ip
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

        String ip = request.getHeader("X-Forwarded-For");
//        if (log.isInfoEnabled()) {
//            log.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);
//        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
//                if (log.isInfoEnabled()) {
//                    log.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
//                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
//                if (log.isInfoEnabled()) {
//                    log.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
//                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
//                if (log.isInfoEnabled()) {
//                    log.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);
//                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//                if (log.isInfoEnabled()) {
//                    log.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);
//                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
//                if (log.isInfoEnabled()) {
//                    log.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
//                }
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

}
