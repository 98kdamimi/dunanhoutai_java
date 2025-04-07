package com.junyang.enums;


/**
 * @category 事件类型
 * @author coocaa
 *
 */
public enum EventTypeEunms {
	TOKENTRANSFER("tokenTransfer"),//代币转账
	NATIVETOKENTRANSFER("nativeTokenTransfer");//本链币转账
	

	private String name;

	private EventTypeEunms(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
