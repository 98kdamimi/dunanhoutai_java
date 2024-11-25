package com.junyang.entity.token;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Document(collection = "token_db")
@ApiModel(value = "代币信息",description = "代币信息")
public class TokenEntity {
	
	@TableField("id")
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "String")
	private String id;
	
	@TableField("__v")
	@ApiModelProperty(name = "__v", value = "文档版本", required = false, dataType = "String")
	private String __v;
	
	@TableField("addToIndex")
	@ApiModelProperty(name = "addToIndex", value = "否将该加密货币添加到索引", required = false, dataType = "boolean")
	private boolean addToIndex;
	
	@TableField("address")
	@ApiModelProperty(name = "address", value = "合约地址", required = false, dataType = "String")
	private String address;
	
	@TableField("chainId")
	@ApiModelProperty(name = "chainId", value = "区块链的 ID", required = false, dataType = "String")
	private String chainId;
	
	@TableField("checked")
	@ApiModelProperty(name = "checked", value = "币是否已通过验证", required = false, dataType = "boolean")
	private boolean checked;
	
	@TableField("cmcId")
	@ApiModelProperty(name = "cmcId", value = "CoinMarketCap 上该代币的唯一 ID", required = false, dataType = "Integer")
	private Integer cmcId;
	
	@TableField("coingeckoId")
	@ApiModelProperty(name = "coingeckoId", value = "CoinGecko 上的唯一标识符", required = false, dataType = "String")
	private String coingeckoId;
	
	@TableField("createdAt")
	@ApiModelProperty(name = "createdAt", value = "创建时间", required = false, dataType = "String")
	private String createdAt;
	
	@TableField("decimals")
	@ApiModelProperty(name = "decimals", value = "代币的精度", required = false, dataType = "Integer")
	private Integer decimals;
	
	@TableField("impl")
	@ApiModelProperty(name = "impl", value = "实现平台", required = false, dataType = "String")
	private String impl;
	
	@TableField("isNative")
	@ApiModelProperty(name = "isNative", value = "是否是区块链的原生代币", required = false, dataType = "boolean")
	private boolean isNative;
	
	@TableField("logoURI")
	@ApiModelProperty(name = "logoURI", value = "代币的 Logo", required = false, dataType = "String")
	private String logoURI;
	
	@TableField("marketCap")
	@ApiModelProperty(name = "marketCap", value = "代币的市场总值", required = false, dataType = "String")
	private String marketCap;
	
	@TableField("name")
	@ApiModelProperty(name = "name", value = "代币的名称", required = false, dataType = "String")
	private String name;

	@TableField("riskLevel")
	@ApiModelProperty(name = "riskLevel", value = "代币的风险等级", required = false, dataType = "String")
	private String riskLevel;
	
	@TableField("security")
	@ApiModelProperty(name = "security", value = "是否具有一定的安全性保障", required = false, dataType = "boolean")
	private boolean security;
	
	@TableField("status")
	@ApiModelProperty(name = "status", value = "代币的当前状态", required = false, dataType = "String")
	private String status;
	
	@TableField("symbol")
	@ApiModelProperty(name = "symbol", value = "代币的符号", required = false, dataType = "String")
	private String symbol;
	
	@TableField("updatedAt")
	@ApiModelProperty(name = "updatedAt", value = "最后更新时间", required = false, dataType = "String")
	private String updatedAt;
	
	@TableField("verified")
	@ApiModelProperty(name = "verified", value = "否已通过验证", required = false, dataType = "boolean")
	private boolean verified;
	
	@TableField("source")
	@ApiModelProperty(name = "source", value = "信息来源", required = false, dataType = "List<String>")
	private List<String> source;

}
