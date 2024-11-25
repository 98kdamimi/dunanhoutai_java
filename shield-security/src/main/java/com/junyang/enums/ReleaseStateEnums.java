package com.junyang.enums;

public enum ReleaseStateEnums {
	TOP_LINE(2,"上线","ONLINE"),
	DOWN_LINE(1, "下线","OFFLINE"),
	WAIT_LINE(0, "待上线","WAIT");

	    private Integer index;

	    private String name;
	    
	    private String value;


	    private ReleaseStateEnums(Integer index, String name, String value) {
	        this.index = index;
	        this.name = name;
	        this.value = value;
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
