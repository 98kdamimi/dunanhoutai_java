package com.junyang.enums;

public enum WebHookUrlEnums {
	WEBHOOK_TASK_LIST(1, "任务列表","/api/v5/explorer/webhook/get-tracker-list"),//任务列表
	WEBHOOK_ADD_TASK(2, "创建 Webhook 任务","/api/v5/explorer/webhook/create-address-activity-tracker"),
	WEBHOOK_ADDRESSES_TASK(3, "获取 Webhook 任务订阅地址列表","/api/v5/explorer/webhook/get-tracker-addresses"),
	WEBHOOK_ADD_ADDRESSES(4, "修改webhook任务地址集合","/api/v5/explorer/webhook/add-and-remove-tracker-addresses");//
	
	private Integer index;

	private String name;
	
	private String value;


	private WebHookUrlEnums(Integer index, String name, String value) {
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
		 WebHookUrlEnums[] typeEnums = values();
	        for (WebHookUrlEnums typeEnum : typeEnums) {
	            if (typeEnum.getIndex().equals(i)) {
	                return typeEnum.getName();
	            }
	        }
	        return null;
	    }

}
