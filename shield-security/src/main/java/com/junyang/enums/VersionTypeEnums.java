package com.junyang.enums;

public enum VersionTypeEnums {
	IOS(1,"IOS"),
	ANDROID(2, "安卓"),
	GOOGLE(3, "谷歌"),
	DIGTALSHIELD(4, "设备");

	    private Integer index;

	    private String name;


	    private VersionTypeEnums(Integer index, String name) {
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
	    	VersionTypeEnums[] carTypeEnums = values();
	        for (VersionTypeEnums carTypeEnum : carTypeEnums) {
	            if (carTypeEnum.getIndex().equals(i)) {
	                return carTypeEnum.getName();
	            }
	        }
	        return null;
	    }

	    public static Integer getIndex(String index) {
	    	VersionTypeEnums[] carTypeEnums = values();
	        for (VersionTypeEnums carTypeEnum : carTypeEnums) {
	            if (carTypeEnum.getName().equals(index)) {
	                return carTypeEnum.getIndex();
	            }
	        }
	        return null;
	    }


}
