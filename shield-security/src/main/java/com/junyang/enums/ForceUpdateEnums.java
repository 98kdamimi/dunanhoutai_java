package com.junyang.enums;

/**
 * @category 强制更新标志 
 * @author Hlin
 *
 */
public enum ForceUpdateEnums {
	ENFORCEMENT(1, "1"),
	NOT_ENFORCEMENT(2, "2");

	    private Integer index;

	    private String name;


	    private ForceUpdateEnums(Integer index, String name) {
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
