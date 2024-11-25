package com.junyang.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.junyang.base.ResponseBase;
import com.junyang.entity.dapp.DappEntity;
import com.junyang.query.PublicQueryEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/dapp")
@Api(value = "Dapp发现页配置管理",tags = "Dapp发现页配置管理")
public interface DappService {
	
	@GetMapping("/rpcList")
	@ApiOperation(value = "通过RPC接口获取",notes = "通过RPC接口获取",response = ResponseBase.class)
	ResponseBase rpcList();
	
	@PostMapping("/findList")
	@ApiOperation(value = "PC端分页列表",notes = "PC端分页列表",response = ResponseBase.class)
	ResponseBase findList(PublicQueryEntity entity);
	
	@GetMapping("/online")
	@ApiOperation(value = "上线",notes = "上线",response = ResponseBase.class)
	ResponseBase online(String id);
	
	@GetMapping("/Offline")
	@ApiOperation(value = "下线",notes = "下线",response = ResponseBase.class)
	ResponseBase Offline(String id);
	
	@PostMapping("/rpcUpdate")
	@ApiOperation(value = "远程更新",notes = "远程更新",response = ResponseBase.class)
	ResponseBase rpcUpdate(DappEntity entity);
	
	@PostMapping("/update")
	@ApiOperation(value = "更新",notes = "更新",response = ResponseBase.class)
	ResponseBase update(DappEntity entity);

}
