package com.junyang.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpResponse {

	private Integer code;
	
	private String msg;
	
	private Object data;
	
	
	public HttpResponse() {
		super();
	}

	public HttpResponse(Integer code, String msg, Object data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
	

}
