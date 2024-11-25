package com.junyang.entity.coingecko;
import org.springframework.data.mongodb.core.mapping.Document;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Document(collection = "assetplat_forms")
@ApiModel(value = "coingecko平台资产平台",description = "coingecko平台资产平台")
public class AssetplatformsEntity {
	
	@TableField("id")
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "String")
	private String id;
	
	@TableField("chain_identifier")
	@ApiModelProperty(name = "chain_identifier", value = "资产平台编号", required = false, dataType = "String")
	private String chain_identifier;
	
	@TableField("name")
	@ApiModelProperty(name = "name", value = "资产平台名称", required = false, dataType = "String")
	private String name;
	
	@TableField("shortname")
	@ApiModelProperty(name = "shortname", value = "简称", required = false, dataType = "String")
	private String shortname;
	
	@TableField("native_coin_id")
	@ApiModelProperty(name = "native_coin_id", value = "身份id", required = false, dataType = "String")
	private String native_coin_id;
	
	@TableField("image")
	@ApiModelProperty(name = "image", value = "", required = false, dataType = "AssetplatFormsImageEntity")
	private AssetplatFormsImageEntity image;
	

}
