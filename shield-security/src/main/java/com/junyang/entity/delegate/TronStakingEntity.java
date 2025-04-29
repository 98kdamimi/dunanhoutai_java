package com.junyang.entity.delegate;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.baomidou.mybatisplus.annotation.TableField;
import com.junyang.query.PageQueryHelperEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "tronstakings")
@ApiModel(value = "质押账户配置",description = "质押账户配置")
public class TronStakingEntity extends PageQueryHelperEntity{

	@TableField("id")
	@ApiModelProperty(name = "id", value = "主键", required = false, dataType = "String")
	private String id;
	
	@TableField("address")
	@ApiModelProperty(name = "address", value = "质押账户账户地址", required = false, dataType = "String")
	private String address;
	
	@TableField("delegation")
	@ApiModelProperty(name = "delegation", value = "委托账户账户信息", required = false, dataType = "String")
	private List<TronDelegationEntity> delegation;
	
	@TableField("createdAt")
	@ApiModelProperty(name = "createdAt", value = "创建时间", required = false, dataType = "String")
	private String createdAt;
	
	@TableField("updatedAt")
	@ApiModelProperty(name = "updatedAt", value = "修改时间", required = false, dataType = "String")
	private String updatedAt;
	
	

}
