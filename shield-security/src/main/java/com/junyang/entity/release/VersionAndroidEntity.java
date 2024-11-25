package com.junyang.entity.release;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("version_android")
@Document(collection = "version_android")
@ApiModel(value = "安卓版本信息",description = "安卓版本信息")
public class VersionAndroidEntity {
	
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "String")
	private Integer id;
	
	@TableField("version_id")
	@ApiModelProperty(name = "versionId", value = "版本管理主表id", required = false, dataType = "String")
	private Integer versionId;
	
	@TableField("android_url")
	@ApiModelProperty(name = "url", value = "下载地址", required = false, dataType = "String")
	private String url;
	
	@TableField("android_version")
	@ApiModelProperty(name = "androidVersion", value = "版本号", required = false, dataType = "String")
	private String androidVersion;
	
	@TableField("android_googlePlay")
	@ApiModelProperty(name = "googlePlay", value = "Google Play 商店中应用的链接", required = false, dataType = "String")
	private String googlePlay;
	
	@TableField("release_state")
	@ApiModelProperty(name = "releaseState", value = "发行状态", required = false, dataType = "Integer")
	private Integer releaseState;
	
	@TableField("set_time")
	@ApiModelProperty(name = "setTime",value = "创建时间",required = true,dataType = "Date")
    private Date setTime;
	
	@TableField("gmt_modified")
	@ApiModelProperty(name = "gmtModified",value = "更新时间",required = true,dataType = "Date")
	private Date gmtModified;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "version", value = "版本号", required = false, dataType = "String")
	private List<Integer> version;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "google", value = "安卓Google Play 商店中应用的版本信息", required = false, dataType = "VersionAndroidGoogleEntity")
	private VersionAndroidGoogleEntity google;

}
