package com.junyang.enums;

public enum UserStateEnums {
    DIMISSION(2,"离职"),//离职，账号未注销
	NORMAL(1, "正常"),
	LOGOUT(0, "注销");//账号已删除

	    private Integer index;

	    private String name;


	    private UserStateEnums(Integer index, String name) {
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

	    public static String getValue(int i) {
	    	UserStateEnums[] carTypeEnums = values();
	        for (UserStateEnums carTypeEnum : carTypeEnums) {
	            if (carTypeEnum.getIndex().equals(i)) {
	                return carTypeEnum.getName();
	            }
	        }
	        return null;
	    }

	    public static Integer getIndex(String index) {
	    	UserStateEnums[] carTypeEnums = values();
	        for (UserStateEnums carTypeEnum : carTypeEnums) {
	            if (carTypeEnum.getName().equals(index)) {
	                return carTypeEnum.getIndex();
	            }
	        }
	        return null;
	    }


}
