package com.junyang.enums;


/**
 * @category dapp三方接口地址
 * @author coocaa
 *
 */
public enum DappHttpAddressEunms {
	
	GET_TOP("https://apis.dappradar.com/v2//dapps/top/uaw"),// 获取热门dapp
	
	GET_NETWORK("https://apis.dappradar.com/v2/dapps/chains"),// 获取链
	
	GET_DAPP("https://apis.dappradar.com/v2/dapps");//获取列表
	
	private String name;

	private DappHttpAddressEunms(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
