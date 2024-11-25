package com.junyang.aspect;
import java.lang.reflect.Method;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.junyang.aop.SysLogAnnotation;
import com.junyang.base.ResponseBase;
import com.junyang.entity.system.SysLogEntity;
import com.junyang.filter.JWTAuthenticationFilter;
import com.junyang.utils.RedisUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @category 日志统一管理
 * @author csz
 *
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
//	@Autowired
//	private SysLogEntity entity = new SysLogEntity();

	@Pointcut("execution(public * com.junyang.service..*.*(..))")
	public void webLog() {
		
	}

	@Before("webLog()")
	public void doBefore(JoinPoint joinPoint) throws Throwable {
		 // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        UsernamePasswordAuthenticationToken token = JWTAuthenticationFilter.getAuthentication(request);
		String username = token.getName();
//		entity.setUserName(username);
        // 记录请求开始
        log.info("##################### 请求开始 ####################");
        log.info("URL : " + request.getRequestURL().toString());
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("IP : " + request.getRemoteAddr());
//        entity.setReqUrl(request.getRequestURL().toString());
//        entity.setIpUrl(request.getRemoteAddr());
        // 获取目标方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // 获取 SysLogAnnotation 注解
        SysLogAnnotation sysLogAnnotation = method.getAnnotation(SysLogAnnotation.class);

        if (sysLogAnnotation != null) {
            // 获取注解值
            String module = sysLogAnnotation.module();
            String type = sysLogAnnotation.type();
            String remark = sysLogAnnotation.remark();
            // 打印注解中的信息
            log.info("操作模块 : " + module);
            log.info("操作类型 : " + type);
            log.info("操作说明 : " + remark);
//            entity.setModuleName(module);
//            entity.setReqType(type);
//            entity.setModuleInfo(remark);
        }
		 Object[] args = joinPoint.getArgs();
		 if(args != null && args.length > 0 && args[0] != null) {
			 try {
			        String parameterJson = JSON.toJSONString(args[0]);
			        log.info("PARAMETER：" + request.getRequestURL().toString() + "-[" + parameterJson+"]");
//			        entity.setReqParamet("[" + parameterJson+"]");
			    } catch (JSONException e) {
			        log.error("PARAMETER：" + request.getRequestURL().toString() + "-[]");
//			        entity.setReqParamet("[]");
			}
		 }
		Enumeration<String> enu = request.getParameterNames();
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			log.info("name:{" + name + "},value:{" + request.getParameter(name) + "}");
		}
	}
	
	@AfterReturning(returning = "ret", pointcut = "webLog()")
	public void doAfterReturning(JoinPoint point, Object ret) throws Throwable {
		String type = point.getSignature().toLongString().split(" ")[1];
		if (type != null &&  !"void".equals(type)) {
			if(ret != null) {
				ResponseBase base = JSONObject.parseObject(JSON.toJSONString(ret), ResponseBase.class);
				log.info("RESPONSE11 : "+point.getSignature().toString().substring(point.getSignature().toString().indexOf(" "))+"-" + (base.getMsg()));
			}
			log.info("RESPONSE22 : " + (ret==null?"":JSON.toJSONString(ret)));
		}
		log.info("#####################请求结束####################");
	}
}
