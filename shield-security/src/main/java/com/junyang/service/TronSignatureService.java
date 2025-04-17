package com.junyang.service;

import com.junyang.entity.tronsignature.MultiSignaturesEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.junyang.base.HttpResponse;
import com.junyang.base.ResponseBase;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/tronSignature")
@Api(value = "多签",tags = "多签")
public interface TronSignatureService {
	
	@GetMapping("/signJudge")
	@ApiOperation(value = "获取tron账户详情判断是否是多签账户",notes = "获取tron账户详情判断是否是多签账户",response = ResponseBase.class)
	HttpResponse signJudge(String address);
	
	@GetMapping("/multiSign")
	@ApiOperation(value = "获取tron账户多签交易",notes = "获取tron账户多签交易",response = ResponseBase.class)
	HttpResponse multiSign(String address);
	
	@GetMapping("/getAccount")
	@ApiOperation(value = "获取tron账户详情",notes = "获取tron账户详情",response = ResponseBase.class)
	HttpResponse getAccount(String address);
	
	@GetMapping("/getAddressToken")
	@ApiOperation(value = "获取tron账户代币",notes = "获取tron账户代币",response = ResponseBase.class)
	HttpResponse getAddressToken(String address);

	@PostMapping("/createMultiSign")
	@ApiOperation(value = "新增修改多签交易记录",notes ="新增修改多签交易记录",response = ResponseBase.class)
	ResponseBase createMultiSign(MultiSignaturesEntity entity);

	@GetMapping("/getMultiSign")
	@ApiOperation(value = "查询多签交易记录",notes ="查询多签交易记录",response = ResponseBase.class)
	ResponseBase getMultiSign(
			@RequestParam String address,
			@RequestParam(defaultValue = "1") int pageNumber, // 默认值为 1
			@RequestParam(defaultValue = "10") int pageSize  // 默认值为 10
	);
}
