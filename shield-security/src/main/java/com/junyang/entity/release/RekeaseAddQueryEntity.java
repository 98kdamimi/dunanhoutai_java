package com.junyang.entity.release;

import lombok.Data;

/**
 * @category 管理端版本发布参数
 * @author Hlin
 *
 */
@Data
public class RekeaseAddQueryEntity {
	
	private String iosVersion;
	
	private String iosUrl;
	
	private String androidVersion;
	
	private String androidUrl;
		
	private String googlePlayUrl;
	
	private String enUS;
	
	private String zhCN;


}
