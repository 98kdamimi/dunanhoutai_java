package com.junyang.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.junyang.base.ResponseBase;
import com.junyang.entity.version.ReleaseEntity;
import com.junyang.query.PublicQueryEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/version")
@Api(value = "版本管理",tags = "版本管理")
public interface ReleaseService {
	
	@PostMapping("/add")
	@ApiOperation(value = "发版接口",notes = "发版接口",response = ResponseBase.class)
	ResponseBase add(ReleaseEntity entity);
	
	@PostMapping("/softwareList")
	@ApiOperation(value = "软件版本列表",notes = "软件版本列表",response = ResponseBase.class)
	ResponseBase softwareList(PublicQueryEntity entity);
	
	@GetMapping("/onlineSoftware")
	@ApiOperation(value = "软件上线接口",notes = "软件上线接口",response = ResponseBase.class)
	ResponseBase onlineSoftware(String id,Integer forceUpdateLable);
	
	@PostMapping("/hardwareList")
	@ApiOperation(value = "硬件版本列表",notes = "硬件版本列表",response = ResponseBase.class)
	ResponseBase hardwareList(PublicQueryEntity entity);
	
	@GetMapping("/onlineHardware")
	@ApiOperation(value = "硬件上线接口",notes = "硬件上线接口",response = ResponseBase.class)
	ResponseBase onlineHardware(String id);
	
	@GetMapping("/msgWarn")
	@ApiOperation(value = "新版本提示",notes = "新版本提示",response = ResponseBase.class)
	ResponseBase msgWarn();
	
	@GetMapping("/deleteMsg")
	@ApiOperation(value = "删除消息",notes = "删除消息",response = ResponseBase.class)
	ResponseBase deleteMsg();

}
