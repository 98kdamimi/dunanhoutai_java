package com.junyang.entity.wallet;

import java.util.Date;
import org.springframework.data.mongodb.core.mapping.Document;

import com.baomidou.mybatisplus.annotation.TableField;
import com.junyang.query.PageQueryHelperEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Document(collection = "walletbalanceinfos")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "钱包余额明细",description = "钱包余额明细")
public class WalletBalanceInfoEntity extends PageQueryHelperEntity{
	
	@TableField("id")
	@ApiModelProperty(name = "id", value = "主键", required = false, dataType = "String")
	private String id;
	
	@TableField("accountId")
	@ApiModelProperty(name = "accountId", value = "账户id", required = false, dataType = "String")
	private String accountId;
	
	@TableField("networkId")
	@ApiModelProperty(name = "networkId", value = "网络id", required = false, dataType = "String")
	private String networkId;
	
	@TableField("networkName")
	@ApiModelProperty(name = "networkName", value = "网络名称", required = false, dataType = "String")
	private String networkName;
	
	@TableField("walletAddress")
	@ApiModelProperty(name = "walletAddress", value = "钱包地址", required = false, dataType = "String")
	private String walletAddress;
	
	@TableField("walletSum")
	@ApiModelProperty(name = "walletSum", value = "钱包总额", required = false, dataType = "String")
	private String walletSum;
	
	@TableField("address")
	@ApiModelProperty(name = "address", value = "合约地址", required = false, dataType = "String")
	private String address;
	
	@TableField("coingeckoId")
	@ApiModelProperty(name = "coingeckoId", value = "coingeckoId", required = false, dataType = "String")
	private String coingeckoId;
	
	@TableField("key")
	@ApiModelProperty(name = "key", value = "唯一值", required = false, dataType = "String")
	private String key;
	
	@TableField("logoURI")
	@ApiModelProperty(name = "logoURI", value = "代币logo", required = false, dataType = "String")
	private String logoURI;
	
	@TableField("name")
	@ApiModelProperty(name = "name", value = "代币名称", required = false, dataType = "String")
	private String name;
	
	@TableField("balance")
	@ApiModelProperty(name = "balance", value = "区块", required = false, dataType = "String")
	private String balance;
	
	@TableField("usdValue")
	@ApiModelProperty(name = "usdValue", value = "转换usdt后金额", required = false, dataType = "String")
	private String usdValue;
	
	@TableField("value")
	@ApiModelProperty(name = "value", value = "代币数量", required = false, dataType = "String")
	private String value;
	
	@TableField("price")
	@ApiModelProperty(name = "price", value = "当前价格", required = false, dataType = "String")
	private String price;
	
	@TableField("price24h")
	@ApiModelProperty(name = "price24h", value = "24小时价格", required = false, dataType = "String")
	private String price24h;
	
	@TableField("risklevel")
	@ApiModelProperty(name = "risklevel", value = "", required = false, dataType = "String")
	private String risklevel;
	
	@TableField("symbol")
	@ApiModelProperty(name = "symbol", value = "实现方式", required = false, dataType = "String")
	private String symbol;
	
	@TableField("createdAt")
	@ApiModelProperty(name = "createdAt", value = "创建时间", required = false, dataType = "String")
	private Date createdAt;
	
	@TableField("updatedAt")
	@ApiModelProperty(name = "updatedAt", value = "修改时间", required = false, dataType = "String")
	private Date updatedAt;
}
