package com.junyang.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.junyang.base.ResponseBase;
import com.junyang.entity.network.NetWorkEntity;
import com.junyang.entity.token.PlatformTokenEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/coingecko")
@Api(value = "coingecko平台调用相关",tags = "coingecko平台调用相关")
public interface CoingeckoService {
	
	@GetMapping("/assetPlatforms")
	@ApiOperation(value = "资产平台列表",notes = "资产平台列表",response = ResponseBase.class)
	ResponseBase assetPlatforms();
	
	@GetMapping("/network")
	@ApiOperation(value = "网络列表",notes = "网络列表",response = ResponseBase.class)
	ResponseBase network();
	
	@PostMapping("/addToken")
	@ApiOperation(value = "网络列表",notes = "网络列表",response = ResponseBase.class)
	ResponseBase addToken(PlatformTokenEntity entity);
	
	@PostMapping("/findCoingeckoToken")
	@ApiOperation(value = "Coingecko平台代币列表",notes = "Coingecko平台代币列表",response = ResponseBase.class)
	ResponseBase findCoingeckoToken(NetWorkEntity entity);
	


}
