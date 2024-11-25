// package com.junyang.exceptionHandler;
// import java.util.ArrayList;
//import java.util.Enumeration;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.amqp.core.AmqpTemplate;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.alibaba.fastjson.JSON;
//import com.junyang.aop.SysLogAnnotation;
//import com.junyang.base.BaseApiService;
//import com.junyang.base.ResponseBase;
//import com.junyang.constants.Constants;
//import com.junyang.utils.RedisUtil;
//
//
// //全局异常捕获
// @ControllerAdvice
// public class GlobalExceptionHandler extends BaseApiService{
//	 
//	 @Autowired
//	 private AmqpTemplate amqpTemplate;
//	 
//	 @Autowired
//	 private JavaMailSender mailSender;
//	 
//	 @Autowired
//	 private RedisUtil redisUtil;
//	 
//	 @Value("${spring.mail.username}")
//	 private String SEND_EMAIL;
//	 
//	 @Value("${mail.sendto_user1}")
//	 private String SEND_TO_EMAIL;
//
// 	@ExceptionHandler(Exception.class)//捕获运行时异常
// 	@ResponseBody
// 	@SysLogAnnotation(module = "全局异常捕获",type = "异常捕获",remark = "异常捕获")
// 	public ResponseBase exceptionHandler(Exception e,HttpServletRequest request) {
// 		Map<String, Object> msg = new HashMap<>();
// 		msg.put("请求接口", request.getRequestURL().toString());
// 		Enumeration<String> enu = request.getParameterNames();
// 		List<Map<String, Object>> list = new ArrayList<>();
//		while (enu.hasMoreElements()) {
//			Map<String, Object> req = new HashMap<>();
//			String name = (String) enu.nextElement();
//			req.put("name", name);
//			req.put("value",  request.getParameter(name));
//			list.add(req);
//		}
//		msg.put("请求参数", list);
// 		msg.put(" 错误代码位置",e.getStackTrace()[0].toString());
// 		if(redisUtil.get(request.getRequestURL().toString()) == null || redisUtil.get(request.getRequestURL().toString()).toString().length() < 1) {
// 			redisUtil.set(request.getRequestURL().toString(), request.getRequestURL().toString());
// 	 		redisUtil.expire(request.getRequestURL().toString(), Constants.REDIS_KEY_EXPIRE);
// 	 		amqpTemplate.convertAndSend("msgExchange", "", JSON.toJSONString(msg));
// 		}
// 		return setResultError(Constants.EXCEPTION_MSG);
// 	}
// 	
// 	@RabbitListener(queues = "msg_queue_1")
// 	public void sendEmail(String mes) {
// 		System.out.println("发送邮件");
// 		SimpleMailMessage message = new SimpleMailMessage();
//		message.setFrom(SEND_EMAIL);
//		message.setTo(SEND_TO_EMAIL);
//		message.setSubject("系统异常通知，请及时处理！！！！");
//		message.setText(mes);
//		mailSender.send(message);
// 	}
// 	
// 	@RabbitListener(queues = "msg_queue_2")
// 	public void sendTel(String mes) {
// 		System.out.println("发送短信通知");
//// 		TxSendSms.sendInform();
// 	}
// 	
//	
// }
