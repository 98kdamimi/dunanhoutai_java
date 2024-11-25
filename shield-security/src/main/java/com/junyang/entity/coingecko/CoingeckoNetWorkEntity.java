package com.junyang.entity.coingecko;

import org.springframework.data.mongodb.core.mapping.Document;

import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Document(collection = "coingecko_network")
@ApiModel(value = "coingecko平台网络列表",description = "coingecko平台网络列表")
public class CoingeckoNetWorkEntity {
	
	@TableField("id")
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "String")
	private String id;
	
	@TableField("type")
	@ApiModelProperty(name = "type", value = "唯一标识符", required = false, dataType = "String")
	private String type;
	
	@TableField("attributes")
	@ApiModelProperty(name = "attributes", value = "属性", required = false, dataType = "CoingeckoNetWorkAttributesEntity")
	private CoingeckoNetWorkAttributesEntity attributes;

}
