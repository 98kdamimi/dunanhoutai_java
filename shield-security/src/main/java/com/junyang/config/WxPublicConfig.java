package com.junyang.config;

import org.springframework.stereotype.Component;

/**
 * @category 微信公众号配置
 * @author csz
 *
 */
@Component
public class WxPublicConfig {
	//小程序appid
	public static String APPLET_APPID = "wxe25265aa514f6f67";

	//小程序密钥
	public static String APPLET_SECRET = "a560f1b64b6b9f23d5294e599ce7ac49";

	
	// 公众号应用ID
	public static String APP_ID = "wx25528fa98a7d501f";
	// 公众号密钥
	public static String APP_SECRET = "031b334bde79f17f743f03412350b211";
	// 回调地址
	public static String REDIRECT_URI = "https://www.miaomujiaoyizhongxin.com/saplingICP/sapling/wx/login";
	// response_type
	public static String RESPONSE_TYPE = "code";
	// scope
	public static String SCOPE = "snsapi_userinfo";
	// grant_type
	public static String GRANT_TYPE = "authorization_code";
	// 网页授权地址
	public static String ACCREDIT_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?";
	// 获取access_token/openid地址
	public static String GAIN_OPENID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?";
	// 获取微信用户详细信息地址
	public static String GAIN_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?";
	// 模板消息模板id
	public static String TEMPLATE_ID = "P1ilThiFgcTj_QXTH0Q7MrAHmyCafDQXQoIvYoOkrC4";
	// 获取全局access_token
	public static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

	public static String WEIXIN_TOKEN = "miaomujiaoyizhongxin";

	// 模板消息跳转地址
	public static String MSG_SKIP_URL = "";

	//小程序调整地址
	public static String APPLET_PAGEPATH = "/pages/index/index";
}
