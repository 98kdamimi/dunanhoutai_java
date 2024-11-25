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
@TableName("version_android_google")
@Document(collection = "version_android_google")
@ApiModel(value = "安卓Google Play 商店中应用的版本信息",description = "安卓Google Play 商店中应用的版本信息")
public class VersionAndroidGoogleEntity {
	
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "String")
	private Integer id;
	
	@TableField("android_version_id")
	@ApiModelProperty(name = "androidVersionId", value = "安卓版本表id", required = false, dataType = "Integer")
	private Integer androidVersionId;
	
	@TableField("google_url")
	@ApiModelProperty(name = "url", value = "Google Play 商店中的应用链接", required = false, dataType = "String")
	private String url;
	
	@TableField("google_version")
	@ApiModelProperty(name = "googleVersion", value = "Google Play 商店中的应用版本号", required = false, dataType = "String")
	private String googleVersion;
	
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

}
