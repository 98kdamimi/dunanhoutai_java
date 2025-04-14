package com.junyang.enums;

public enum TokenViweUrlEnums {
	ADDRESSES_LIST(1, "监控列表","https://services.tokenview.io/vipapi/monitor/address/list/"),
	ADDRESSES_ADD(2, "新增监控地址","https://services.tokenview.io/vipapi/monitor/address/add/");
	
	private Integer index;

	private String name;
	
	private String value;


	private TokenViweUrlEnums(Integer index, String name, String value) {
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
		 TokenViweUrlEnums[] typeEnums = values();
	        for (TokenViweUrlEnums typeEnum : typeEnums) {
	            if (typeEnum.getIndex().equals(i)) {
	                return typeEnum.getName();
	            }
	        }
	        return null;
	    }

}
