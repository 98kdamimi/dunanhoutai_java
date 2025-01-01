package com.junyang.entity.appconfig;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Document(collection = "appconfig_db")
@ApiModel(value = "app配置",description = "app配置")
public class AppconfigEntity{
	
	@TableField("id")
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "String")
	private String id;
	
	@TableField("version")
	@ApiModelProperty(name = "version", value = "版本号", required = false, dataType = "String")
	private String version;
	
	@TableField("app")
	@ApiModelProperty(name = "app", value = "配置内容", required = false, dataType = "JSONObject")
	private JSONObject app;
	
	@TableField("set_time")
	@ApiModelProperty(name = "setTime",value = "创建时间",required = false,dataType = "Date")
    private Date setTime;
	
	@TableField("gmt_modified")
	@ApiModelProperty(name = "gmtModified",value = "更新时间",required = false,dataType = "Date")
	private Date gmtModified;
	
	@TableField(exist = false)
	@ApiModelProperty(name = "networkIds",value = "网络id集合",required = false,dataType = "List<String>")
	private List<String> networkIds;

}
