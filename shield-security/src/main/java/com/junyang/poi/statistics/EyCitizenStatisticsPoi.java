package com.junyang.poi.statistics;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "重点关注市民统计导出表格",description = "重点关注市民统计导出表格")
public class EyCitizenStatisticsPoi {
	
	@Excel(name = "市民姓名",orderNum = "1")
	private String citizenName;
	
	@Excel(name = "市民手机号",orderNum = "2")
	private String citizenTel;
	
	@Excel(name = "问题数量",orderNum = "3")
	private String numAll;

}
