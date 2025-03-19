package com.junyang.service;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.junyang.base.ResponseBase;
import com.junyang.entity.appuser.AppUserEntity;
import com.junyang.entity.appuser.AppUserLogEntity;

import io.swagger.annotations.Api;

@RequestMapping("/appUser")
@Api(value = "app用户",tags = "app用户")
public interface AppUserService {
	
	@PostMapping("/findList")
	ResponseBase findList(AppUserEntity entity);
	
	@PostMapping("/findInfoList")
	ResponseBase findInfoList(AppUserLogEntity entity);
	
	
	@PostMapping("/findWalletList")
	ResponseBase findWalletList(AppUserLogEntity entity);

}
