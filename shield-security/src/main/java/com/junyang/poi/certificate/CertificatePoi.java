package com.junyang.poi.certificate;
import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "证书导入导出表格",description = "证书导入导出表格")
public class CertificatePoi {
	
	@Excel(name = "设备序列号",orderNum = "1")
	private String serialNumber;
	
	@Excel(name = "证书名称",orderNum = "2")
	private String fileName;
	
	@Excel(name = "证书内容",orderNum = "3")
	private String certificatePem;

}
