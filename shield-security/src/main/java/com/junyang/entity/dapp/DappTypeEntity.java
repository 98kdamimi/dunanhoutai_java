package com.junyang.entity.dapp;
import org.springframework.data.mongodb.core.mapping.Document;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Document(collection = "dapp_type")
@ApiModel(value = "Dapp发现页分类",description = "Dapp发现页分类")
public class DappTypeEntity {
	
	@TableField("id")
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "String")
	private String id;
	
	@TableField("name")
	@ApiModelProperty(name = "name", value = "分类名称", required = false, dataType = "String")
	private String name;
	
	@TableField("type")
	@ApiModelProperty(name = "type", value = "分类", required = false, dataType = "String")
	private String type;
	
	@TableField("createdAt")
	@ApiModelProperty(name = "createdAt", value = "创建时间", required = false, dataType = "String")
	private String createdAt;
	
	@TableField("updatedAt")
	@ApiModelProperty(name = "updatedAt", value = "更新时间", required = false, dataType = "String")
	private String updatedAt;
	
	@TableField("localization")
	@ApiModelProperty(name = "localization", value = "语言", required = false, dataType = "DappTypeLocalizationEntity")
	private JSONObject localization;
}
