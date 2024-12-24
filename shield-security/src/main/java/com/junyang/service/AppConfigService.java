package com.junyang.service;

import org.springframework.web.bind.annotation.RequestMapping;

import com.junyang.base.ResponseBase;

import io.swagger.annotations.Api;

@RequestMapping("/appConfig")
@Api(value = "app配置",tags = "app配置")
public interface AppConfigService {
	
	@RequestMapping("/add")
	ResponseBase add();

}
