package com.junyang.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.map.HashedMap;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.junyang.config.WxPublicConfig;

/**
 * 微信公众号接口工具类
 * 
 *
 */
@Component
public class WechatUtil {

	/**
	 * 通过openid获取用户绑定信息
	 */
	public static final String GETUSERBINDSTATUS_URL = "https://wx.igskapp.com/WeChatPlatform/Mp_Token/GetUserBindStatus";

	/**
	 * 获取全局access_token
	 */
	public static final String GETACCESSTOKEN_URL = "https://wx.igskapp.com/WeChatPlatform/Mp_Token/GetAccessToken";

	/**
	 * 微信接入ToKen
	 */
	public static final String TOKEN = "wechat";

	/**
	 * appid
	 */
	public static final String APPID = "wx935def5a4541f543";

	/**
	 * wechatid
	 */
	public static final String WECHAT_ID = "5ab542de-2cf2-40d2-8b33-18b4963f6332";

	/**
	 * 上传图文消息内的图片获取URL
	 */
	public static final String WX_IMG_UP_URL = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=";

	/**
	 * 新增图片类型永久素材URL
	 */
	public static final String WX_ADD_MATERIAL_URL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=";

	/**
	 * 新增图片类型临时素材URL
	 */
	public static final String WX_ADD_TEMPORARY_MATERIAL_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=";

	/**
	 * 新增永久图文素材URL
	 */
	public static final String WX_ADD_NEWS_URL = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=";

	/**
	 * 获取素材列表URL
	 */
	public static final String WX_SELECTALL_MATERIAL_URL = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=";

	/**
	 * 获取永久素材URL
	 */
	public static final String WX_SELECT_MATERIAL_URL = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=";

	/**
	 * 删除永久素材URL
	 */
	public static final String WX_DEL_MATERIAL_URL = "https://api.weixin.qq.com/cgi-bin/material/del_material?access_token=";

	/**
	 * 创建标签URL
	 */
	public static final String WX_CREATE_LABEL_URL = "https://api.weixin.qq.com/cgi-bin/tags/create?access_token=";

	/**
	 * 删除标签URL
	 */
	public static final String WX_DEL_LABEL_URL = "https://api.weixin.qq.com/cgi-bin/tags/delete?access_token=";

	/**
	 * 批量为用户打标签URL
	 */
	public static final String WX_ADD_USER_LABEL_URL = "https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token=";

	/**
	 * 批量为用户取消标签URL
	 */
	public static final String WX_DEL_USER_LABEL_URL = "https://api.weixin.qq.com/cgi-bin/tags/members/batchuntagging?access_token=";

	/**
	 * 创建默认菜单URL
	 */
	public static final String WX_CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";

	/**
	 * 创建个性化菜单URL
	 */
	public static final String WX_ADD_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token=";

	/**
	 * 删除个性化菜单URL
	 */
	public static final String WX_DEL_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delconditional?access_token=";

	/**
	 * 根据标签进行群发URL
	 */
	public static final String WX_SEND_NEWS_LEBEL_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=";

	/**
	 * 根据openid列表进行群发URL
	 */
	public static final String WX_SEND_NEWS_OPENID_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=";

	/**
	 * 根据openid获取用户信息URL
	 */
	public static final String WX_FIND_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=";

	/**
	 * @category 模板消息推送URL
	 */
	public static final String WX_PUSH_TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

	/**
	 * 返回消息类型：文本
	 */
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 返回消息类型：音乐
	 */
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

	/**
	 * 返回消息类型：图文
	 */
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";

	/**
	 * 返回消息类型：图片
	 */
	public static final String RESP_MESSAGE_TYPE_IMAGE = "image";

	/**
	 * 返回消息类型：语音
	 */
	public static final String RESP_MESSAGE_TYPE_VOICE = "voice";

	/**
	 * 返回消息类型：视频
	 */
	public static final String RESP_MESSAGE_TYPE_VIDEO = "video";

	/**
	 * 请求消息类型：文本
	 */
	public static final String REQ_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 请求消息类型：图片
	 */
	public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

	/**
	 * 请求消息类型：链接
	 */
	public static final String REQ_MESSAGE_TYPE_LINK = "link";

	/**
	 * 请求消息类型：地理位置
	 */
	public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

	/**
	 * 请求消息类型：音频
	 */
	public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

	/**
	 * 请求消息类型：视频
	 */
	public static final String REQ_MESSAGE_TYPE_VIDEO = "video";

	/**
	 * 请求消息类型：推送
	 */
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";

	/**
	 * 事件类型：subscribe(订阅)
	 */
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

