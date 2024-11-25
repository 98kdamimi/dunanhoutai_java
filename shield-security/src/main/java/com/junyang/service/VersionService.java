package com.junyang.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.junyang.base.ResponseBase;
import com.junyang.entity.release.VersionEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/version")
@Api(value = "应用及硬件版本管理",tags = "应用及硬件版本管理")
public interface VersionService {
	
	@PostMapping("/add")
	@ApiOperation(value = "发版接口",notes = "发版接口",response = ResponseBase.class)
	ResponseBase add(String versionStr,MultipartFile iosFile,MultipartFile androidFile,MultipartFile deviceFile);
	
	@PostMapping("/findList")
	@ApiOperation(value = "软件版本列表",notes = "软件版本列表",response = ResponseBase.class)
	ResponseBase findList(VersionEntity entity);
	
	@PostMapping("/onlineSoftware")
	@ApiOperation(value = "软件上线接口",notes = "软件上线接口",response = ResponseBase.class)
	ResponseBase onlineSoftware(Integer id);
	
	@PostMapping("/hardwareFindList")
	@ApiOperation(value = "硬件版本列表",notes = "硬件版本列表",response = ResponseBase.class)
	ResponseBase hardwareFindList(VersionEntity entity);
	
	@PostMapping("/onlineHardware")
	@ApiOperation(value = "硬件上线接口",notes = "硬件上线接口",response = ResponseBase.class)
	ResponseBase onlineHardware(Integer id);
	
	@GetMapping("/msgWarn")
	@ApiOperation(value = "新版本提示",notes = "新版本提示",response = ResponseBase.class)
	ResponseBase msgWarn();
	
	@PostMapping("/Offline")
	@ApiOperation(value = "下线接口",notes = "下线接口",response = ResponseBase.class)
	ResponseBase Offline(Integer id);
	

}
