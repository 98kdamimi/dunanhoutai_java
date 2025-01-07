package com.junyang.entity.wallet;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.baomidou.mybatisplus.annotation.TableField;
import com.junyang.query.PageQueryHelperEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Document(collection = "walletbalance")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "钱包余额",description = "钱包余额")
public class WalletBalanceEntity extends PageQueryHelperEntity{
	
	@TableField("id")
	@ApiModelProperty(name = "id", value = "主键", required = false, dataType = "String")
	private String id;
	
	@TableField("network")
	@ApiModelProperty(name = "network", value = "所属网络", required = false, dataType = "String")
	private String network;
	
	@TableField("walletAddress")
	@ApiModelProperty(name = "walletAddress", value = "钱包地址", required = false, dataType = "String")
	private String walletAddress;
	
	@TableField("createdAt")
	@ApiModelProperty(name = "createdAt", value = "创建时间", required = false, dataType = "String")
	private String createdAt;
	
	@TableField("updatedAt")
	@ApiModelProperty(name = "updatedAt", value = "修改时间", required = false, dataType = "String")
	private String updatedAt;
	
	@TableField("tokenBalan")
	@ApiModelProperty(name = "tokenBalan", value = "代币余额", required = false, dataType = "String")
	private List<TokenBalanceEntity> tokenBalan;
	
	

}
