package com.junyang.entity.token;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Document(collection = "platform_token")
@ApiModel(value = "第三方平台代币信息",description = "第三方平台代币信息")
public class PlatformTokenEntity {
	
	@Id
	@ApiModelProperty(name = "id", value = "唯一标识", required = false, dataType = "String")
	private String id;
	
	@TableField("data")
	@ApiModelProperty(name = "data", value = "类型", required = false, dataType = "JSONObject")
	private JSONObject data;
	
	@TableField("included")
	@ApiModelProperty(name = "included", value = "组成内容", required = false, dataType = "JSONObject")
	private List<JSONObject> included;
	
//	@TableField("relationships")
//	@ApiModelProperty(name = "relationships", value = "", required = false, dataType = "String")
//	private JSONObject relationships;
//	
//	@TableField("included")
//	@ApiModelProperty(name = "included", value = "最低价格", required = false, dataType = "String")
//	private List<JSONObject> included;
//	
//	@TableField("atl_change_percentage")
//	@ApiModelProperty(name = "atl_change_percentage", value = "最低价格到当前价格的变化百分比", required = false, dataType = "String")
//	private String atl_change_percentage;
//	
//	@TableField("atl_date")
//	@ApiModelProperty(name = "atl_date", value = "最低价格的日期和时间", required = false, dataType = "String")
//	private String atl_date;
//	
//	@TableField("circulating_supply")
//	@ApiModelProperty(name = "circulating_supply", value = "当前在市场上流通的代币数量", required = false, dataType = "String")
//	private String circulating_supply;
//	
//	@TableField("current_price")
//	@ApiModelProperty(name = "current_price", value = "代币当前的市场价格", required = false, dataType = "String")
//	private String current_price;
//	
//	@TableField("fully_diluted_valuation")
//	@ApiModelProperty(name = "fully_diluted_valuation", value = "当前价格下的市场总值", required = false, dataType = "String")
//	private String fully_diluted_valuation;
//	
//	@TableField("high_24h")
//	@ApiModelProperty(name = "high_24h", value = "过去24小时内代币的最高价格", required = false, dataType = "String")
//	private String high_24h;
//	
//	@TableField("image")
//	@ApiModelProperty(name = "image", value = "代币的图标 URL", required = false, dataType = "String")
//	private String image;
//	
//	@TableField("last_updated")
//	@ApiModelProperty(name = "last_updated", value = "最后一次更新的时间", required = false, dataType = "String")
//	private String last_updated;
//	
//	@TableField("low_24h")
//	@ApiModelProperty(name = "low_24h", value = "过去24小时内代币的最低价格", required = false, dataType = "String")
//	private String low_24h;
//	
//	@TableField("market_cap")
//	@ApiModelProperty(name = "market_cap", value = "代币的市值", required = false, dataType = "String")
//	private String market_cap;
//	
//	@TableField("market_cap_change_24h")
//	@ApiModelProperty(name = "market_cap_change_24h", value = "过去24小时内市值的变化金额", required = false, dataType = "String")
//	private String market_cap_change_24h;
//	
//	@TableField("market_cap_change_percentage_24h")
//	@ApiModelProperty(name = "market_cap_change_percentage_24h", value = "过去24小时内市值变化的百分比", required = false, dataType = "String")
//	private String market_cap_change_percentage_24h;
//	
//	@TableField("market_cap_rank")
//	@ApiModelProperty(name = "market_cap_rank", value = "代币在所有市场中按市值排名的位置", required = false, dataType = "String")
//	private String market_cap_rank;
//	
//	@TableField("max_supply")
//	@ApiModelProperty(name = "max_supply", value = "代币的最大供应量", required = false, dataType = "String")
//	private String max_supply;
//	
//	@TableField("name")
//	@ApiModelProperty(name = "name", value = "代币的名称", required = false, dataType = "String")
//	private String name;
//	
//	@TableField("price_change_24h")
//	@ApiModelProperty(name = "price_change_24h", value = "从历史最高价格到当前价格的变化百分比", required = false, dataType = "String")
//	private String price_change_24h;
//	
//	@TableField("price_change_percentage_24h")
//	@ApiModelProperty(name = "price_change_percentage_24h", value = "从历史最高价格到当前价格的变化百分比", required = false, dataType = "String")
//	private String price_change_percentage_24h;
//	
//	@TableField("symbol")
//	@ApiModelProperty(name = "symbol", value = "从历史最高价格到当前价格的变化百分比", required = false, dataType = "String")
//	private String symbol;
//	
//	@TableField("total_supply")
//	@ApiModelProperty(name = "total_supply", value = "从历史最高价格到当前价格的变化百分比", required = false, dataType = "String")
//	private String total_supply;
//	
//	@TableField("total_volume")
//	@ApiModelProperty(name = "total_volume", value = "从历史最高价格到当前价格的变化百分比", required = false, dataType = "String")
//	private String total_volume;
}
