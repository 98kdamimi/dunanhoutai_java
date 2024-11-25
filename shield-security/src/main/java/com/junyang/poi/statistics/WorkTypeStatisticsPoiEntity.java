package com.junyang.poi.statistics;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "工单大类统计导出表格",description = "工单大类统计导出表格")
public class WorkTypeStatisticsPoiEntity {
	
	@Excel(name = "工单类型",orderNum = "1")
	private String bigType;
	
	@Excel(name = "工单类型",orderNum = "2")
	private String eventType;
	
	@Excel(name = "工单数量",orderNum = "3")
	private Integer num;
}
