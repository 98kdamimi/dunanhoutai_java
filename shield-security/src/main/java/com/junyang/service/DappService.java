package com.junyang.service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.junyang.base.ResponseBase;
import com.junyang.query.PublicQueryEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/dapp")
@Api(value = "Dapp发现页配置管理",tags = "Dapp发现页配置管理")
public interface DappService {
	
	@GetMapping("/typeRpcList")
	@ApiOperation(value = "通过RPC接口获取分类",notes = "通过RPC接口获取分类",response = ResponseBase.class)
	ResponseBase typeRpcList();
	
	@GetMapping("/rpcList")
	@ApiOperation(value = "通过RPC接口获取",notes = "通过RPC接口获取",response = ResponseBase.class)
	ResponseBase rpcList();
	
	@GetMapping("/findTypeList")
	@ApiOperation(value = "分类列表",notes = "分类列表",response = ResponseBase.class)
	ResponseBase findTypeList();
	
	@PostMapping("/findList")
	@ApiOperation(value = "列表查询",notes = "列表查询",response = ResponseBase.class)
	ResponseBase findList(PublicQueryEntity entity);
	
	@GetMapping("/Offline")
	@ApiOperation(value = "下线",notes = "下线",response = ResponseBase.class)
	ResponseBase Offline(String id);
	
	@GetMapping("/online")
	@ApiOperation(value = "上线",notes = "上线",response = ResponseBase.class)
	ResponseBase online(String id);
	
	@PostMapping("/update")
	@ApiOperation(value = "编辑",notes = "编辑",response = ResponseBase.class)
	ResponseBase update(String dataStr,MultipartFile file);
	
	@PostMapping("/add")
	@ApiOperation(value = "新增",notes = "新增",response = ResponseBase.class)
	ResponseBase add(String dataStr,MultipartFile file);

}
