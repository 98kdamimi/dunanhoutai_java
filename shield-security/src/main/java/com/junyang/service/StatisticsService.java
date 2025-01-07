package com.junyang.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.junyang.base.ResponseBase;
import com.junyang.query.PublicQueryEntity;

import io.swagger.annotations.ApiOperation;

@RequestMapping("/statistics")
public interface StatisticsService {
	
	@GetMapping("/equipment")
	@ApiOperation(value = "设备注册数量统计",notes = "注册数量统计",response = ResponseBase.class)
	ResponseBase equipment();
	
	@PostMapping("/equipmentChar")
	@ApiOperation(value = "设备注册统计图表",notes = "注册数量统计图表",response = ResponseBase.class)
	ResponseBase equipmentChar(PublicQueryEntity entity);
	
	@GetMapping("/account")
	@ApiOperation(value = "账号数量统计",notes = "账号数量统计",response = ResponseBase.class)
	ResponseBase account();
	
	@PostMapping("/accountChar")
	@ApiOperation(value = "账号统计图表",notes = "账号统计图表",response = ResponseBase.class)
	ResponseBase accountChar(PublicQueryEntity entity);

}
