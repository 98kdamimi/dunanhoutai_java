package com.junyang.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.junyang.base.ResponseBase;
import com.junyang.query.PublicQueryEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/token")
@Api(value = "代币管理",tags = "代币管理")
public interface TokenService {
	
//	@GetMapping("/rpcList")
//	@ApiOperation(value = "通过RPC接口获取",notes = "通过RPC接口获取",response = ResponseBase.class)
//	ResponseBase rpcList();
	
	@GetMapping("/ThirdPartylist")
	@ApiOperation(value = "获取第三方平台代币",notes = "获取第三方平台代币",response = ResponseBase.class)
	ResponseBase ThirdPartylist();
	
	@PostMapping("/findList")
	@ApiOperation(value = "pc端代币分页列表",notes = "pc端代币分页列表",response = ResponseBase.class)
	ResponseBase findList(PublicQueryEntity entity);
	
	@PostMapping("/findListThirdParty")
	@ApiOperation(value = "pc端第三方代币分页列表",notes = "pc端第三方代币分页列表",response = ResponseBase.class)
	ResponseBase findListThirdParty(PublicQueryEntity entity);
	
	@PostMapping("/add")
	@ApiOperation(value = "新增代币",notes = "新增代币",response = ResponseBase.class)
	ResponseBase add(String dataStr,MultipartFile file);
	
	@PostMapping("/update")
	@ApiOperation(value = "更新代币",notes = "更新代币",response = ResponseBase.class)
	ResponseBase update(String dataStr,MultipartFile file);
	
	@GetMapping("/findSource")
	@ApiOperation(value = "代币来源",notes = "代币来源",response = ResponseBase.class)
	ResponseBase findSource();

}
