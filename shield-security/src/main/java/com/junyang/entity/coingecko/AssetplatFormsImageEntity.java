package com.junyang.entity.coingecko;
import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "资产平台图片",description = "资产平台图片")
public class AssetplatFormsImageEntity {
	
	@TableField("thumb")
	@ApiModelProperty(name = "thumb", value = "thumb图", required = false, dataType = "String")
	private String thumb;
	
	@TableField("small")
	@ApiModelProperty(name = "small", value = "small图", required = false, dataType = "String")
	private String small;
	
	@TableField("large")
	@ApiModelProperty(name = "large", value = "large图", required = false, dataType = "String")
	private String large;

}
