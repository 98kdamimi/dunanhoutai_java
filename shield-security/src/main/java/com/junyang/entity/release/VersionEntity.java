package com.junyang.entity.release;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.junyang.query.PublicQueryEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("version_db")
@Document(collection = "version_db")
@ApiModel(value = "版本信息",description = "版本信息")
public class VersionEntity extends PublicQueryEntity  {
	
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "String")
	private Integer id;
	
	@TableField("version_content")
	@ApiModelProperty(name = "versionContent", value = "版本描述", required = false, dataType = "String")
	private String versionContent;
	
	@TableField("release_state")
	@ApiModelProperty(name = "releaseState", value = "发行状态", required = false, dataType = "Integer")
	private Integer releaseState;
	
	@TableField("set_time")
	@ApiModelProperty(name = "setTime",value = "创建时间",required = false,dataType = "Date")
    private Date setTime;
	
	@TableField("gmt_modified")
	@ApiModelProperty(name = "gmtModified",value = "更新时间",required = false,dataType = "Date")
	private Date gmtModified;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "ios",value = "ios版本信息",required = false,dataType = "VersionIosEntity")
    private VersionIosEntity ios;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "android",value = "安卓版本信息",required = false,dataType = "VersionAndroidEntity")
    private VersionAndroidEntity android;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "digtalshield",value = "设备版本信息",required = false,dataType = "VersionDigtalshieEntity")
    private VersionDigtalshieEntity digtalshield;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "versionTypeList",value = "版本更新类型",required = false,dataType = "List<VersionTypeEntity>")
    private List<VersionTypeEntity> versionTypeList;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "VersionTypeIdList",value = "版本更新类型id集合",required = false,dataType = "List<Integer>")
    private List<Integer> VersionTypeIdList;
	
	
	
	

}
