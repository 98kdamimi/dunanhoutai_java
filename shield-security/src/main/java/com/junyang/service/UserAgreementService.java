package com.junyang.service;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.junyang.base.ResponseBase;
import com.junyang.entity.system.UserAgreementEntity;
import com.junyang.query.PublicQueryEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/agreement")
@Api(value = "用户协议、隐私协议",tags = "用户协议、隐私协议")
public interface UserAgreementService {
	
	@PostMapping("/add")
	@ApiOperation(value = "新增协议",notes = "新增协议",response = ResponseBase.class)
	ResponseBase add(UserAgreementEntity entity);
	
	@PostMapping("/update")
	@ApiOperation(value = "编辑协议",notes = "编辑协议",response = ResponseBase.class)
	ResponseBase update(UserAgreementEntity entity);
	
	@PostMapping("/findType")
	@ApiOperation(value = "查询协议",notes = "查询协议",response = ResponseBase.class)
	ResponseBase findType(PublicQueryEntity entity);
	
	@PostMapping("/delete")
	@ApiOperation(value = "查询协议",notes = "查询协议",response = ResponseBase.class)
	ResponseBase delete(String id);
	
	

}
