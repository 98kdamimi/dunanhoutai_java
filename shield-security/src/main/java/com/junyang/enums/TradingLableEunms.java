package com.junyang.enums;

import java.util.ArrayList;
import java.util.List;

import com.junyang.entity.response.DicEntity;

/**
 * @category 交易相关标签
 * @author coocaa
 *
 */
public enum TradingLableEunms {
	BUY(1, "购买"), SELL(2, "出售"), EXCHANGE(3, "兑换");

	private Integer index;

	private String name;

	private TradingLableEunms(Integer index, String name) {
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

	public static List<DicEntity> getList() {
		List<DicEntity> list = new ArrayList<DicEntity>();
		TradingLableEunms[] carTypeEnums = values();
		for (TradingLableEunms carTypeEnum : carTypeEnums) {
			DicEntity dicEntity = new DicEntity();
			dicEntity.setId(carTypeEnum.getIndex());
			dicEntity.setName(carTypeEnum.getName());
			list.add(dicEntity);
		}
		return list;
	}

}
