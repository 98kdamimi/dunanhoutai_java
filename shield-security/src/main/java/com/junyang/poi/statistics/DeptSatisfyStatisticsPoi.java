package com.junyang.poi.statistics;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "部门村庄满意度统计导出表格",description = "部门村庄满意度统计导出表格")
public class DeptSatisfyStatisticsPoi {
	
	@Excel(name = "责任人",orderNum = "1")
	private String dutyManName;
	
	@Excel(name = "责任部门",orderNum = "2")
	private String dutyDeptName;
	
	@Excel(name = "工单总数",orderNum = "3")
	private Integer numAll;
	
	@Excel(name = "满意数量",orderNum = "4")
	private Integer satisfyNum;
	
	@Excel(name = "不满意数量",orderNum = "5")
	private Integer satisfyNoNum;
	
	@Excel(name = "满意比例",orderNum = "6")
	private String satisfyRatio;
	
	@Excel(name = "不满意比例",orderNum = "7")
	private String satisfyNoRatio;
	
	private Integer dutyDept;
	
	private Integer dutyMan;

}
