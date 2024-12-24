package com.junyang.enums;


/**
 * @category 接口地址
 * @author coocaa
 *
 */
public enum HttpAddressEunms {
	ONLINE("manager/online"),// 上线接口地址
	NETWORK_LIST("manager/network/list"),// 网络列表
	NETWORK_LIST_ALL("manager/network/list?isAll=true"),// 网络列表
	TOKEN_LIST("manager/token/list?"),// 代币列表
	TOKEN_ADD("manager/token/create"),// 新增代币
	TOKEN_UPDATE("manager/token/update"),//更新代币
	DAPP_LIST("manager/discover/list"),//查询Dapp服务模块当前可用的版本列表
	DAPP_UPDATE("manager/discover/update"),//发现页更新
	DAPP_ADD("manager/discover/create"),//发现页新增
	DAPP_TYPE_LIST("manager/discoverType/list"),//发现页类型列表
	CAROUSE_LIST("manager/carousel/list"),//轮播图列表
	CAROUSE_DELETE("manager/carousel/delete"),//轮播图删除
	CAROUSE_UPDATE("manager/carousel/update"),//编辑轮播图
	CAROUSE_ADD("manager/carousel/create"),//轮播图创建
	MESSAGE_PUSH("message/push"),//消息推送
	GREEMENT_ADD("manager/agreement/create"),//新增协议
	GREEMENT_UPDATE("manager/agreement/update"),//编辑协议
	GREEMENT_DELETE("manager/agreement/delete"),//删除协议
	GREEMENT_LIST("manager/agreement/list"),//协议列表
	LINKEMAIL_ADD("manager/linkEmail/create"),//新增联系邮箱
	LINKEMAIL_DELETE("manager/linkEmail/delete"),//删除联系邮箱
	LINKEMAIL_LIST("manager/linkEmail/list"),//联系邮箱列表;
	LINKEMAIL_UPDATE("manager/linkEmail/update");//联系邮箱编辑;

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
