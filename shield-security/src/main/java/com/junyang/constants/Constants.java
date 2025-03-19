package com.junyang.constants;

/**
 * 
 * @author csz
 *
 */
public interface Constants {
	
	public static final String MSG_KEY = "新版本提醒";
		
	 /**
     * www主域
     */
    public static final String WWW = "www.";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";
    
    public static final String APP_PACKAGE_NAME = "shieldSecurity";
    
    public static final Integer FAST_EXPIRE_TIME = 3 * 24;//工单任务即将到期时长
    
	String  SUCCESS="操作成功";
	
	String ERROR = "操作失败，请稍后再试";
	
	String NULL_CONTETN="没有相关数据";
	
	String NULL_PARAMS="参数不能为空";
	
	String ADMIN_STR = "admin";
	
	int PAGENUMBER = 1;
	
	int PAGESIZE =10;
	
	int DAPP_PAGESIZE =10;
	
	Integer OPERATE_COUNT = 0;
	
	int FIND_ONE =1;
	
	Integer ODD_DAYS_ATTEND_CLASS_NUM=3;
	
	Integer REDIS_EXPIRE_TIME = 24 * 60 * 60 * 1000;
	
	Integer REDIS_TIME = 2 * 60;
	
	String ATTEND = "attend";
	
	Integer ENTRANCE_REDIS_EXPIRE_TIME = 10 * 60;
	
	// 响应请求成功
	String HTTP_RES_CODE_200_VALUE = "操作成功";
	// 系统错误
	String HTTP_RES_CODE_500_VALUE = "操作失败，请稍后再试";
	// 响应请求成功code
	Integer HTTP_RES_CODE_200 = 200;
	// 系统错误
	Integer HTTP_RES_CODE_500 = 500;

	Integer HTTP_RES_CODE_404 = 404;
	
	long REDIS_KEY_EXPIRE = 15*60*1000;
	
	String EXCEPTION_MSG = "系统繁忙，请稍后再试！！！";

    /**
     * AUTH_HEADER_START_WITH 请求中token前缀
     */
    public static final String AUTH_HEADER_START_WITH = "Bearer ";

    /**
     * SIGNING_KEY token生成密钥
     */
    public static final String SIGNING_KEY = "Boss@Jwt!&Secret^#";

    /**
     * HEADER_AUTH 请求header中token的key
     */
    public static final String HEADER_AUTH = "token";

	public static final String APP_LABLE = "appLable";
	/**
	 * 命名格式："大驼峰的业务名-随机字符串"
	 */
    public static final String APP_LABLE_VALUE = "AppCount-]a/sfUas";


    /**
     * SESSION_CAPTCHA 验证码
     */
    public static final String SESSION_CAPTCHA = "captcha";
    
    //配置前端响应头
    public static final String HEADER_ACCESS = "Access-Control-Expose-Headers";
    
    public static final String googleKey = "QAXMNIOPHPEMRSSV";
    
    public static final String user_googleKey = "3EDUIKMIXDQIZQIH";
    
    public static final String GOOGLE_COCE = "googleCoce";
    
    public static final String MSG_CONTENT_TYPE = "token";
    
    public static final String SEND_TYPE = "all";

}
