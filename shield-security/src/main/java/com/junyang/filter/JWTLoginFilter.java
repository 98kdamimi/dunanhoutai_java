package com.junyang.filter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junyang.constants.Constants;
import com.junyang.dao.system.SysUserDao;
import com.junyang.entity.ipWhitelist.IpWhitelistEntity;
import com.junyang.entity.system.SysUserEntity;
import com.junyang.enums.UserStateEnums;
import com.junyang.exception.BaseException;
import com.junyang.utils.CustomUtils;
import com.junyang.utils.GoogleAuthenticatorUtil;
import com.junyang.utils.JsonData;
import com.junyang.utils.RedisUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * jwt登录拦截器 登录controller方法不用自己写，直接访问/login就行
 * 该类继承自UsernamePasswordAuthenticationFilter，重写了其中的2个方法
 * 验证用户名密码正确后，生成一个token，并将token返回给客户端 attemptAuthentication ：接收并解析用户凭证。
 * successfulAuthentication ：用户成功登录后，这个方法会被调用，我们在这个方法里生成token。
 *
 */
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {
	
	@Autowired
	private GoogleAuthenticatorUtil googleAuthenticator;
	
	private AuthenticationManager authenticationManager;

	private RedisUtil redisUtil;

	private SysUserDao sysUserDao;
	
	private MongoTemplate mongoTemplate;
	
	private String defaultIp;

	public JWTLoginFilter(AuthenticationManager authenticationManager, RedisUtil redisUtil,
			SysUserDao sysUserDao, MongoTemplate mongoTemplate,String defaultIp) {
		this.authenticationManager = authenticationManager;
		this.redisUtil = redisUtil;
		this.sysUserDao = sysUserDao;
		this.mongoTemplate = mongoTemplate;
		this.defaultIp = defaultIp;
	}

	/**
	 * 处理登录请求，校验用户名和密码
	 * 
	 * @param req 请求
	 * @param res 响应
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		try {
			 // 获取客户端 IP
	        String ipAddress = getClientIp(req);
	        System.out.println("******************"+ipAddress);
	        // 查询 IP 是否在白名单
	        boolean isAllowed = false;
	        Query query = new Query(Criteria.where("ipSite").is(ipAddress));
	        IpWhitelistEntity whitelistEntity = mongoTemplate.findOne(query, IpWhitelistEntity.class);
	        if (whitelistEntity != null || isLocalIp(ipAddress)) {
	            isAllowed = true;
	        }
	        
			if(isAllowed) {
				// 请求头反序列化获得User对象
				SysUserEntity sysUserLoginDTO = new ObjectMapper().readValue(req.getInputStream(), SysUserEntity.class);
				if (sysUserLoginDTO.getGoogleCode() != null) {
					redisUtil.set(Constants.GOOGLE_COCE, sysUserLoginDTO.getGoogleCode());
					return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
							sysUserLoginDTO.getUsername(), sysUserLoginDTO.getPassword(), new ArrayList<>()));
				} else {
					CustomUtils.sendJsonMessage(res, JsonData.Error("请输入谷歌验证码"));
					return null;
				}
			}else {
				CustomUtils.sendJsonMessage(res, JsonData.Error("对不起，你的IP地址没有访问权限"));
				return null;
			}
		} catch (IOException e) {
			throw new BaseException(e);
		}
	}

	/**
	 * 登录请求后，CustomAuthenticationProvider.authenticate身份验证通过会生成Authentication令牌
	 * 我们在这个方法里对令牌生成token，并设置在响应头中
	 * 
	 * @param req   请求
	 * @param res   响应
	 * @param chain 过滤链
	 * @param auth  身份认证
	 */
	@SuppressWarnings("static-access")
	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		String code = redisUtil.get(Constants.GOOGLE_COCE).toString();
		
		// 获取权限主题
		String subject = auth.getName();
		SysUserEntity entity = sysUserDao.findByAcctiveState(subject, UserStateEnums.NORMAL.getIndex());
		if(entity.getGoogleSecretkey() !=null && entity.getGoogleSecretkey().length()>0) {
			Boolean temp = googleAuthenticator.verifyCode(entity.getGoogleSecretkey(), Integer.parseInt(code));
			if(temp) {
				// subject中存入用户名和角色权限
				String token = Jwts.builder()
						// 设置主题
						.setSubject(subject)
						// 设置到期时间
						.setExpiration(new Date(System.currentTimeMillis() + Constants.REDIS_EXPIRE_TIME))
						// 选择 加密算法和私钥
						.signWith(SignatureAlgorithm.HS512, Constants.SIGNING_KEY).compact();
				// 登录用户token存入redis
				if (subject.indexOf(",") == -1) {
					redisUtil.set(Constants.APP_PACKAGE_NAME + subject, Constants.AUTH_HEADER_START_WITH + token);
				} else {
					redisUtil.set(Constants.APP_PACKAGE_NAME + subject.substring(0, subject.indexOf(",")),
							Constants.AUTH_HEADER_START_WITH + token);
				}

				// token返回到请求头中，前端在请求头中获取
				res.setHeader(Constants.HEADER_ACCESS, Constants.HEADER_AUTH);
				res.addHeader(Constants.HEADER_AUTH, Constants.AUTH_HEADER_START_WITH + token);

				CustomUtils.sendJsonMessage(res, JsonData.buildSuccess("登录成功"));
			}else {
				CustomUtils.sendJsonMessage(res, JsonData.Error("谷歌验证失败"));
			}
		}else {
			CustomUtils.sendJsonMessage(res, JsonData.Error("谷歌验证失败"));
		}

	}
	
	/**
	 * 获取客户端真实 IP 地址（兼容代理）
	 */
	private String getClientIp(HttpServletRequest request) {
	    String ip = request.getHeader("X-Forwarded-For"); // 处理 Nginx 反向代理
	    if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
	        return ip.split(",")[0].trim(); // 获取真实 IP
	    }
	    ip = request.getHeader("Proxy-Client-IP");
	    if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
	        return ip;
	    }
	    ip = request.getHeader("WL-Proxy-Client-IP");
	    if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
	        return ip;
	    }
	    return request.getRemoteAddr(); // 直接获取 IP
	}

	/**
	 * 判断是否为本机或内网 IP
	 */
	private boolean isLocalIp(String ipAddress) {
	    if (ipAddress == null || ipAddress.trim().isEmpty()) {
	        return false;
	    }
	    ipAddress = ipAddress.trim();
	    return "127.0.0.1".equals(ipAddress) 
	    		|| defaultIp.equals(ipAddress)  
	    		|| "::1".equals(ipAddress) // 本机
	            || ipAddress.startsWith("192.168.") // 私有内网
	            || ipAddress.startsWith("10.") // A 类内网
	            || (ipAddress.startsWith("172.") && Integer.parseInt(ipAddress.split("\\.")[1]) >= 16
	                && Integer.parseInt(ipAddress.split("\\.")[1]) <= 31); // B 类内网
	}

}
