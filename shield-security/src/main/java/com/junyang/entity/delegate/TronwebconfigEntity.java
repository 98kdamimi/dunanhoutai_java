package com.junyang.entity.delegate;

import org.springframework.data.mongodb.core.mapping.Document;

import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Document(collection = "tronwebconfigs")
@ApiModel(value = "tronWeb配置",description = "tronWeb配置")
public class TronwebconfigEntity {
	
	@TableField("id")
	@ApiModelProperty(name = "id", value = "主键", required = false, dataType = "String")
	private String id;
	
	@TableField("tronFullhost")
	@ApiModelProperty(name = "tronFullhost", value = "波场链请求地址", required = false, dataType = "String")
	private String tronFullhost;
	
	@TableField("tronPrivatekey")
	@ApiModelProperty(name = "tronPrivatekey", value = "委托钱包私钥", required = false, dataType = "String")
	private String tronPrivatekey;
	
	@TableField("tronContractaddress")
	@ApiModelProperty(name = "tronContractaddress", value = "委托钱包地址", required = false, dataType = "String")
	private String tronContractaddress;
	
	@TableField("tronApikey")
	@ApiModelProperty(name = "tronApikey", value = "请求api密钥", required = false, dataType = "String")
	private String tronApikey;
	
	@TableField("createdAt")
	@ApiModelProperty(name = "createdAt", value = "创建时间", required = false, dataType = "String")
	private String createdAt;
	
	@TableField("updatedAt")
	@ApiModelProperty(name = "updatedAt", value = "修改时间", required = false, dataType = "String")
	private String updatedAt;

}
