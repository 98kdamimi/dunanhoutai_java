package com.junyang.entity.release;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("version_digtalshie_firmware")
@Document(collection = "version_digtalshie_firmware")
@ApiModel(value = "设备版本信息",description = "设备版本信息")
public class VersionDigtalshieFirmwareEntity {
	
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "String")
	private Integer id;
	
	@TableField("digtalshie_Id")
	@ApiModelProperty(name = "digtalshieId", value = "设备主表唯一标识符", required = false, dataType = "Integer")
	private Integer digtalshieId;
	
	@TableField("firmware_required")
	@ApiModelProperty(name = "required", value = "否需要进行固件更新", required = false, dataType = "boolean")
	private boolean required;
	
	@TableField("firmware_version")
	@ApiModelProperty(name = "firmwareVersion", value = "固件的版本号", required = false, dataType = "String")
	private String firmwareVersion;
	
	@TableField("firmware_url")
	@ApiModelProperty(name = "url", value = "固件的下载链接", required = false, dataType = "String")
	private String url;
	
	@TableField("firmware_fingerprint")
	@ApiModelProperty(name = "fingerprint", value = "固件文件的指纹", required = false, dataType = "String")
	private String fingerprint;
	
	@TableField("release_state")
	@ApiModelProperty(name = "releaseState", value = "发行状态", required = false, dataType = "Integer")
	private Integer releaseState;
	
	@TableField("release_changelog")
	@ApiModelProperty(name = "releaseChangelog", value = "版本说明", required = false, dataType = "JSONObject")
	private String releaseChangelog;
	
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
	@ApiModelProperty(name = "changelog", value = "版本说明", required = false, dataType = "JSONObject")
	private JSONObject changelog;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "explainContent", value = "版本说明", required = false, dataType = "JSONObject")
	private String explainContent;
	

}
