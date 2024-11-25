package com.junyang.enums;


/**
 * @category 接口地址
 * @author coocaa
 *
 */
public enum HttpAddressEunms {
	ONLINE("manager/online"),// 上线接口地址
	NETWORK_LIST("manager/network/list"),// 网络列表
	TOKEN_LIST("manager/token/list?chainId=1&impl=evm"),// 代币列表
	TOKEN_ADD("manager/token/create"),// 新增代币
	TOKEN_UPDATE("manager/token/update"),//更新代币
	DAPP_LIST("manager/discover/list?version=20241001&status=ONLINE&query=20241001"),//查询Dapp服务模块当前可用的版本列表
	DAPP_UPDATE("manager/discover/update");
	

	private String name;

	private HttpAddressEunms(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
