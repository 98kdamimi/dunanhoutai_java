package com.junyang.enums;

public enum ReleaseStateEnums {
	TOP_LINE(2,"上线","ONLINE","LISTED"),
	DOWN_LINE(1, "下线","OFFLINE","DRAFT"),
	WAIT_LINE(0, "待上线","WAIT","");

	    private Integer index;

	    private String name;
	    
	    private String value;
	    
	    private String lable;


	    private ReleaseStateEnums(Integer index, String name, String value, String lable) {
	        this.index = index;
	        this.name = name;
	        this.value = value;
	        this.lable = lable;
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
	    
	    public String getValue() {
	        return value;
	    }


	    public void setValue(String value) {
	        this.value = value;
	    }
	    
	    public String getLable() {
	        return lable;
	    }


	    public void setLable(String lable) {
	        this.lable = lable;
	    }



	    public static String getValue(int i) {
	    	ReleaseStateEnums[] carTypeEnums = values();
	        for (ReleaseStateEnums carTypeEnum : carTypeEnums) {
	            if (carTypeEnum.getIndex().equals(i)) {
	                return carTypeEnum.getName();
	            }
	        }
	        return null;
	    }

	    public static Integer getIndex(String index) {
	    	ReleaseStateEnums[] carTypeEnums = values();
	        for (ReleaseStateEnums carTypeEnum : carTypeEnums) {
	            if (carTypeEnum.getName().equals(index)) {
	                return carTypeEnum.getIndex();
	            }
	        }
	        return null;
	    }


}
