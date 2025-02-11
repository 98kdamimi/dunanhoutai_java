package com.junyang.service;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.junyang.base.ResponseBase;
import com.junyang.entity.transaction.TransactionEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/transaction")
@Api(value = "流水记录",tags = "流水记录")
public interface TransactionService {
	
	@PostMapping("/findList")
	@ApiOperation(value = "记录列表",notes = "记录列表",response = ResponseBase.class)
	ResponseBase findList(TransactionEntity entity);

}