	/**
	 * 事件类型：unsubscribe(取消订阅)
	 */
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

	/**
	 * 事件类型：CLICK(自定义菜单点击事件)
	 */
	public static final String EVENT_TYPE_CLICK = "CLICK";

	/**
	 * 事件类型：VIEW(自定义菜单URl视图)
	 */
	public static final String EVENT_TYPE_VIEW = "VIEW";

	/**
	 * 事件类型：LOCATION(上报地理位置事件)
	 */
	public static final String EVENT_TYPE_LOCATION = "LOCATION";

	/**
	 * 事件类型：SCAN(用户已关注时扫码事件)
	 */
	public static final String EVENT_TYPE_SCAN = "SCAN";
	/**
	 * 全局accessToken
	 */
	public static String accessToken;
	/**
	 * accessToken过期时间
	 */
	public static long expiresTime;

	/**
	 * 将微信请求中的参数转成map
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> xmlToMap(HttpServletRequest request) {
		// new一个map集合
		Map<String, String> map = new HashMap<String, String>();
		// 通过SAXReader解析xml格式
		SAXReader reader = new SAXReader();
		// 流
		InputStream in = null;
		try {
			// 获取传过来的流数据
			in = request.getInputStream();
			// reader.read(in)：从xml文件获取数据
			Document doc = reader.read(in);
			Element root = doc.getRootElement();
			// 把数据放到list集合中
			@SuppressWarnings("unchecked")
			List<Element> list = root.elements();
			// 遍历集合，放到map集合中
			for (Element element : list) {
				map.put(element.getName(), element.getText());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	/**
	 * 获取全局access_token（测试）公众号
	 * 
	 * @return access_token
	 */
	public static String getAccessTokenTest() {
		if ((accessToken == null) || (System.currentTimeMillis() > expiresTime)) {
			// 测试账号
			String result = HttpUtil.get(
					"https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+WxPublicConfig.APP_ID+
					"&secret="+WxPublicConfig.APP_SECRET);
			System.out.println(result);
			JSONObject json = JSONObject.parseObject(result);
			accessToken = json.getString("access_token");
			Long expiresIn = json.getLong("expires_in");
			expiresTime = System.currentTimeMillis() + (expiresIn.longValue() - 60L) * 1000L;
		}
		return accessToken;
	}

	/**
	 * 获取全局access_token（测试）公众号
	 * 
	 * @return access_token
	 */
	public static String getAppltAccessTokenTest() {
		if ((accessToken == null) || (System.currentTimeMillis() > expiresTime)) {
			// 测试账号
			String result = HttpUtil.get(
					"https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET"
							.replace("APPID", WxPublicConfig.APPLET_APPID)
							.replace("APPSECRET", WxPublicConfig.APPLET_SECRET));
			JSONObject json = JSONObject.parseObject(result);
			accessToken = json.getString("access_token");
			Long expiresIn = json.getLong("expires_in");
			expiresTime = System.currentTimeMillis() + (expiresIn.longValue() - 60L) * 1000L;
		}
		return accessToken;
	}

	/**
	 * 通过openid获取用户基本信息
	 * 
	 * @param openid 用户公众号标识
	 * @return
	 */
	public static String findInfoByOpenid(String openid) {
		String accessToken = getAccessTokenTest();
		String result = HttpUtil.get(WX_FIND_INFO_URL + accessToken + "&openid=" + openid + "&lang=zh_CN");
		if (result.indexOf("errcode") == -1) {
			return result;
		}
		return null;
	}


	/**
	 * @category 微信小程序获取用户tel
	 * @param code
	 */
	public static void appletFindTel(String code) {
		String toke = getAppltAccessTokenTest();
		Map<String, Object> map = new HashedMap<>();
		map.put("code", code);
		HttpUtil.post("https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + toke + "",
				JSON.toJSONString(map));
	}

	/**
	 * @category 微信小程序获取用户openid
	 * @param code
	 */
	public static String getOpenid(String code) {
		String data = HttpUtil.get("https://api.weixin.qq.com/sns/jscode2session?appid=" + WxPublicConfig.APPLET_APPID
				+ "&secret=" + WxPublicConfig.APPLET_SECRET + "&js_code=" + code + "&grant_type=authorization_code");
		return data;
	}

	/**
	 * @category 微信解密
	 * @param key
	 * @param iv
	 * @param encData
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(byte[] key, byte[] iv, byte[] encData) throws Exception {
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
		// 解析解密后的字符串
		return new String(cipher.doFinal(encData), "UTF-8");
	}

}
