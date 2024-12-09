package com.junyang.enums;

public enum AgreementTypeEnums {
	USER_AGREEMENT(1, "用户协议","userGreement"),//用户协议
	PRIVACY_AGREEMENT(2, "隐私协议","privacyPolicy");//隐私协议
	
	private Integer index;

	private String name;
	
	private String value;


	private AgreementTypeEnums(Integer index, String name, String value) {
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


	public static String getName(int i) {
		 AgreementTypeEnums[] typeEnums = values();
	        for (AgreementTypeEnums typeEnum : typeEnums) {
	            if (typeEnum.getIndex().equals(i)) {
	                return typeEnum.getName();
	            }
	        }
	        return null;
	    }

}
