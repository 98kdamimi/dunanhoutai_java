package com.junyang.entity.message;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

@Data
public class JpushMessageEntity {
	
	private String title;//标题
	
	private String msgContent;//消息内容
	
	private String sendType;//推送类型
	
	private String msgContentType;//消息类型
	
	private JSONObject extras;//附加消息

}
