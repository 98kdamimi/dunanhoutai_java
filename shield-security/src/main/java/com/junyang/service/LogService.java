package com.junyang.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.junyang.base.ResponseBase;
import com.junyang.query.PublicQueryEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/log")
@Api(value = "日志管理",tags = "日志管理")
public interface LogService {
	
	@PostMapping("/findList")
	@ApiOperation(value = "查询列表",notes = "查询列表",response = ResponseBase.class)
	ResponseBase findList(PublicQueryEntity entity);

}
