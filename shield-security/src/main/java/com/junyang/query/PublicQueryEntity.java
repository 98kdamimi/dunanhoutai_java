package com.junyang.query;

import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "公共查询条件",description = "公共查询条件")
public class PublicQueryEntity extends PageQueryHelperEntity{
	
	@TableField(exist = false)
	@ApiModelProperty(name = "begTime",value = "查询条件开始时间",required = true,dataType = "String")
    private String begTime;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "endTime",value = "查询条件结束时间",required = true,dataType = "String")
    private String endTime;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "begMonth",value = "查询条件开始月份",required = true,dataType = "String")
    private String begMonth;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "endMonth",value = "查询条件结束月份",required = true,dataType = "String")
    private String endMonth;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "serialNumber",value = "序列号",required = true,dataType = "String")
    private String serialNumber;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "status",value = "状态",required = true,dataType = "String")
    private String status;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "title",value = "标题",required = true,dataType = "String")
    private String title;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "typeId",value = "分类",required = true,dataType = "Integer")
    private String typeId;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "name",value = "名称",required = true,dataType = "Integer")
    private String name;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "chainId",value = "网络id",required = true,dataType = "String")
    private String chainId;
	

	@TableField(exist = false)
	@ApiModelProperty(name = "type",value = "分类",required = true,dataType = "Integer")
    private String type;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "impl",value = "实现方法",required = true,dataType = "String")
    private String impl;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "version",value = "版本",required = true,dataType = "String")
    private String version;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "language",value = "语言",required = true,dataType = "String")
    private String language;

}
