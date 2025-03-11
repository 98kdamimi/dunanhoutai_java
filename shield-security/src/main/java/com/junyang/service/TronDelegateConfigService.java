package com.junyang.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.junyang.base.ResponseBase;
import com.junyang.entity.delegate.TronDelegateCofigEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/delegateConfig")
@Api(value = "联系方式",tags = "联系方式")
public interface TronDelegateConfigService {
	
	@PostMapping("/add")
	@ApiOperation(value = "新增配置",notes = "新增配置",response = ResponseBase.class)
	ResponseBase add(TronDelegateCofigEntity entity);
	
	@GetMapping("/find")
	@ApiOperation(value = "新增配置",notes = "新增配置",response = ResponseBase.class)
	ResponseBase find();
	
	@GetMapping("/delete")
	@ApiOperation(value = "删除配置",notes = "删除配置",response = ResponseBase.class)
	ResponseBase delete(String id);

}
