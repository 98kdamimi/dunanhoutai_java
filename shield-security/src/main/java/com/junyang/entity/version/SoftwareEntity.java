package com.junyang.entity.version;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SoftwareEntity {
	
	@TableField("android")
	@ApiModelProperty(name = "android", value = "安卓版本信息", required = false, dataType = "Android")
	private Android android;
	
	@TableField("changelog")
	@ApiModelProperty(name = "changelog", value = "更新日志", required = false, dataType = "List<Changelog>")
	private List<Changelog> changelog;
	
	@TableField("ios")
	@ApiModelProperty(name = "ios", value = "ios版本信息", required = false, dataType = "ios")
	private IOS ios;
	
	@TableField("onlineState")
	@ApiModelProperty(name = "onlineState", value = "上线状态", required = false, dataType = "Integer")
	private Integer onlineState;

	
	@Data
	public static class Android {
		private List<Integer> forceUpdateVersion;
		private Google google;
		private String googlePlay;
		private String url;
		private List<Integer> version;
		private List<Changelog> changelog;
	}

	@Data
	public static class Google {
		private String url;
		private List<Integer> version;
	}

	@Data
	public static class IOS {
		private List<Integer> forceUpdateVersion;
		private String url;
		private List<Integer> version;
		private List<Changelog> changelog;
	}

	@Data
	public static class Changelog {
		private JSONObject locale;
		private List<Integer> version;
	}

	

}
