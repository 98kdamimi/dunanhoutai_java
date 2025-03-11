package com.junyang.entity.delegate;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.baomidou.mybatisplus.annotation.TableField;
import com.junyang.query.PageQueryHelperEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "trondelegatelists")
@ApiModel(value = "波场能量委托记录",description = "波场能量委托记录")
public class TronDelegatelistEntity extends PageQueryHelperEntity{
	
	@TableField("id")
	@ApiModelProperty(name = "id", value = "主键", required = false, dataType = "String")
	private String id;

	@TableField("receiverAddress")
	@ApiModelProperty(name = "receiverAddress", value = "钱包地址", required = false, dataType = "String")
	private String receiverAddress;
	
	@TableField("amount")
	@ApiModelProperty(name = "amount", value = "委托数量", required = false, dataType = "String")
	private String amount;
	
	@TableField("resourceType")
	@ApiModelProperty(name = "resourceType", value = "委托类型", required = false, dataType = "String")
	private String resourceType;
	
	@TableField("transactionId")
	@ApiModelProperty(name = "transactionId", value = "交易id", required = false, dataType = "String")
	private String transactionId;
	
	@TableField("tradeState")
	@ApiModelProperty(name = "tradeState", value = "交易类型", required = false, dataType = "String")
	private String tradeState;
	
	@TableField("createdAt")
	@ApiModelProperty(name = "createdAt", value = "创建时间", required = false, dataType = "String")
	private String createdAt;
	
	@TableField("updatedAt")
	@ApiModelProperty(name = "updatedAt", value = "修改时间", required = false, dataType = "String")
	private String updatedAt;
	
	@TableField("resourceAmount")
	@ApiModelProperty(name = "resourceAmount", value = "委托资源量", required = false, dataType = "String")
	private String resourceAmount;
	
	@TableField("timestamp")
	@ApiModelProperty(name = "timestamp", value = "修改时间", required = false, dataType = "String")
	private Date timestamp;

}
