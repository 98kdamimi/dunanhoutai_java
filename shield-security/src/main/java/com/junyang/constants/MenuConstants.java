package com.junyang.constants;

/**
 * 菜单常量类
 *
 * 业务中，一般用户和角色是可以灵活设置的，而角色与菜单权限也是可以灵活设置的
 * 唯有，菜单和功能方法是唯一确定的，所以我们用sys_menu的menu_code来做@Secured的权限属性，
 * 本例只细节到大菜单，没有细节到具体功能接口，如果要做肯定也是可以的
 *
 * 这里的常量要加final，不然@Secured会报错
 *
 */
public class MenuConstants {

    private MenuConstants(){
    }

    /**
     * ROLE_PREFIX 角色的前缀
     * 权限的前缀一定要是ROLE_不然不能够识别，导致所有权限都不能进入
     */
    public static final String ROLE_PREFIX = "ROLE_";

    /**
     * SYS_MANAGEMENT 系统管理
     */
    public static final String SYS_MANAGEMENT = ROLE_PREFIX + "sys";

    /**
     * USER_MANAGEMENT 用户管理
     */
    public static final String USER_MANAGEMENT = ROLE_PREFIX + "userManagement";

    /**
     * ROLE_MANAGEMENT 角色管理
     */
    public static final String ROLE_MANAGEMENT = ROLE_PREFIX + "roleManagement";
    
    /**
     * 会员管理
     */
    public static final String MEMBER_MANAGEMENT = ROLE_PREFIX + "memberManagement";
    
    /**
     * 会员卡管理
     */
    public static final String CARD_MANAGEMENT = ROLE_PREFIX + "member:list";

    /**
     * 商品管理
     */
    public static final String GOODS_MANAGEMENT = ROLE_PREFIX + "goodsManagement";
    
    /**
     * 会员进出场
     */
    public static final String MEMBER_ENTRANCE = ROLE_PREFIX+"memberEntrance";
    
    /**
     * 会员上课记录
     */
    public static final String ATTEND_CLASS = ROLE_PREFIX+"attendClass";
    
    /**
     * 会员跟进记录
     */
    public static final String FOLLOW_LIST = ROLE_PREFIX+"followList";
    
    
    /**
     * 会员缴费记录
     */
    public static final String MEM_PAY_LIST = ROLE_PREFIX+"memPayList";
}
