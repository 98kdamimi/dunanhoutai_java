package com.junyang.enums;

import java.util.ArrayList;
import java.util.List;

import com.junyang.entity.response.DicEntity;

public enum FilePathEnums {
	BIN(1, "bin/","version_db","bin版本文件"), APP(2, "app/","version_db","线上json文件"), 
	APK(3, "apk/","version_db","版本apk"), TOKENS(4, "tokens/","token_db","代币图标"), 
	NFTS(5, "nfts/","token_db","代币NFT图标"), ASSETS(6, "assets/","token_db","币种图标");

	private Integer index;

	private String name;
	
	private String databaseName;
	
	private String value;

	private FilePathEnums(Integer index, String name, String databaseName, String value) {
		this.index = index;
		this.name = name;
		this.databaseName = databaseName;
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
	
	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static String getName(int i) {
		FilePathEnums[] carTypeEnums = values();
		for (FilePathEnums carTypeEnum : carTypeEnums) {
			if (carTypeEnum.getIndex().equals(i)) {
				return carTypeEnum.getName();
			}
		}
		return null;
	}
	
	public static String getValue(int i) {
		FilePathEnums[] carTypeEnums = values();
		for (FilePathEnums carTypeEnum : carTypeEnums) {
			if (carTypeEnum.getIndex().equals(i)) {
				return carTypeEnum.getValue();
			}
		}
		return null;
	}

	public static Integer getIndex(String index) {
		FilePathEnums[] carTypeEnums = values();
		for (FilePathEnums carTypeEnum : carTypeEnums) {
			if (carTypeEnum.getName().equals(index)) {
				return carTypeEnum.getIndex();
			}
		}
		return null;
	}
	
	public static String getDatabaseName(int i) {
		FilePathEnums[] carTypeEnums = values();
		for (FilePathEnums carTypeEnum : carTypeEnums) {
			if (carTypeEnum.getIndex().equals(i)) {
				return carTypeEnum.getDatabaseName();
			}
		}
		return null;
	}
	
	public static List<DicEntity> getList() {
		FilePathEnums[] carTypeEnums = values();
		List<DicEntity> list = new ArrayList<>();
		for (FilePathEnums carTypeEnum : carTypeEnums) {
			DicEntity dicEntity = new DicEntity();
			dicEntity.setId(carTypeEnum.getIndex());
			dicEntity.setName(carTypeEnum.getName());
			dicEntity.setValue(carTypeEnum.getValue());
			list.add(dicEntity);
		}
		return list;
	}

}
