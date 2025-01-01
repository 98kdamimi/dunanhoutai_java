package com.junyang.enums;

import java.util.ArrayList;
import java.util.List;

import com.junyang.entity.response.DicEntity;

/**
 * @category 语言
 * @author Hlin
 *
 */
public enum LanguageEnums {
	ZH_CN(1, "zh-CN", "cht"), 
//	ZH_HK(2, "zh-HK", "繁体中文"),
//	FIL(3, "fil", ""), 
//	PT_BR(4, "pt-BR", ""),
	EN_US(5, "en-US", "en");

	private Integer index;

	private String name;

	private String value;

	private LanguageEnums(Integer index, String name, String value) {
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
		LanguageEnums[] typeEnums = values();
		for (LanguageEnums typeEnum : typeEnums) {
			if (typeEnum.getIndex().equals(i)) {
				return typeEnum.getName();
			}
		}
		return null;
	}
	
	public static List<DicEntity> getList() {
		LanguageEnums[] typeEnums = values();
		List<DicEntity> list = new ArrayList<>();
		for (LanguageEnums typeEnum : typeEnums) {
			DicEntity dicEntity = new DicEntity();
			dicEntity.setId(typeEnum.getIndex());
			dicEntity.setName(typeEnum.getName());
			dicEntity.setValue(typeEnum.getValue());
			list.add(dicEntity);
		}
		return list;
	}

}
