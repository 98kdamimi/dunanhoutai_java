package com.junyang.entity.response;


import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "rpc接口调用响应对象",description = "rpc接口调用响应对象")
public class RpcResponseEntity {
	
	private Object data;
	
	private Boolean success;
	
	private Object message;
	
	private Object error;
	
	private Integer statusCode;

}
