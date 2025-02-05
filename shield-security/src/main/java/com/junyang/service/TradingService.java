package com.junyang.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.junyang.base.ResponseBase;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/tradingLableState")
@Api(value = "交易标签状态",tags = "交易标签状态")
public interface TradingService {
	
	@GetMapping("/getList")
	@ApiOperation(value = "获取列表",notes = "获取列表",response = ResponseBase.class)
	ResponseBase getList();
	
	@GetMapping("/update")
	@ApiOperation(value = "修改",notes = "修改",response = ResponseBase.class)
	ResponseBase update(String id,boolean states);
	
}
