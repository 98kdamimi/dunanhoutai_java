package com.junyang.entity.device;
import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Document(collection = "device_certificate")
@ApiModel(value = "设备证书",description = "设备证书")
public class DeviceCertificateEntity {
	
	@Id
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "String")
    private String id;   
	
	@TableField("serialNumber")
	@ApiModelProperty(name = "serialNumber", value = "设备序列号", required = false, dataType = "String")
    private String serialNumber;
	
	@TableField("certificatePem")
	@ApiModelProperty(name = "certificatePem", value = "设备证书 PEM 格式", required = false, dataType = "String")
    private String certificatePem;
    
	@TableField("isValid")
	@ApiModelProperty(name = "isValid", value = "证书是否有效", required = false, dataType = "String")
    private boolean isValid;
	
	@TableField("fileSize")
	@ApiModelProperty(name = "fileSize", value = "证书大小", required = false, dataType = "String")
	private String fileSize;
	
	@TableField("fileName")
	@ApiModelProperty(name = "fileName", value = "文件名称", required = false, dataType = "String")
	private String fileName;
	
	@TableField("set_time")
	@ApiModelProperty(name = "setTime",value = "创建时间",required = false,dataType = "Date")
    private Date setTime;
	
	@TableField("gmt_modified")
	@ApiModelProperty(name = "gmtModified",value = "更新时间",required = false,dataType = "Date")
	private Date gmtModified;

}
