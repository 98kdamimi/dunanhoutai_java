package com.junyang.entity.instance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InstanceAccountEntity {

	@ApiModelProperty(name = "address", value = "地址", required = false, dataType = "String")
	private String address;
	
	@ApiModelProperty(name = "coinType", value = "", required = false, dataType = "String")
	private String coinType;
	
	@ApiModelProperty(name = "id", value = "", required = false, dataType = "String")
	private String id;
	
	@ApiModelProperty(name = "name", value = "", required = false, dataType = "String")
	private String name;
	
	@ApiModelProperty(name = "path", value = "", required = false, dataType = "String")
	private String path;
	
	@ApiModelProperty(name = "type", value = "", required = false, dataType = "String")
	private String type;
	
	@ApiModelProperty(name = "walletType", value = "", required = false, dataType = "String")
	private String walletType;
	
}
