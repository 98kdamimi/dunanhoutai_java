package com.junyang.poi.statistics;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "部门村庄处理时长统计导出表格",description = "部门村庄处理时长统计导出表格")
public class DeptResultTimeStatisticsPoi {
	
	private String dutyDept;
	
	@Excel(name = "村庄/部门",orderNum = "1")
	private String dutyDeptName;
	
	@Excel(name = "责任人",orderNum = "2")
	private String dutyUserName;
	
	@Excel(name = "工单数量",orderNum = "3")
	private Integer numAll;
	
	@Excel(name = "总时长",orderNum = "4")
	private String timeLong;
	
	@Excel(name = "平均时长",orderNum = "5")
	private String meanTime;


}
