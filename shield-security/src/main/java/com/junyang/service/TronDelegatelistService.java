package com.junyang.service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.junyang.base.ResponseBase;
import com.junyang.entity.delegate.TronDelegatelistEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/delegatelist")
@Api(value = "委托记录",tags = "委托记录")
public interface TronDelegatelistService {
	
	@PostMapping("/findList")
	@ApiOperation(value = "查询委托记录",notes = "查询委托记录",response = ResponseBase.class)
	ResponseBase findList(TronDelegatelistEntity entity);

}
