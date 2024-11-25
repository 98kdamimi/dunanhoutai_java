package com.junyang.entity.network;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "区块链节点的 RPC（远程过程调用）接口地址",description = "区块链节点的 RPC（远程过程调用）接口地址")
public class NetWorkRpcUrlsEntity {
	
	@TableField("rpc_url")
	@ApiModelProperty(name = "url", value = "地址", required = false, dataType = "String")
	private String url; 
	
	@TableField("rpc_ur")
	@ApiModelProperty(name = "ur",value = "地址",required = true,dataType = "String")
    private String ur;
	
	@TableField("rpc_index")
	@ApiModelProperty(name = "index",value = "标记",required = true,dataType = "Integer")
    private Integer index;
	
	@TableField("rpc_indexer")
	@ApiModelProperty(name = "indexer",value = "标记",required = true,dataType = "String")
    private String indexer;
	
}
