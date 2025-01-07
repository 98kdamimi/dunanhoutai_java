package com.junyang.entity.version;
import org.springframework.data.mongodb.core.mapping.Document;

import com.baomidou.mybatisplus.annotation.TableField;
import com.junyang.query.PageQueryHelperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Document(collection = "version_db")
@ApiModel(value = "版本管理",description = "版本管理")
public class ReleaseEntity{
	
	@TableField("id")
	@ApiModelProperty(name = "id", value = "数据库主键", required = false, dataType = "String")
	private String id;
	
	@TableField("hardware")
	@ApiModelProperty(name = "hardware", value = "硬件版本信息", required = false, dataType = "HardwareEntity")
	private HardwareEntity hardware;
	
	@TableField("software")
	@ApiModelProperty(name = "software", value = "软件版本信息", required = false, dataType = "SoftwareEntity")
	private SoftwareEntity software;
	
	@TableField("forceUpdateLable")
	@ApiModelProperty(name = "forceUpdateLable", value = "强制更新标志", required = false, dataType = "ForceUpdateLable")
	private Integer forceUpdateLable;
	
	@TableField("createdAt")
	@ApiModelProperty(name = "createdAt", value = "创建时间", required = false, dataType = "String")
	private String createdAt;
	
	@TableField("updatedAt")
	@ApiModelProperty(name = "updatedAt", value = "更新时间", required = false, dataType = "String")
	private String updatedAt;

}
