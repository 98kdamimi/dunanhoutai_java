package com.junyang.enums;


/**
 * @category 接口地址
 * @author coocaa
 *
 */
public enum DappTypeLableEunms {
	TAG("tag"),
	CATEGORY("category");
	

	private String name;

	private DappTypeLableEunms(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
