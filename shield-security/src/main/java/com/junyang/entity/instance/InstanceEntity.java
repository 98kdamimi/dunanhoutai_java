package com.junyang.entity.instance;
import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.junyang.query.PageQueryHelperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Document(collection = "instances")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "账户",description = "账户")
public class InstanceEntity extends PageQueryHelperEntity{

	@TableField("id")
	@ApiModelProperty(name = "id", value = "主键", required = false, dataType = "String")
	private String id;
	
	@TableField("instanceId")
	@ApiModelProperty(name = "instanceId", value = "主键", required = false, dataType = "String")
	private String instanceId;
	
	@TableField("createdAt")
	@ApiModelProperty(name = "createdAt", value = "创建时间", required = false, dataType = "String")
	private String createdAt;
	
	@TableField("updatedAt")
	@ApiModelProperty(name = "updatedAt", value = "修改时间", required = false, dataType = "String")
	private String updatedAt;
	
	@TableField("notificationConfig")
	@ApiModelProperty(name = "notificationConfig", value = "配置", required = false, dataType = "String")
	private NotificationConfigEntity notificationConfig;
	
	@TableField("accounts")
	@ApiModelProperty(name = "accounts", value = "账户", required = false, dataType = "String")
	private List<InstanceAccountEntity> accounts;
}
