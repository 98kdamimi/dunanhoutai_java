package com.junyang.entity.wallet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @category 代币余额
 * @author Hlin
 *
 */
@Data
public class TokenBalanceEntity {
	
	
	@ApiModelProperty(name = "name", value = "代币名称", required = false, dataType = "String")
	private String name;
	
	@ApiModelProperty(name = "address", value = "代币合约地址", required = false, dataType = "String")
	private String address;
	
	@ApiModelProperty(name = "coingeckoId", value = "代币在 CoinGecko 上的 ID", required = false, dataType = "String")
	private String coingeckoId;
	
	@ApiModelProperty(name = "balance", value = "代币的余额", required = false, dataType = "String")
	private String balance;
	
	@ApiModelProperty(name = "tokenId", value = "代币的 MongoDB _id，用于唯一标识该代币", required = false, dataType = "String")
	private String tokenId;
	
	@ApiModelProperty(name = "buildByService", value = "标识返回数据是由哪个服务生成的", required = false, dataType = "String")
	private String buildByService;

}