package com.junyang.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.junyang.base.ResponseBase;
import com.junyang.entity.link.LinkEmailEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/linkEmail")
@Api(value = "联系邮箱",tags = "联系邮箱")
public interface LinkEmailService {
	
	@PostMapping("/add")
	@ApiOperation(value = "联系邮箱新增",notes = "联系邮箱新增",response = ResponseBase.class)
	ResponseBase add(LinkEmailEntity entity);
	
	@PostMapping("/update")
	@ApiOperation(value = "联系邮箱编辑",notes = "联系邮箱编辑",response = ResponseBase.class)
	ResponseBase update(LinkEmailEntity entity);
	
	@GetMapping("/delete")
	@ApiOperation(value = "联系邮箱删除",notes = "联系邮箱删除",response = ResponseBase.class)
	ResponseBase delete(String id);
	
	@PostMapping("/findList")
	@ApiOperation(value = "联系邮箱列表查询",notes = "联系邮箱列表查询",response = ResponseBase.class)
	ResponseBase findList(LinkEmailEntity entity);

}
