package com.junyang.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.junyang.base.ResponseBase;
import com.junyang.entity.ipWhitelist.IpWhitelistEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/ipWhitelist")
@Api(value = "ip白名单",tags = "ip白名单")
public interface IpWhitelistService {
	
	@PostMapping("/getList")
	@ApiOperation(value = "获取列表",notes = "获取列表",response = ResponseBase.class)
	ResponseBase getList(IpWhitelistEntity entity);
	
	@GetMapping("/delete")
	@ApiOperation(value = "删除",notes = "删除",response = ResponseBase.class)
	ResponseBase delete(String id);
	
	@PostMapping("/add")
	@ApiOperation(value = "新增",notes = "新增",response = ResponseBase.class)
	ResponseBase add(IpWhitelistEntity entity);

}
