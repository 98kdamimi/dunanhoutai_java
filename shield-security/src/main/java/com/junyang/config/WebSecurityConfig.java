package com.junyang.config;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.junyang.dao.system.SysUserDao;
import com.junyang.filter.JWTAuthenticationFilter;
import com.junyang.filter.JWTLoginFilter;
import com.junyang.handler.AuthenticationLogout;
import com.junyang.handler.TokenAccessDeniedHandler;
import com.junyang.handler.TokenAuthenticationEntryPoint;
import com.junyang.security.CustomAuthenticationProvider;
import com.junyang.utils.RedisUtil;

/**
 * SpringSecurity配置类
 * 通过SpringSecurity的配置，将JWTLoginFilter，JWTAuthenticationFilter组合在一起
 * securedEnabled = true 注解控制方法权限
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

     //身份认证时需要使用，注入我们实现了这个接口的类
   private UserDetailsService userDetailsService;
   
   @Value("${DEFAULT_IP}")
   private String DEFAULT_IP;

   @Autowired
   RedisUtil redisUtil;

   @Autowired
   AuthenticationLogout authenticationLogout;

   @Autowired
   private SysUserDao sysUserMapper;
   
   @Autowired
   private MongoTemplate mongoTemplate; 

   private HttpServletResponse response;

    public WebSecurityConfig(UserDetailsService userDetailsService, SysUserDao sysUserMapper,HttpServletResponse response) {
        this.userDetailsService = userDetailsService;
        this.sysUserMapper = sysUserMapper;
        this.response = response;
    }

    /**
     * 设置HTTP验证规则
     * @param http 描述此参数
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.cors().and().csrf().disable()
		        //会话创建策略：无状态
		        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		        .authorizeRequests()
                //druid放行
		        .antMatchers("/statistics/**").permitAll()
                .antMatchers("/druid/**").permitAll()
                .antMatchers("/sysApk/findLastVersion").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/version/msgWarn").permitAll()
                .antMatchers("/webhook/**").permitAll()
                .antMatchers("/tronSignature/**").permitAll()
                .antMatchers("/delegatelist/**").permitAll()
                //swagger放行
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/configuration/ui").permitAll()
                .antMatchers("/configuration/security").permitAll()
                .antMatchers( "/*.html","/**/*.html","/**/*.css", "/**/*.js","/webSocket/**").permitAll()
                //其余所有请求需要身份认证
                .anyRequest().authenticated()
                .and()
                .logout()
                .permitAll()
                .logoutSuccessHandler(authenticationLogout) //注销时的逻辑
                .and()
		        .exceptionHandling().authenticationEntryPoint(new TokenAuthenticationEntryPoint())  //未登录时的逻辑处理
		        .accessDeniedHandler(new TokenAccessDeniedHandler())   //权限不足时的逻辑处理
		        //添加拦截器
                .and()
                .addFilter(new JWTLoginFilter(authenticationManager(),redisUtil,sysUserMapper,mongoTemplate, DEFAULT_IP))
                .addFilter(new JWTAuthenticationFilter(authenticationManager(),redisUtil));
    }


    /**
     * 使用自定义身份验证组件
     * @param auth 身份认证管理器
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(new CustomAuthenticationProvider(userDetailsService, sysUserMapper,response));
    }

}
