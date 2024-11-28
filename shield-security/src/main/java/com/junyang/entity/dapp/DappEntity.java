package com.junyang.entity.dapp;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Document(collection = "dapp_db")
@ApiModel(value = "Dapp发现页",description = "Dapp发现页")
public class DappEntity {
	
	@TableField("id")
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "String")
	private String id;
	
//	@TableField(exist = false)
//	@ApiModelProperty(name = "_id", value = "唯一标识符", required = false, dataType = "String")
//	private String _id;
	
	@TableField("logoURL")
	@ApiModelProperty(name = "logoURL", value = "log地址", required = false, dataType = "String")
	private String logoURL;
	
	@TableField("name")
	@ApiModelProperty(name = "name", value = "名称", required = false, dataType = "String")
	private String name;
	
	@TableField("status")
	@ApiModelProperty(name = "status", value = "状态", required = false, dataType = "String")
	private String status;
	
	@TableField("subtitle")
	@ApiModelProperty(name = "subtitle", value = "标题", required = false, dataType = "String")
	private String subtitle;
	
	@TableField("url")
	@ApiModelProperty(name = "url", value = "url地址", required = false, dataType = "String")
	private String url;
	
	@TableField("createdAt")
	@ApiModelProperty(name = "createdAt", value = "创建时间", required = false, dataType = "String")
	private String createdAt;
	
	@TableField("updatedAt")
	@ApiModelProperty(name = "updatedAt", value = "更新时间", required = false, dataType = "String")
	private String updatedAt;

	@TableField("__v")
	@ApiModelProperty(name = "__v", value = "版本", required = false, dataType = "String")
	private Integer __v;
	
	@TableField("networkIds")
	@ApiModelProperty(name = "networkIds", value = "网络id", required = false, dataType = "String")
	private List<String> networkIds;
	
	@TableField("tagIds")
	@ApiModelProperty(name = "tagIds", value = "标签id", required = false, dataType = "String")
	private List<String> tagIds;
	
	@TableField("categoryIds")
	@ApiModelProperty(name = "categoryIds", value = "类型id", required = false, dataType = "String")
	private List<String> categoryIds;
	
	@TableField("localization")
	@ApiModelProperty(name = "localization", value = "语言", required = false, dataType = "String")
	private JSONObject localization;

}
