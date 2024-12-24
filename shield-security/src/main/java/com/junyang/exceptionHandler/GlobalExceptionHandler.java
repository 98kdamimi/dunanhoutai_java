 package com.junyang.exceptionHandler;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
 import org.springframework.web.bind.annotation.ExceptionHandler;
 import org.springframework.web.bind.annotation.ResponseBody;

import com.junyang.aop.SysLogAnnotation;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.constants.Constants;


 //全局异常捕获
 @ControllerAdvice
 public class GlobalExceptionHandler extends BaseApiService{

	 @ExceptionHandler(Exception.class)//捕获运行时异常
 	@ResponseBody
 	@SysLogAnnotation(module = "全局异常捕获",type = "异常捕获",remark = "异常捕获")
 	public ResponseBase exceptionHandler(Exception e,HttpServletRequest request) {
 		return setResultError(Constants.EXCEPTION_MSG);
 	}
	
 }
