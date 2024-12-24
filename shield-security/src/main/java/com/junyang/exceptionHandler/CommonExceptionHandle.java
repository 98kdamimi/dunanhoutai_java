package com.junyang.exceptionHandler;
import javax.validation.ConstraintViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;


@RestControllerAdvice
public class CommonExceptionHandle extends BaseApiService{

	@ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public ResponseBase handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		return setResult(400, ex.getMessage(), null);
    }
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseBody
    public ResponseBase handleConstraintViolationException(ConstraintViolationException ex) {
    	return setResult(400, ex.getMessage(), null);
    }
    
    @ExceptionHandler({BindException.class})
    @ResponseBody
    public ResponseBase BindException(BindException ex) {
    	return setResult(400, ex.getMessage(), null);
    }
    
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResponseBase exceptionHandler(BindException ex) {
    	return setResultError("系统异常，请稍后再试");
    }
}
