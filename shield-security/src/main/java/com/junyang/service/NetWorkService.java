package com.junyang.service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.junyang.base.ResponseBase;
import com.junyang.query.PublicQueryEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/network")
@Api(value = "主链管理",tags = "主链管理")
public interface NetWorkService{
	
	@GetMapping("/obtainList")
	@ApiOperation(value = "主链列表RPC获取",notes = "主链列表RPC获取",response = ResponseBase.class)
	ResponseBase obtainList();
	
	@PostMapping("/findList")
	@ApiOperation(value = "pc端主链分页列表",notes = "pc端主链分页列表",response = ResponseBase.class)
	ResponseBase findList(PublicQueryEntity entity);
	
	@GetMapping("/findAll")
	@ApiOperation(value = "主链列表",notes = "主链列表",response = ResponseBase.class)
	ResponseBase findAll();
	
	@GetMapping("/findNetWorkIdList")
	@ApiOperation(value = "主链列表",notes = "主链列表",response = ResponseBase.class)
	ResponseBase findNetWorkIdList();
	
}
