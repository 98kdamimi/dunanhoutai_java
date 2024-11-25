package com.junyang.enums;


/**
 * @category 接口地址
 * @author coocaa
 *
 */
public enum CoingeckoSiteEunms {
	ASSET_PLATFORMS("https://api.coingecko.com/api/v3/asset_platforms"),//资产平台列表
	NETWORKS("https://api.coingecko.com/api/v3/onchain/networks");//网络列表

	private String name;

	private CoingeckoSiteEunms(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
