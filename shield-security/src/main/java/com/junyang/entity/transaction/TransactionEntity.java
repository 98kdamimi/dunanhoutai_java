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
	
	@TableField("txid")
	@ApiModelProperty(name = "txid", value = "交易哈希（唯一标识）", required = false, dataType = "String")
	private String txid;
	
	@TableField("accountId")
	@ApiModelProperty(name = "accountId", value = "账户id", required = false, dataType = "String")
	private String accountId;
	
	@TableField("fromAddress")
	@ApiModelProperty(name = "fromAddress", value = "发送地址", required = false, dataType = "String")
	private String fromAddress;
	
	@TableField("toAddress")
	@ApiModelProperty(name = "toAddress", value = "接收地址", required = false, dataType = "String")
	private String toAddress;
	
	@TableField("amount")
	@ApiModelProperty(name = "amount", value = "转账金额", required = false, dataType = "String")
	private String amount;
	
	@TableField("networkId")
	@ApiModelProperty(name = "networkId", value = "网络id", required = false, dataType = "String")
	private String networkId;
	
	@TableField("networkName")
	@ApiModelProperty(name = "networkName", value = "网络名称", required = false, dataType = "String")
	private String networkName;
	
	@TableField("tokenId")
	@ApiModelProperty(name = "tokenId", value = "代币id", required = false, dataType = "String")
	private String tokenId;
	
	@TableField("tokenLogo")
	@ApiModelProperty(name = "tokenLogo", value = "代币图标", required = false, dataType = "String")
	private String tokenLogo;
	
	@TableField("tokenName")
	@ApiModelProperty(name = "tokenName", value = "代币名称", required = false, dataType = "String")
	private String tokenName;
	
	@TableField("type")
	@ApiModelProperty(name = "type", value = "交易类型", required = false, dataType = "String")
	private String type;
	
	@TableField("direction")
	@ApiModelProperty(name = "direction", value = "交易方向，转入转出", required = false, dataType = "String")
	private String direction;
	
	@TableField("timestamp")  
	@ApiModelProperty(name = "timestamp", value = "交易时间", required = false, dataType = "String")
	private String timestamp;
	
	@TableField("createdAt")
	@ApiModelProperty(name = "createdAt", value = "记录创建时间", required = false, dataType = "String")
	private String createdAt;
	
	@TableField("updatedAt")
	@ApiModelProperty(name = "updatedAt", value = "更新时间", required = false, dataType = "String")
	private String updatedAt;
}
