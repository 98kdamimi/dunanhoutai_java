package com.junyang.entity.system;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.baomidou.mybatisplus.annotation.TableField;
import com.junyang.entity.dapp.DappEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Document(collection = "sys_log")
@ApiModel(value = "系统日志",description = "系统日志")
public class SysLogEntity {
	
	@TableField("id")
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "String")
	private String id;
	
	@TableField("userName")
	@ApiModelProperty(name = "userName", value = "操作用户", required = false, dataType = "String")
	private String userName;
	
	@TableField("moduleName")
	@ApiModelProperty(name = "moduleName", value = "操作模块", required = false, dataType = "String")
	private String moduleName;
	
	@TableField("moduleInfo")
	@ApiModelProperty(name = "moduleInfo", value = "操作说明", required = false, dataType = "String")
	private String moduleInfo;
	
	@TableField("reqType")
	@ApiModelProperty(name = "reqType", value = "请求方式", required = false, dataType = "String")
	private String reqType;
	
	@TableField("reqUrl")
	@ApiModelProperty(name = "reqUrl", value = "接口地址", required = false, dataType = "String")
	private String reqUrl;
	
	@TableField("ipUrl")
	@ApiModelProperty(name = "ipUrl", value = "ip地址", required = false, dataType = "String")
	private String ipUrl;
	
	@TableField("reqParamet")
	@ApiModelProperty(name = "reqParamet", value = "请求参数", required = false, dataType = "String")
	private String reqParamet;
	
	@TableField("resData")
	@ApiModelProperty(name = "resData", value = "响应结果", required = false, dataType = "String")
	private String resData;
	
	@TableField("begTime")
	@ApiModelProperty(name = "begTime", value = "请求开始时间", required = false, dataType = "String")
	private Date begTime;
	
	@TableField("endTime")
	@ApiModelProperty(name = "endTime", value = "请求结束时间", required = false, dataType = "String")
	private Date endTime;
	
	

}
