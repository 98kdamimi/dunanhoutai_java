package com.junyang.poi.statistics;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "工单状态统计导出表格",description = "工单状态统计导出表格")
public class WrokStateStatisticsPoi {
	
	private Integer workDisposeState;
	
	@Excel(name = "工单状态",orderNum = "1")
	private String workDisposeStateName;
	
	@Excel(name = "状态数量",orderNum = "2")
	private String num;
	
	@Excel(name = "状态占比",orderNum = "3")
	private double ratio;

}
