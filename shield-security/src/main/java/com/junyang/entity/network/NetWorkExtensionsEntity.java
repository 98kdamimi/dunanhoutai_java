package com.junyang.entity.network;

import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "网络扩展信息",description = "网络扩展信息")
public class NetWorkExtensionsEntity {
	
	@ApiModelProperty(name = "position", value = "排序位置", required = false, dataType = "int")
	private int position;
	
	@ApiModelProperty(name = "defaultStableTokens", value = "提供商选项", required = false, dataType = "List<String>")
	private List<String> defaultStableTokens;
	
	@ApiModelProperty(name = "position", value = "提供商选项", required = false, dataType = "NetWorkProviderOptionsEntity")
	private NetWorkProviderOptionsEntity providerOptions;
    

}
