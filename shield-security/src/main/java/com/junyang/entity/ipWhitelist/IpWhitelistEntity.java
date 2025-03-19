package com.junyang.entity.ipWhitelist;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.baomidou.mybatisplus.annotation.TableField;
import com.junyang.query.PageQueryHelperEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Document(collection = "ip_whitelist")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ip白名单",description = "ip白名单")
public class IpWhitelistEntity extends PageQueryHelperEntity{
	
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "String")
	private String id;
	
	@ApiModelProperty(name = "ipSite", value = "ip地址", required = false, dataType = "String")
	private String ipSite;
	
	@TableField("set_time")
	@ApiModelProperty(name = "setTime",value = "创建时间",required = false,dataType = "Date")
    private Date setTime;
	
	@TableField("gmt_modified")
	@ApiModelProperty(name = "gmtModified",value = "更新时间",required = false,dataType = "Date")
	private Date gmtModified;

}
