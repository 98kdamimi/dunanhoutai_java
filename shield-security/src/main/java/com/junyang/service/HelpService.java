package com.junyang.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.junyang.base.ResponseBase;
import com.junyang.entity.help.HelpEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/help")
@Api(value = "帮助",tags = "帮助")
public interface HelpService {
	
	@PostMapping("/getList")
	@ApiOperation(value = "获取列表",notes = "获取列表",response = ResponseBase.class)
	ResponseBase getList(HelpEntity entity);
	
	@GetMapping("/delete")
	@ApiOperation(value = "删除",notes = "删除",response = ResponseBase.class)
	ResponseBase delete(String id);
	
	@PostMapping("/add")
	@ApiOperation(value = "新增",notes = "新增",response = ResponseBase.class)
	ResponseBase add(HelpEntity entity);
	
	@PostMapping("/update")
	@ApiOperation(value = "编辑",notes = "编辑",response = ResponseBase.class)
	ResponseBase update(HelpEntity entity);

}
