package com.junyang.entity.coingecko;

import org.springframework.data.mongodb.core.mapping.Document;

import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Document(collection = "Coingecko_NetWork")
@ApiModel(value = "coingecko平台网络列表",description = "coingecko平台网络列表")
public class CoingeckoNetWorkAttributesEntity {
	
	@TableField("name")
	@ApiModelProperty(name = "name", value = "名称", required = false, dataType = "String")
	private String name;
	
	@TableField("coingecko_asset_platform_id")
	@ApiModelProperty(name = "coingecko_asset_platform_id", value = "id", required = false, dataType = "String")
	private String coingecko_asset_platform_id;

}
