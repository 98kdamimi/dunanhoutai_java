package com.junyang.service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.junyang.base.ResponseBase;
import com.junyang.entity.wallet.WalletBalanceInfoEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/walletBalanceInfo")
@Api(value = "联系方式",tags = "联系方式")
public interface WalletBalanceInfoService {
	
	@PostMapping("/getList")
	@ApiOperation(value = "获取列表",notes = "获取列表",response = ResponseBase.class)
	ResponseBase getList(WalletBalanceInfoEntity entity);
	
	@PostMapping("/getNumAll")
	@ApiOperation(value = "账户总额",notes = "账户总额",response = ResponseBase.class)
	ResponseBase getNumAll(WalletBalanceInfoEntity entity);
	
	@PostMapping("/getTotalUsdValueByName")
	@ApiOperation(value = "代币分组",notes = "代币分组",response = ResponseBase.class)
	ResponseBase getTotalUsdValueByName(WalletBalanceInfoEntity entity);

}
