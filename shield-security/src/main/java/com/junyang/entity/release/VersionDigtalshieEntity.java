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
@TableName("version_digtalshie")
@Document(collection = "version_digtalshie")
@ApiModel(value = "设备信息",description = "设备信息")
public class VersionDigtalshieEntity {
	
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "String")
	private Integer id;
	
	@TableField("version_id")
	@ApiModelProperty(name = "versionId", value = "版本管理主表id", required = false, dataType = "Integer")
	private Integer versionId;
	
	@TableField("digtalshie_id")
	@ApiModelProperty(name = "digtalshieId", value = "设备id", required = false, dataType = "String")
	private String digtalshieId;
	
	@TableField("digtalshie_name")
	@ApiModelProperty(name = "digtalshieName", value = "设备名称", required = false, dataType = "String")
	private String digtalshieName;
	
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
	@ApiModelProperty(name = "firmware",value = "设备版本信息",required = true,dataType = "List<VersionDigtalshieFirmwareEntity>")
	private List<VersionDigtalshieFirmwareEntity> firmware;
	
	

}
