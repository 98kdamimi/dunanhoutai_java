package com.junyang.service;

import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.Api;

@RequestMapping("/transaction")
@Api(value = "流水记录",tags = "流水记录")
public interface TransactionService {

}
