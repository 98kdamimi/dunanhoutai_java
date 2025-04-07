package com.junyang.entity.monitorEvent;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Document(collection = "addressesJpush")
@ApiModel(value = "jpush账户绑定关系",description = "jpush账户绑定关系")
public class AddressesJpushEntity {
	
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "String")
	private String id;
	
	@ApiModelProperty(name = "registrationId", value = "设备id", required = false, dataType = "String")
	private String registrationId;
	
	@ApiModelProperty(name = "addresses", value = "账户地址", required = false, dataType = "List<String>")
	private List<String> addresses;
	
	@TableField("set_time")
	@ApiModelProperty(name = "setTime",value = "创建时间",required = false,dataType = "Date")
    private Date setTime;
	
	@TableField("gmt_modified")
	@ApiModelProperty(name = "gmtModified",value = "更新时间",required = false,dataType = "Date")
	private Date gmtModified;

}
