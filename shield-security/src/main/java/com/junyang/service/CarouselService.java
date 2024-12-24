package com.junyang.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.junyang.base.ResponseBase;
import com.junyang.entity.carouse.CarouselEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/carouse")
@Api(value = "轮播图",tags = "轮播图")
public interface CarouselService {
	
	@PostMapping("/getList")
	@ApiOperation(value = "获取列表",notes = "获取列表",response = ResponseBase.class)
	ResponseBase getList(CarouselEntity entity);
	
	@GetMapping("/delete")
	@ApiOperation(value = "删除",notes = "删除",response = ResponseBase.class)
	ResponseBase delete(String id);
	
	@PostMapping("/add")
	@ApiOperation(value = "新增",notes = "新增",response = ResponseBase.class)
	ResponseBase add(String dataStr,MultipartFile file);
	
	@PostMapping("/update")
	@ApiOperation(value = "编辑",notes = "编辑",response = ResponseBase.class)
	ResponseBase update(String dataStr,MultipartFile file);
	

}
