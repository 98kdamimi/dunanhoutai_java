package com.junyang.entity.uploadefiel;
import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Document(collection = "upload_file")
@ApiModel(value = "上传文件",description = "上传文件")
public class UploadFileEntity {
	
	@Id
	@ApiModelProperty(name = "id", value = "唯一标识符", required = false, dataType = "ObjectId")
	private String id;
	
	@TableField("typeId")
	@ApiModelProperty(name = "typeId", value = "类型id", required = false, dataType = "Integer")
	private Integer typeId;
	
	@TableField("typeName")
	@ApiModelProperty(name = "typeName", value = "类型名称", required = false, dataType = "String")
	private String typeName;
	
	@TableField("fileCatalogue")
	@ApiModelProperty(name = "fileCatalogue", value = "资源目录", required = false, dataType = "String")
	private String fileCatalogue;
	
	@TableField("databseName")
	@ApiModelProperty(name = "databseName", value = "对应配数据库名称", required = false, dataType = "String")
	private String databseName;
	
	@TableField("databseId")
	@ApiModelProperty(name = "databseId", value = "对应配数据库数据id", required = false, dataType = "String")
	private String databseId;
	
	@TableField("filePath")
	@ApiModelProperty(name = "filePath", value = "文件地址", required = false, dataType = "String")
	private String filePath;
	
	@TableField("fileSize")
	@ApiModelProperty(name = "fileSize", value = "文件大小", required = false, dataType = "String")
	private String fileSize;
	
	@TableField("fileType")
	@ApiModelProperty(name = "fileType", value = "文件后缀", required = false, dataType = "String")
	private String fileType;
	
	@TableField("imageLable")
	@ApiModelProperty(name = "imageLable", value = "是否是图片类型文件", required = false, dataType = "boolean")
	private boolean imageLable;
	
	@TableField("set_time")
	@ApiModelProperty(name = "setTime",value = "创建时间",required = false,dataType = "Date")
    private Date setTime;
	
	@TableField("gmt_modified")
	@ApiModelProperty(name = "gmtModified",value = "更新时间",required = false,dataType = "Date")
	private Date gmtModified;

}
