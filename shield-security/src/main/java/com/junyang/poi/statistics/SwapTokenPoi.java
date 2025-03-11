package com.junyang.poi.statistics;
import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "token导入导出表格",description = "token导入导出表格")
public class SwapTokenPoi {
	
	@Excel(name = "onramperId",orderNum = "1")
	private String onramperId;
	
	@Excel(name = "code",orderNum = "2")
	private String code;
	
	@Excel(name = "name",orderNum = "3")
	private String name;
	
	@Excel(name = "network",orderNum = "4")
	private String network;
	
	@Excel(name = "supportBuy",orderNum = "5")
	private String supportBuy;
	
	@Excel(name = "supportSell",orderNum = "6")
	private String supportSell;



}
