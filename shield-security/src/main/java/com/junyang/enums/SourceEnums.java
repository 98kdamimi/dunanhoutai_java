package com.junyang.enums;

import java.util.ArrayList;
import java.util.List;

import com.junyang.entity.response.DicEntity;

/**
 * @category 来源
 * @author coocaa
 *
 */
public enum SourceEnums {
	COINGECKO("Coingecko"),
	UNISWAP("Uniswap Labs Default"),
	SUSHI("sushi");

	private String name;
	

	private SourceEnums(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static List<DicEntity> getList() {
		SourceEnums[] carTypeEnums = values();
		List<DicEntity> list = new ArrayList<>();
		for (SourceEnums carTypeEnum : carTypeEnums) {
			DicEntity dicEntity = new DicEntity();
			dicEntity.setName(carTypeEnum.getName());
			list.add(dicEntity);
		}
		return list;
	}

}
