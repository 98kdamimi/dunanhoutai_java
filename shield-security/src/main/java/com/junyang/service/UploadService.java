package com.junyang.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.junyang.base.ResponseBase;
import com.junyang.query.PublicQueryEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/upload")
@Api(value = "附件管理",tags = "附件管理")
public interface UploadService {
	
	@PostMapping("/fileUpload")
	@ApiOperation(value = "上传升级文件",notes = "上传升级文件",response = ResponseBase.class)
	ResponseBase fileUpload(MultipartFile file,Integer typeId,String dbId);
	
	@PostMapping("/findList")
	@ApiOperation(value = "列表",notes = "列表",response = ResponseBase.class)
	ResponseBase findList(PublicQueryEntity entity);
	
	@GetMapping("/findTypeList")
	@ApiOperation(value = "列表",notes = "列表",response = ResponseBase.class)
	ResponseBase findTypeList();
	
	
}
