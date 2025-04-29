package com.junyang.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.junyang.base.ResponseBase;
import com.junyang.entity.delegate.TronStakingEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/tronStaking")
@Api(value = "质押账户",tags = "质押账户")
public interface TronStakingService {
	
	@PostMapping("/add")
	@ApiOperation(value = "新增质押账户",notes = "新增质押账户",response = ResponseBase.class)
	ResponseBase add(TronStakingEntity entity);
	
	@PostMapping("/find")
	@ApiOperation(value = "查询质押账户",notes = "查询质押账户",response = ResponseBase.class)
	ResponseBase find(TronStakingEntity entity);
	
	@GetMapping("/delete")
	@ApiOperation(value = "删除质押账户",notes = "删除配置",response = ResponseBase.class)
	ResponseBase delete(String id);

}
