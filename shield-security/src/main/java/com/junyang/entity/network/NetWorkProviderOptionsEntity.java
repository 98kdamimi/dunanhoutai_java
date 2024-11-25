package com.junyang.entity.network;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "网络提供商",description = "网络提供商")
public class NetWorkProviderOptionsEntity {

	@ApiModelProperty(name = "EIP1559Enabled", value = "是否启用了 EIP-1559 交易费用机制", required = false, dataType = "boolean")
	private boolean EIP1559Enabled;
	
	@ApiModelProperty(name = "preferMetamask", value = "是否首选 Metamask 钱包", required = false, dataType = "boolean")
	private boolean preferMetamask;
	
	@ApiModelProperty(name = "hardwareCoinName", value = "硬件钱包上表示的代币名称", required = false, dataType = "String")
	private String hardwareCoinName;
	
}
