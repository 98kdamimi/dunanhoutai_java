package com.junyang.enums;

public enum NetWorkNameEnums {
	Bitcoin(1, "Bitcoin", "btc", "BtcTask"), ETHEREUM(2, "Ethereum", "eth", "EthTask"),
	BSC(3, "BNB Smart Chain", "bch", "BscTask"), SOLANA(4, "Solana", "SOLANA", "SolanaTask"),
	POLYGON(5, "Polygon", "POLYGON", "PolygonTask"), APTOS(6, "Aptos", "APT", "AptosTask"),
	DOGECOIN(7, "Dogecoin", "doge", "DogecoinTask"), LITECOIN(8, "Litecoin", "ltc", "LitecoinTask"),
	SUI(9, "SUI", "SUI", "SuiTask"), TRON(10, "Tron", "trx", "TronTask");

	private Integer index;

	private String name;

	private String value;

	private String lable;

	private NetWorkNameEnums(Integer index, String name, String value, String lable) {
		this.index = index;
		this.name = name;
		this.value = value;
		this.lable = lable;
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

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public static String getName(int i) {
		NetWorkNameEnums[] typeEnums = values();
		for (NetWorkNameEnums typeEnum : typeEnums) {
			if (typeEnum.getIndex().equals(i)) {
				return typeEnum.getName();
			}
		}
		return null;
	}

	public static String getValue(String name) {
		NetWorkNameEnums[] typeEnums = values();
		for (NetWorkNameEnums typeEnum : typeEnums) {
			if (typeEnum.getName().equals(name)) {
				return typeEnum.getValue();
			}
		}
		return null;
	}
	
	public static String getLable(String name) {
		NetWorkNameEnums[] typeEnums = values();
		for (NetWorkNameEnums typeEnum : typeEnums) {
			if (typeEnum.getName().equals(name)) {
				return typeEnum.getLable();
			}
		}
		return null;
	}

}
