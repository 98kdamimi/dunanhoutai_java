package com.junyang.entity.appuser;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.baomidou.mybatisplus.annotation.TableField;
import com.junyang.query.PageQueryHelperEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Document(collection = "appusers")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "app用户",description = "app用户")
public class AppUserEntity extends PageQueryHelperEntity{
	
	@TableField("id")
	@ApiModelProperty(name = "id", value = "主键", required = false, dataType = "String")
	private String id;
	
	@TableField("userIp")
	@ApiModelProperty(name = "userIp", value = "用户ip", required = false, dataType = "String")
	private String userIp;
	
	@TableField("deviceId")
	@ApiModelProperty(name = "deviceId", value = "设备id,唯一值", required = false, dataType = "String")
	private String deviceId;
	
	@TableField("deviceModel")
	@ApiModelProperty(name = "deviceModel", value = "设备型号", required = false, dataType = "String")
	private String deviceModel;
	
	@TableField("ipAddress")
	@ApiModelProperty(name = "ipAddress", value = "归属地", required = false, dataType = "String")
	private String ipAddress;
	
	@TableField("instanceId")
	@ApiModelProperty(name = "instanceId", value = "实例id", required = false, dataType = "String")
	private String instanceId;

	@TableField("createdAt")
	@ApiModelProperty(name = "createdAt", value = "创建时间", required = false, dataType = "String")
	private Date createdAt;
	
	@TableField("updatedAt")
	@ApiModelProperty(name = "updatedAt", value = "修改时间", required = false, dataType = "String")
	private Date updatedAt;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "content", value = "查询条件", required = false, dataType = "String")
	private String content;
	
}
