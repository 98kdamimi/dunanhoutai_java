package com.junyang.entity.monitorEvent;

import java.util.List;

import lombok.Data;

@Data
public class MonitorEventEntity {
	
	private String event; // 监控的事件类型
	
    private String chainShortName; // 公链缩写符号
    
    private String webhookUrl; // 接收消息的URL
    
    private String trackerName; // 监控任务名称
    
    private List<String> addresses; // 地址
    
    private List<String> tokenContractAddress; // 代币合约地址（可选）
    
    private AmountFilter amountFilter; // 代币数量筛选（可选）
    
    private ValueUsdFilter valueUsdFilter; // 美元价值筛选（可选）
    
    public static class AmountFilter {
        private String minAmount; // 最小代币数量
        private String maxAmount; // 最大代币数量
        
        // Getter & Setter
        public String getMinAmount() {
            return minAmount;
        }

        public void setMinAmount(String minAmount) {
            this.minAmount = minAmount;
        }

        public String getMaxAmount() {
            return maxAmount;
        }

        public void setMaxAmount(String maxAmount) {
            this.maxAmount = maxAmount;
        }
    }
    
    public static class ValueUsdFilter {
        private String minValueUsd; // 交易的最小金额
        private String maxValueUsd; // 交易的最大金额
        
        // Getter & Setter
        public String getMinValueUsd() {
            return minValueUsd;
        }

        public void setMinValueUsd(String minValueUsd) {
            this.minValueUsd = minValueUsd;
        }

        public String getMaxValueUsd() {
            return maxValueUsd;
        }

        public void setMaxValueUsd(String maxValueUsd) {
            this.maxValueUsd = maxValueUsd;
        }
    }

}
