package com.junyang.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.junyang.base.ResponseBase;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/dic")
@Api(value = "字典",tags = "字典")
public interface DicService {

	@GetMapping("/getLanguage")
	@ApiOperation(value = "查询语言列表",notes = "查询语言列表",response = ResponseBase.class)
	ResponseBase getLanguage();
	
}
