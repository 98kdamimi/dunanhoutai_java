package com.junyang.entity.carouse;
import java.util.Date;
import org.springframework.data.mongodb.core.mapping.Document;
import com.baomidou.mybatisplus.annotation.TableField;
import com.junyang.query.PageQueryHelperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "carousels")
@ApiModel(value = "轮播图",description = "轮播图")
public class CarouselEntity extends PageQueryHelperEntity{
	
	@TableField("id")
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "String")
	private String id;
	
	@TableField("logoURI")
	@ApiModelProperty(name = "logoURI", value = "图片地址", required = false, dataType = "String")
	private String logoURI;
	
	@TableField("sortType")
	@ApiModelProperty(name = "sortType", value = "分类 1轮播 2广告位", required = false, dataType = "String")
	private String sortType;
	
	@TableField("language")
	@ApiModelProperty(name = "language", value = "语言", required = false, dataType = "String")
	private String language;
	
	@TableField("toUrl")
	@ApiModelProperty(name = "toUrl", value = "跳转地址", required = false, dataType = "String")
	private String toUrl;
	
	@TableField("setTime")
	@ApiModelProperty(name = "setTime",value = "创建时间",required = false,dataType = "Date")
    private Date setTime;
	
	@TableField("gmt_modified")
	@ApiModelProperty(name = "gmtModified",value = "更新时间",required = false,dataType = "Date")
	private Date gmtModified;
	
	@TableField("createdAt")
	@ApiModelProperty(name = "createdAt",value = "创建时间",required = false,dataType = "Date")
    private String createdAt;
	
	@TableField("updatedAt")
	@ApiModelProperty(name = "updatedAt",value = "更新时间",required = false,dataType = "Date")
	private String updatedAt;

}
