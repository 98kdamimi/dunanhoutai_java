package com.junyang.enums;


/**
 * @category 接口地址
 * @author coocaa
 *
 */
public enum HttpAddressEunms {
	ONLINE("api/manager/online"),// 上线接口地址
	NETWORK_LIST("api/manager/network/list"),// 网络列表
	NETWORK_LIST_ALL("api/manager/network/list?isAll=true"),// 网络列表
	TOKEN_LIST("api/manager/token/list?"),// 代币列表
	TOKEN_ADD("api/manager/token/create"),// 新增代币
	TOKEN_UPDATE("api/manager/token/update"),//更新代币
	DAPP_LIST("api/manager/discover/list"),//查询Dapp服务模块当前可用的版本列表
	DAPP_UPDATE("api/manager/discover/update"),//发现页更新
	DAPP_ADD("api/manager/discover/create"),//发现页新增
	DAPP_TYPE_LIST("api/manager/discoverType/list"),//发现页类型列表
	CAROUSE_LIST("api/manager/carousel/list"),//轮播图列表
	CAROUSE_DELETE("api/manager/carousel/delete"),//轮播图删除
	CAROUSE_UPDATE("api/manager/carousel/update"),//编辑轮播图
	CAROUSE_ADD("api/manager/carousel/create"),//轮播图创建
	MESSAGE_PUSH("api/message/push"),//消息推送
	GREEMENT_ADD("api/manager/agreement/create"),//新增协议
	GREEMENT_UPDATE("api/manager/agreement/update"),//编辑协议
	GREEMENT_DELETE("api/manager/agreement/delete"),//删除协议
	GREEMENT_LIST("api/manager/agreement/list"),//协议列表
	LINKEMAIL_ADD("api/manager/linkEmail/create"),//新增联系邮箱
	LINKEMAIL_DELETE("api/manager/linkEmail/delete"),//删除联系邮箱
	LINKEMAIL_LIST("api/manager/linkEmail/list"),//联系邮箱列表;
	LINKEMAIL_UPDATE("api/manager/linkEmail/update"),//联系邮箱编辑;
	APPCONFIG_ADD("api/manager/appConfig/create"),//app配置新增;
	APPCONFIG_UPDATE("api/manager/appConfig/update"),//app配置编辑;
	APPCONFIG_LIST("api/manager/appConfig/list"),//app配置列表
	HELP_DELETE("api/manager/help/delete"),//帮助菜单删除
	HELP_ADD("api/manager/help/create"),//帮助菜单新增
	HELP_LIST("api/manager/help/list"),//帮助菜单列表
	HELP_UPDATE("api/manager/help/update"),//帮助菜单编辑
	ABBREVIATION_DELETE("api/manager/abbreviation/delete"),//联系方式删除
	ABBREVIATION_ADD("api/manager/abbreviation/create"),//联系方式新增
	ABBREVIATION_LIST("api/manager/abbreviation/list"),//联系方式列表
	ABBREVIATION_UPDATE("api/manager/abbreviation/update");//联系方式编辑

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
