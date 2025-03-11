package com.junyang.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.junyang.base.ResponseBase;
import com.junyang.entity.wallet.WalletBalanceEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/walletBalance")
@Api(value = "账号余额",tags = "账号余额")
public interface WalletBalanceService {
	

	@PostMapping("/getList")
	@ApiOperation(value = "获取列表",notes = "获取列表",response = ResponseBase.class)
	ResponseBase getList(WalletBalanceEntity entity);
	
	@GetMapping("/getNumAll")
	@ApiOperation(value = "获取账户余额总额",notes = "获取账户余额总额",response = ResponseBase.class)
	ResponseBase getNumAll();
	

}
