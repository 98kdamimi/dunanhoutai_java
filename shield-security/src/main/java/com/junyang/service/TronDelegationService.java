package com.junyang.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.junyang.base.ResponseBase;
import com.junyang.entity.delegate.TronDelegationEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/tronDelegation")
@Api(value = "委托账户信息",tags = "委托账户信息")
public interface TronDelegationService{
	
	@PostMapping("/add")
	@ApiOperation(value = "新增委托账户",notes = "新增委托账户",response = ResponseBase.class)
	ResponseBase add(TronDelegationEntity entity);
	
	@PostMapping("/find")
	@ApiOperation(value = "查询委托账户",notes = "查询委托账户",response = ResponseBase.class)
	ResponseBase find(TronDelegationEntity entity);
	
	@GetMapping("/delete")
	@ApiOperation(value = "删除委托账户",notes = "删除委托账户",response = ResponseBase.class)
	ResponseBase delete(String id);

}
