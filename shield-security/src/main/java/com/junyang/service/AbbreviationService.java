package com.junyang.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.junyang.base.ResponseBase;
import com.junyang.entity.AbbreviationEntity.AbbreviationEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/abbreviation")
@Api(value = "联系方式",tags = "联系方式")
public interface AbbreviationService {
	
	@PostMapping("/getList")
	@ApiOperation(value = "获取列表",notes = "获取列表",response = ResponseBase.class)
	ResponseBase getList(AbbreviationEntity entity);
	
	@GetMapping("/delete")
	@ApiOperation(value = "删除",notes = "删除",response = ResponseBase.class)
	ResponseBase delete(String id);
	
	@PostMapping("/add")
	@ApiOperation(value = "新增",notes = "新增",response = ResponseBase.class)
	ResponseBase add(AbbreviationEntity entity);
	
	@PostMapping("/update")
	@ApiOperation(value = "编辑",notes = "编辑",response = ResponseBase.class)
	ResponseBase update(AbbreviationEntity entity);

}
