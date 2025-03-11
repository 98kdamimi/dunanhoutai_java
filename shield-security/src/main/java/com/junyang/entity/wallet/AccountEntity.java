package com.junyang.entity.wallet;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Document(collection = "instances")
@ApiModel(value = "账户信息", description = "账户信息")
public class AccountEntity {

	@TableField("id")
	@ApiModelProperty(name = "id", value = "主键", required = false, dataType = "String")
	private String id;

	@TableField("instanceId")
	@ApiModelProperty(name = "instanceId", value = "实例ID", required = false, dataType = "String")
	private String instanceId;

	@TableField("createdAt")
	@ApiModelProperty(name = "createdAt", value = "创建时间", required = false, dataType = "String")
	private String createdAt;

	@TableField("updatedAt")
	@ApiModelProperty(name = "updatedAt", value = "修改时间", required = false, dataType = "String")
	private String updatedAt;

	@TableField("accounts")
	@ApiModelProperty(name = "accounts", value = "账户列表", required = false, dataType = "List")
	private List<AccountInfoEntity> accounts;
}

