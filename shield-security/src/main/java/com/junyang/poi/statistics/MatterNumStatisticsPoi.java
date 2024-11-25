package com.junyang.poi.statistics;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "问题反映数量统计表格",description = "问题反映数量统计表格")
public class MatterNumStatisticsPoi {
	
	@Excel(name = "日期",orderNum = "1")
	private String day;
	
	@Excel(name = "数量",orderNum = "2")
	private Integer num;

}
