package com.junyang.entity.network;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "链交易手续费的详细信息",description = "链交易手续费的详细信息")
public class NetWorkFeeMetaEntity {
	
	@TableField("feemeta_code")
	@ApiModelProperty(name = "code", value = "手续费使用的币种代码", required = false, dataType = "String")
	private String code;
	
	@TableField("feemeta_decimals")
	@ApiModelProperty(name = "decimals", value = "手续费的精度", required = false, dataType = "String")
	private String decimals;
	
	@TableField("feemeta_symbol")
	@ApiModelProperty(name = "symbol", value = "手续费的单位符号", required = false, dataType = "String")
	private String symbol;
	

}
