package com.junyang.entity.dapp;

import org.springframework.data.mongodb.core.mapping.Document;
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
	
	@TableField("createdAt")
	@ApiModelProperty(name = "createdAt", value = "创建时间", required = false, dataType = "String")
	private String createdAt;
	
	@TableField("path")
	@ApiModelProperty(name = "path", value = "地址", required = false, dataType = "String")
	private String path;
	
	@TableField("remark")
	@ApiModelProperty(name = "remark", value = "描述", required = false, dataType = "String")
	private String remark;
	
	@TableField("status")
	@ApiModelProperty(name = "status", value = "状态", required = false, dataType = "String")
	private String status;
	
	@TableField("updatedAt")
	@ApiModelProperty(name = "updatedAt", value = "更新时间", required = false, dataType = "String")
	private String updatedAt;
	
	@TableField("version")
	@ApiModelProperty(name = "version", value = "版本", required = false, dataType = "String")
	private String version;

}
