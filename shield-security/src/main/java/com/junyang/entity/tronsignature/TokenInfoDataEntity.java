package com.junyang.entity.tronsignature;

import lombok.Data;

@Data
public class TokenInfoDataEntity {
	 /**
     * 代币数量
     */
    private String amount;

    /**
     * 代币的美元金额
     */
    private String amountInUsd;

    /**
     * 余额
     */
    private String balance;

    /**
     * 等级
     */
    private String level;

    /**
     * 持有该代币的用户数量
     */
    private String nrOfTokenHolders;

    /**
     * 项目名称（如果有的话）
     */
    private String project;

    /**
     * 代币数量（具体数量）
     */
    private String quantity;

    /**
     * 代币缩写
     */
    private String tokenAbbr;

    /**
     * 是否能显示（1 为可显示，0 为不可显示）
     */
    private int tokenCanShow;

    /**
     * 代币的精度（小数位数）
     */
    private int tokenDecimal;

    /**
     * 代币的合约 ID
     */
    private String tokenId;

    /**
     * 代币的图标 URL
     */
    private String tokenLogo;

    /**
     * 代币名称（如 Tether USD）
     */
    private String tokenName;

    /**
     * 代币在 TRX 中的价格
     */
    private String tokenPriceInTrx;

    /**
     * 代币的美元价格
     */
    private String tokenPriceInUsd;

    /**
     * 代币类型（如 trc20）
     */
    private String tokenType;

    /**
     * 转账次数
     */
    private String transferCount;

    /**
     * 是否是 VIP 代币
     */
    private boolean vip;
}