package com.junyang.entity.delegate;

import org.springframework.data.mongodb.core.mapping.Document;

import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Document(collection = "trondelegateconfigs")
@ApiModel(value = "波场能量委托配置",description = "波场能量委托配置")
public class TronDelegateCofigEntity {
	
	@TableField("id")
	@ApiModelProperty(name = "id", value = "主键", required = false, dataType = "String")
	private String id;
	
	@TableField("freeNum")
	@ApiModelProperty(name = "freeNum", value = "每日免费次数", required = false, dataType = "String")
	private Integer freeNum = 3;
	
	@TableField("maxTrxNum")
	@ApiModelProperty(name = "maxTrxNum", value = "每日委托量上限", required = false, dataType = "Double")
	private Double maxTrxNum;
	
	@TableField("everyTrxNum")
	@ApiModelProperty(name = "everyTrxNum", value = "每笔委托数量", required = false, dataType = "Double")
	private Double everyTrxNum;
	
	@TableField("invalidationTime")
	@ApiModelProperty(name = "invalidationTime", value = "失效时长", required = false, dataType = "String")
	private Integer invalidationTime =50000;
	
	@TableField("energyType")
	@ApiModelProperty(name = "energyType", value = "委托类型", required = false, dataType = "String")
	private String energyType ="BANDWIDTH";
	 
	@TableField("createdAt")
	@ApiModelProperty(name = "createdAt", value = "创建时间", required = false, dataType = "String")
	private String createdAt;
	
	@TableField("updatedAt")
	@ApiModelProperty(name = "updatedAt", value = "修改时间", required = false, dataType = "String")
	private String updatedAt;

}
