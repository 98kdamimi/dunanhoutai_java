package com.junyang.enums;

public enum ActionTypeEnums {
	ADD(0, "add"), REMOVE(1, "remove"), REPLACE(2, "replace");

	private Integer index;

	private String name;

	private ActionTypeEnums(Integer index, String name) {
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

}
