package com.junyang.poi.statistics;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "处理结果统计导出表格",description = "处理结果统计导出表格")
public class ResultStatisticsPoi {
	
	@Excel(name = "工单总数",orderNum = "1")
	private Integer numAll;
	
	@Excel(name = "满意数量",orderNum = "2")
	private Integer satisfyNum;
	
	@Excel(name = "不满意数量",orderNum = "3")
	private Integer satisfyNoNum;
	
	@Excel(name = "处理中数量",orderNum = "4")
	private Integer disposeNum;
	
	@Excel(name = "满意比例",orderNum = "5")
	private String satisfyRatio;
	
	@Excel(name = "不满意比例",orderNum = "6")
	private String satisfyNoRatio;
	
	@Excel(name = "处理中比例",orderNum = "7")
	private String disposeRatio;
	

}
