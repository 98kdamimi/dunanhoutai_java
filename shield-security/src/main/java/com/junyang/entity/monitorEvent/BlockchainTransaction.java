package com.junyang.entity.monitorEvent;

import lombok.Data;

@Data
public class BlockchainTransaction {

	  /**
     * 钱包地址
     */
    private String address;

    /**
     * 主链币种（如 ETH）
     */
    private String coin;

    /**
     * 确认数（多少个区块确认）
     */
    private int confirmations;

    /**
     * 区块高度
     */
    private long height;

    /**
     * 时间戳（单位：秒）
     */
    private long time;

    /**
     * 代币合约地址（例如 USDT 的合约地址）
     */
    private String tokenAddress;

    /**
     * 代币符号（例如 USDT）
     */
    private String tokenSymbol;

    /**
     * 代币数量（可以为负数，表示转出）
     */
    private String tokenValue;

    /**
     * 交易哈希（交易 ID）
     */
    private String txid;

    /**
     * 主链币种的转账数量（如 ETH 的 value，可为负数）
     */
    private String value;
	
}
