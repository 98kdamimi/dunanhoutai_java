package com.junyang.entity.release;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("version_type")
@Document(collection = "version_type")
@ApiModel(value = "版本信息更新版本",description = "版本信息更新版本")
public class VersionTypeEntity {
	
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "String")
	private Integer id;
	
	@TableField("version_id")
	@ApiModelProperty(name = "versionId", value = "版本管理主表id", required = false, dataType = "String")
	private Integer versionId;
	
	@TableField("vice_id")
	@ApiModelProperty(name = "viceId", value = "关联副表id", required = false, dataType = "String")
	private Integer viceId;

	@TableField("version_type_id")
	@ApiModelProperty(name = "versionTypeId", value = "更新类型id", required = false, dataType = "Integer")
	private Integer versionTypeId;
	
	@TableField("version_type_name")
	@ApiModelProperty(name = "versionTypeName", value = "更新类型名称", required = false, dataType = "String")
	private String versionTypeName;
	
	@TableField("version_code")
	@ApiModelProperty(name = "versionCode", value = "更新版本号", required = false, dataType = "String")
	private String versionCode;
	
	@TableField("set_time")
	@ApiModelProperty(name = "setTime",value = "创建时间",required = true,dataType = "Date")
    private Date setTime;
	
	@TableField("gmt_modified")
	@ApiModelProperty(name = "gmtModified",value = "更新时间",required = true,dataType = "Date")
	private Date gmtModified;
	
}
