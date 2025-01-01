package com.junyang.entity.help;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.baomidou.mybatisplus.annotation.TableField;
import com.junyang.query.PageQueryHelperEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Document(collection = "help_db")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "个人中心帮助菜单",description = "个人中心帮助菜单")
public class HelpEntity extends PageQueryHelperEntity{
	
	@TableField("id")
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "String")
	private String id;
	
	@TableField("iconImg")
	@ApiModelProperty(name = "iconImg", value = "图标", required = false, dataType = "String")
	private String iconImg;
	
	@TableField("language")
	@ApiModelProperty(name = "language", value = "语言", required = false, dataType = "String")
	private String language;
	
	@TableField("menuName")
	@ApiModelProperty(name = "menuName", value = "菜单名称", required = false, dataType = "String")
	private String menuName;
	
	@TableField("toUrl")
	@ApiModelProperty(name = "toUrl", value = "跳转地址", required = false, dataType = "String")
	private String toUrl;
	
	@TableField("set_time")
	@ApiModelProperty(name = "setTime",value = "创建时间",required = false,dataType = "Date")
    private Date setTime;
	
	@TableField("gmt_modified")
	@ApiModelProperty(name = "gmtModified",value = "更新时间",required = false,dataType = "Date")
	private Date gmtModified;

}
