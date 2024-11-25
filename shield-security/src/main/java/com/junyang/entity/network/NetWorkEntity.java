package com.junyang.entity.network;
import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Document(collection = "net_work")
@ApiModel(value = "链",description = "链")
public class NetWorkEntity {
	
	@TableField("id")
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "String")
	private String id;
	
	@TableField("workId")
	@ApiModelProperty(name = "workId", value = "唯一标识符", required = false, dataType = "String")
	private String workId;
	
	@TableField("__v")
	@ApiModelProperty(name = "__v", value = "唯一标识符", required = false, dataType = "int")
	private int __v;
	
	@TableField("balance2FeeDecimals")
	@ApiModelProperty(name = "balance2FeeDecimals", value = "手续费的精度", required = false, dataType = "Integer")
	private Integer balance2FeeDecimals;
	
	@TableField("chainId")
	@ApiModelProperty(name = "chainId", value = "区块链的唯一标识符", required = false, dataType = "String")
	private String chainId;
	
	@TableField("code")
	@ApiModelProperty(name = "code", value = "区块链的代码", required = false, dataType = "String")
	private String code;
	
	@TableField("decimals")
	@ApiModelProperty(name = "decimals", value = "该区块链代币的精度", required = false, dataType = "Integer")
	private Integer decimals;
	
	@TableField("defaultEnabled")
	@ApiModelProperty(name = "defaultEnabled", value = "是否在默认情况下启用或显示", required = false, dataType = "boolean")
	private boolean defaultEnabled;
	
	@TableField("impl")
	@ApiModelProperty(name = "impl", value = "实现的类型", required = false, dataType = "String")
	private String impl;
	
	@TableField("testnet")
	@ApiModelProperty(name = "testnet", value = "区块链是否为测试网络", required = false, dataType = "boolean")
	private boolean testnet;
	
	@TableField("logoURI")
	@ApiModelProperty(name = "logoURI", value = "logo 图像", required = false, dataType = "String")
	private String logoURI;
	
	@TableField("name")
	@ApiModelProperty(name = "name", value = "名称", required = false, dataType = "String")
	private String name;
	
	@TableField("shortcode")
	@ApiModelProperty(name = "shortcode", value = "区块链标识符", required = false, dataType = "String")
	private String shortcode;
	
	@TableField("shortname")
	@ApiModelProperty(name = "shortname", value = "区块链的简短名", required = false, dataType = "String")
	private String shortname;
	
	@TableField("status")
	@ApiModelProperty(name = "status", value = "区块链的当前状态", required = false, dataType = "String")
	private String status;
	
	@TableField("symbol")
	@ApiModelProperty(name = "symbol", value = "区块链的代币符号", required = false, dataType = "String")
	private String symbol;
	
	@TableField("updatedAt")
	@ApiModelProperty(name = "updatedAt", value = "更新时间", required = false, dataType = "String")
	private String updatedAt;
	
	@TableField("feeMeta")
	@ApiModelProperty(name = "feeMeta",value = "链交易手续费的详细信息",required = false,dataType = "NetWorkFeeMetaEntity")
	private NetWorkFeeMetaEntity feeMeta;
	
	@TableField("netWorkRpcUrls")
	@ApiModelProperty(name = "netWorkRpcUrls",value = "区块链节点的 RPC（远程过程调用）接口地址",required = false,dataType = "NetWorkRpcUrlsEntity")
	private List<NetWorkRpcUrlsEntity> rpcURLs;
	
	@TableField("extensions")
	@ApiModelProperty(name = "extensions",value = "扩展信息",required = false,dataType = "extensions")
	private NetWorkExtensionsEntity extensions;
	
	
}
