package com.junyang.entity.system;

import java.util.Date;
import org.springframework.data.mongodb.core.mapping.Document;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Document(collection = "user_agreement")
@ApiModel(value = "用户协议",description = "用户协议")
public class UserAgreementEntity {
	
	@TableField("id")
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "String")
	private String id;
	
	@TableField("typeId")
	@ApiModelProperty(name = "typeId", value = "协议分类", required = false, dataType = "Integer")
	private Integer typeId;
	
	@TableField("typeName")
	@ApiModelProperty(name = "typeName", value = "协议分类名称", required = false, dataType = "String")
	private String typeName;
	
	@TableField("languageType")
	@ApiModelProperty(name = "languageType", value = "语言", required = false, dataType = "String")
	private String languageType;
	
	@TableField("contentInfo")
	@ApiModelProperty(name = "contentInfo", value = "内容", required = false, dataType = "String")
	private String contentInfo;
	
	@TableField("htmlSite")
	@ApiModelProperty(name = "htmlSite", value = "访问地址", required = false, dataType = "String")
	private String htmlSite;
	
	@TableField("set_time")
	@ApiModelProperty(name = "setTime",value = "创建时间",required = false,dataType = "Date")
    private Date setTime;
	
	@TableField("gmt_modified")
	@ApiModelProperty(name = "gmtModified",value = "更新时间",required = false,dataType = "Date")
	private Date gmtModified;

}
