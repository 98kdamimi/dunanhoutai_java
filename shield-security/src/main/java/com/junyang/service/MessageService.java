package com.junyang.service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.junyang.base.ResponseBase;
import com.junyang.entity.message.MessageEntity;
import com.junyang.query.PublicQueryEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/message")
@Api(value = "消息推送管理",tags = "消息推送管理")
public interface MessageService {

	@PostMapping("/add")
	@ApiOperation(value = "编辑消息",notes = "编辑消息",response = ResponseBase.class)
	ResponseBase add(MessageEntity entity);
	
	@PostMapping("/delete")
	@ApiOperation(value = "删除消息",notes = "删除消息",response = ResponseBase.class)
	ResponseBase delete(String id);
	
	@PostMapping("/findList")
	@ApiOperation(value = "消息列表",notes = "消息列表",response = ResponseBase.class)
	ResponseBase findList(PublicQueryEntity entity);
	
}
