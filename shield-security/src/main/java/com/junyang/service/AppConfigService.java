package com.junyang.service;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.junyang.base.ResponseBase;
import com.junyang.entity.appconfig.AppconfigEntity;
import com.junyang.query.PublicQueryEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/appConfig")
@Api(value = "app配置",tags = "app配置")
public interface AppConfigService {
	
	@PostMapping("/add")
	@ApiOperation(value = "新增配置",notes = "新增配置",response = ResponseBase.class)
	ResponseBase add(AppconfigEntity entity);
	
	@PostMapping("/update")
	@ApiOperation(value = "编辑配置",notes = "编辑配置",response = ResponseBase.class)
	ResponseBase update(AppconfigEntity entity);
	
	@PostMapping("/findList")
	@ApiOperation(value = "列表查询",notes = "列表查询",response = ResponseBase.class)
	ResponseBase findList(PublicQueryEntity entity);

//	@PostMapping("/getRpc")
//	@ApiOperation(value = "远程列表",notes = "远程列表",response = ResponseBase.class)
//	ResponseBase getRpc(AppconfigEntity entity);


}
