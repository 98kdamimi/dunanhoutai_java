package com.junyang.entity.wallet;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "账户详细信息", description = "账户详细信息")
public class AccountInfoEntity {
	@ApiModelProperty(name = "address", value = "钱包地址", required = false, dataType = "String")
	private String address;

	@ApiModelProperty(name = "coinType", value = "币种类型", required = false, dataType = "String")
	private String coinType;

	@ApiModelProperty(name = "id", value = "账户ID", required = false, dataType = "String")
	private String id;

	@ApiModelProperty(name = "name", value = "账户名称", required = false, dataType = "String")
	private String name;

	@ApiModelProperty(name = "path", value = "路径", required = false, dataType = "String")
	private String path;

	@ApiModelProperty(name = "type", value = "账户类型", required = false, dataType = "String")
	private String type;

	@ApiModelProperty(name = "walletType", value = "钱包类型", required = false, dataType = "String")
	private String walletType;

	@ApiModelProperty(name = "enabled", value = "是否启用", required = false, dataType = "Boolean")
	private Boolean enabled;

	@ApiModelProperty(name = "webhookIds", value = "Webhook ID 列表", required = false, dataType = "List")
	private List<String> webhookIds;

	@ApiModelProperty(name = "batchNo", value = "批次号", required = false, dataType = "String")
	private String batchNo;
}