package com.junyang.entity.transaction;
import org.springframework.data.mongodb.core.mapping.Document;
import com.baomidou.mybatisplus.annotation.TableField;
import com.junyang.query.PageQueryHelperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Document(collection = "transactions")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "交易流水",description = "交易流水")
public class TransactionEntity extends PageQueryHelperEntity{
	
	@TableField("id")
	@ApiModelProperty(name = "id", value = "主键", required = false, dataType = "String")
	private String id;
	
	@TableField("transactionHash")
	@ApiModelProperty(name = "transactionHash", value = "交易哈希（唯一标识）", required = false, dataType = "String")
	private String transactionHash;
	
	@TableField("fromAddress")
	@ApiModelProperty(name = "fromAddress", value = "发送地址", required = false, dataType = "String")
	private String fromAddress;
	
	@TableField("toAddress")
	@ApiModelProperty(name = "toAddress", value = "接收地址", required = false, dataType = "String")
	private String toAddress;
	
	@TableField("amount")
	@ApiModelProperty(name = "amount", value = "转账金额", required = false, dataType = "String")
	private String amount;
	
	@TableField("fee")
	@ApiModelProperty(name = "fee", value = "手续费", required = false, dataType = "String")
	private String fee;
	
	@TableField("tokenAddress")
	@ApiModelProperty(name = "tokenAddress", value = "代币合约地址", required = false, dataType = "Long")
	private Long tokenAddress;
	
	@TableField("tokenSymbol")
	@ApiModelProperty(name = "tokenSymbol", value = "代币符号", required = false, dataType = "String")
	private String tokenSymbol;
	
	@TableField("tokenDecimals")
	@ApiModelProperty(name = "tokenDecimals", value = "代币的精度", required = false, dataType = "String")
	private String tokenDecimals;
	
	@TableField("transactionType")
	@ApiModelProperty(name = "transactionType", value = "交易类型：原生币（如ETH）或 ERC-20代币", required = false, dataType = "String")
	private String transactionType;
	
	@TableField("network")
	@ApiModelProperty(name = "network", value = "区块链网络", required = false, dataType = "String")
	private String network;
	
	@TableField("timestamp")
	@ApiModelProperty(name = "timestamp", value = "交易时间", required = false, dataType = "String")
	private String timestamp;
	
	@TableField("blockNumber")
	@ApiModelProperty(name = "blockNumber", value = "区块号", required = false, dataType = "String")
	private String blockNumber;
	
	@TableField("status")
	@ApiModelProperty(name = "status", value = "交易状态", required = false, dataType = "String")
	private String status;
	
	@TableField("createdAt")
	@ApiModelProperty(name = "createdAt", value = "记录创建时间", required = false, dataType = "String")
	private String createdAt;
	
	@TableField("updatedAt")
	@ApiModelProperty(name = "updatedAt", value = "更新时间", required = false, dataType = "String")
	private String updatedAt;
}
