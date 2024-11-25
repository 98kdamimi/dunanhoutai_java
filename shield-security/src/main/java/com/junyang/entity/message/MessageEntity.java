package com.junyang.entity.message;
import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Document(collection = "message")
@ApiModel(value = "消息",description = "消息")
public class MessageEntity {
	
	@Id
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "String")
	private String id;
	
	@TableField("messageTitleZh")
	@ApiModelProperty(name = "messageTitleZh", value = "中文消息标题", required = false, dataType = "String")
	private String messageTitleZh;
	
	@TableField("messageTitleEn")
	@ApiModelProperty(name = "messageTitleEn", value = "英文消息标题", required = false, dataType = "String")
	private String messageTitleEn;
	
	@TableField("messageContentZh")
	@ApiModelProperty(name = "messageContentZh", value = "中文消息内容", required = false, dataType = "String")
	private String messageContentZh;
	
	@TableField("messageContentEn")
	@ApiModelProperty(name = "messageContentEn", value = "英文消息内容", required = false, dataType = "String")
	private String messageContentEn;
	
	@TableField("set_time")
	@ApiModelProperty(name = "setTime",value = "创建时间",required = false,dataType = "Date")
    private Date setTime;
	
	@TableField("gmt_modified")
	@ApiModelProperty(name = "gmtModified",value = "更新时间",required = false,dataType = "Date")
	private Date gmtModified;

}
