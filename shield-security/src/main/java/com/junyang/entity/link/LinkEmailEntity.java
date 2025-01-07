package com.junyang.entity.link;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.baomidou.mybatisplus.annotation.TableField;
import com.junyang.query.PageQueryHelperEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Document(collection = "link_email")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "联系邮箱",description = "联系邮箱")
public class LinkEmailEntity extends PageQueryHelperEntity{
	
	@Id
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "String")
	private String id;
	
	@TableField("language")
	@ApiModelProperty(name = "language", value = "语言", required = false, dataType = "String")
	private String language;
	
	@TableField("contentData")
	@ApiModelProperty(name = "contentData", value = "内容", required = false, dataType = "String")
	private String contentData;
	
	@TableField("email")
	@ApiModelProperty(name = "email", value = "邮箱", required = false, dataType = "String")
	private String email;
	
	@TableField("set_time")
	@ApiModelProperty(name = "setTime",value = "创建时间",required = false,dataType = "Date")
    private Date setTime;
	
	@TableField("gmt_modified")
	@ApiModelProperty(name = "gmtModified",value = "更新时间",required = false,dataType = "Date")
	private Date gmtModified;

}
