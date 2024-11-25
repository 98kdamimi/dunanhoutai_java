package com.junyang.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.junyang.handler.ApiIdempotentInterceptor;
import com.junyang.utils.RedisUtil;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	 private RedisUtil redisUtil; // 替换为你的自定义 Redis 工具类

	    public InterceptorConfig(RedisUtil redisUtil) {
	        this.redisUtil = redisUtil;
	    }

	    // 省略其他代码...

	    @Bean
	    public ApiIdempotentInterceptor apiIdempotentInterceptor() {
	        return new ApiIdempotentInterceptor(redisUtil); // 将自定义 Redis 工具类传递给拦截器的构造函数
	    }

}