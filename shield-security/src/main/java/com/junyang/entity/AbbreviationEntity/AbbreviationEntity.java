package com.junyang.entity.AbbreviationEntity;
import java.util.Date;
import org.springframework.data.mongodb.core.mapping.Document;
import com.baomidou.mybatisplus.annotation.TableField;
import com.junyang.query.PageQueryHelperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
//@Document(collection = "abbreviation_db")
@Document(collection = "abbreviations")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "联系方式",description = "联系方式")
public class AbbreviationEntity extends PageQueryHelperEntity{
	
	@TableField("id")
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "String")
	private String id;
	
	@TableField("language")
	@ApiModelProperty(name = "language", value = "语言", required = false, dataType = "String")
	private String language;
	
	@TableField("linkName")
	@ApiModelProperty(name = "linkName", value = "联系方式名称", required = false, dataType = "String")
	private String linkName;
	
	@TableField("toUrl")
	@ApiModelProperty(name = "toUrl", value = "跳转地址", required = false, dataType = "String")
	private String toUrl;
	
	@TableField("setTime")
	@ApiModelProperty(name = "setTime",value = "创建时间",required = false,dataType = "Date")
    private Date setTime;
	
	@TableField("createdAt")
	@ApiModelProperty(name = "createdAt",value = "创建时间",required = false,dataType = "Date")
    private String createdAt;
	
	@TableField("updatedAt")
	@ApiModelProperty(name = "updatedAt",value = "更新时间",required = false,dataType = "Date")
	private String updatedAt;

}
