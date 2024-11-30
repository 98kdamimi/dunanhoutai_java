package com.junyang.enums;


/**
 * @category 接口地址
 * @author coocaa
 *
 */
public enum TokenTypeLableEunms {
	TAG("tag"),
	CATEGORY("category");
	

	private String name;

	private TokenTypeLableEunms(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
