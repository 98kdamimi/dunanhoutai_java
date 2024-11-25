package com.junyang.enums;

import java.util.ArrayList;
import java.util.List;

public enum RoleTypeEnums {

    ADMIN(1, "超级管理员"),
    TOWN_LEAD(35,"镇领导") ,
    DISTRICT_LEAD(32, "管区领导"),
    WORK_LEAD(3, "分工领导"),
    DEPT_DUTY_MAN(33,"村庄责任人"),
    DUTY_MAN(31,"部门责任人"),
    ROOT(34,"管理员"),
    SOCIETY_DEPT(36,"民生办");
	
	
	
    private Integer index;

    private String name;


    private RoleTypeEnums(Integer index, String name) {
        this.index = index;
        this.name = name;
    }


    public Integer getIndex() {
        return index;
    }


    public void setIndex(Integer index) {
        this.index = index;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @category 责任人角色
     * @return
     */
    public static List<Integer> getDuty() {
    	List<Integer> roleList = new ArrayList<Integer>();
		roleList.add(RoleTypeEnums.DEPT_DUTY_MAN.getIndex());
		roleList.add(RoleTypeEnums.DUTY_MAN.getIndex());
		return roleList;
	}
    
    /**
     * @category 监督人角色
     * @return
     */
    public static List<Integer> getLead() {
    	List<Integer> roleList = new ArrayList<Integer>();
		roleList.add(RoleTypeEnums.DISTRICT_LEAD.getIndex());
		roleList.add(RoleTypeEnums.WORK_LEAD.getIndex());
		return roleList;
	}
    
    public static List<Integer> getRole() {
    	List<Integer> roleList = new ArrayList<Integer>();
		roleList.add(RoleTypeEnums.TOWN_LEAD.getIndex());
		roleList.add(RoleTypeEnums.DISTRICT_LEAD.getIndex());
		roleList.add(RoleTypeEnums.WORK_LEAD.getIndex());
		roleList.add(RoleTypeEnums.DEPT_DUTY_MAN.getIndex());
		roleList.add(RoleTypeEnums.DUTY_MAN.getIndex());
		roleList.add(RoleTypeEnums.SOCIETY_DEPT.getIndex());
		return roleList;
	}
    
    public static boolean adminJudge(List<Integer> roleIdList,List<Integer> newRoleLIst){
    	boolean containsAdminRole = false;
		for (Integer adminRole : newRoleLIst) {
		    if (roleIdList.contains(adminRole)) {
		        containsAdminRole = true;
		        break; // 如果找到一个匹配的角色，跳出循环
		    }
		}
        return containsAdminRole;
    }
    

}
