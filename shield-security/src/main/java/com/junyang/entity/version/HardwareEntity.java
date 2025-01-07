package com.junyang.entity.version;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HardwareEntity {

	@TableField("bootloaders")
	@ApiModelProperty(name = "bootloaders", value = "加载程序", required = false, dataType = "List<Bootloader>")
	private List<Bootloader> bootloaders;
	
	@TableField("firmwares")
	@ApiModelProperty(name = "firmwares", value = "固件", required = false, dataType = "List<Firmware>")
	private List<Firmware> firmwares;
	
	@TableField("onlineState")
	@ApiModelProperty(name = "onlineState", value = "上线状态", required = false, dataType = "Integer")
	private Integer onlineState;

	@Data
	public static class Bootloader {
		private Changelog changelog;
		private String fingerprint;
		private boolean required;
		private String url;
		private List<Integer> version;
	}

	@Data
	public static class Firmware {
		private Changelog changelog;
		private String fingerprint;
		private boolean required;
		private String url;
		private List<Integer> version;
	}

	@Data
	public static class Changelog {
		private JSONObject locale;
		private List<Integer> version;
	}
	
	

}
