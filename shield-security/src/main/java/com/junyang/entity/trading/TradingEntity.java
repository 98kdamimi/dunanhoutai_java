package com.junyang.entity.trading;

import org.springframework.data.mongodb.core.mapping.Document;

import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @category 交易相关标签开关
 * @author Hlin
 *
 */
@Data
@Document(collection = "tradinglablestates")
@ApiModel(value = "Dapp发现页标签状态",description = "Dapp发现页标签状态")
public class TradingEntity {
	
	@TableField("id")
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "String")
	private String id;
	
	@TableField("states")
	@ApiModelProperty(name = "states", value = "状态", required = false, dataType = "boolean")
	private boolean states;
	
	@TableField("lableId")
	@ApiModelProperty(name = "lableId", value = "标签id", required = false, dataType = "Integer")
	private Integer lableId;
	
	@TableField("lableName")
	@ApiModelProperty(name = "lableName", value = "标签名称", required = false, dataType = "String")
	private String lableName;
	
	@TableField("createdAt")
	@ApiModelProperty(name = "createdAt", value = "创建时间", required = false, dataType = "String")
	private String createdAt;
	
	@TableField("updatedAt")
	@ApiModelProperty(name = "updatedAt", value = "更新时间", required = false, dataType = "String")
	private String updatedAt;


}
