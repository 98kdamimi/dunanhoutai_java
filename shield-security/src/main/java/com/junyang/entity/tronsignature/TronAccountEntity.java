package com.junyang.entity.tronsignature;

import java.util.List;

import lombok.Data;

/**
 * @category tron账户详情
 * @author Hlin
 *
 */
@Data
public class TronAccountEntity {
	 // 账户资源信息
    private AccountResource accountResource;
    // 账户类型
    private int accountType;
    // 获取的带宽冻结委托数量
    private long acquiredDelegateFrozenForBandWidth;
    // 获取的能量冻结委托数量
    private long acquiredDelegateFrozenForEnergy;
    // 获取的V2版本带宽冻结委托数量
    private long acquiredDelegatedFrozenV2BalanceForBandwidth;
    // 获取的V2版本能量冻结委托数量
    private long acquiredDelegatedFrozenV2BalanceForEnergy;
    // 是否已激活
    private boolean activated;
    // 激活权限列表
    private List<ActivePermission> activePermissions;
    // 地址
    private String address;
    // 地址标签logo
    private String addressTagLogo;
    // 允许交易的资源列表
    private List<Object> allowExchange; // 空数组，保持为Object类型
    // 公告信息
    private String announcement;
    // 账户余额
    private long balance;
    // 带宽信息
    private Bandwidth bandwidth;
    // 蓝色标签
    private String blueTag;
    // 可提现的金额V2
    private long canWithdrawAmountV2;
    // 创建日期（毫秒）
    private long dateCreated;
    // 带宽冻结委托数量
    private long delegateFrozenForBandWidth;
    // 能量冻结委托数量
    private long delegateFrozenForEnergy;
    // 委托冻结数据（空对象）
    private Object delegated; // 空对象，保持为Object类型
    // V2版本带宽委托冻结数量
    private long delegatedFrozenV2BalanceForBandwidth;
    // V2版本能量委托冻结数量
    private long delegatedFrozenV2BalanceForEnergy;
    // 能量消耗
    private double energyCost;
    // 交易记录列表（空数组）
    private List<Object> exchanges; // 空数组，保持为Object类型
    // 是否存在风险反馈
    private boolean feedbackRisk;
    // 冻结的数量
    private long freezing;
    // 冻结信息
    private Frozen frozen;
    // 带宽冻结数量
    private long frozenForBandWidth;
    // V2版本带宽冻结数量
    private long frozenForBandWidthV2;
    // 能量冻结数量
    private long frozenForEnergy;
    // V2版本能量冻结数量
    private long frozenForEnergyV2;
    // 冻结供应数据（空数组）
    private List<Object> frozenSupply; // 空数组，保持为Object类型
    // 灰色标签
    private String greyTag;
    // 最新操作时间（毫秒）
    private long latestOperationTime;
    // 名称
    private String name;
    // 网络成本
    private double netCost;
    // 所有者权限信息
    private OwnerPermission ownerPermission;
    // 红色标签
    private String redTag;
    // 代表者信息
    private Representative representative;
    // 奖励数量
    private int reward;
    // 奖励数量（数值）
    private long rewardNum;
    // 总冻结金额
    private long totalFrozen;
    // 总冻结金额V2
    private long totalFrozenV2;
    // 总交易次数
    private long totalTransactionCount;
    // 总交易数
    private long transactions;
    // 进入交易数
    private long transactionsIn;
    // 输出交易数
    private long transactionsOut;
    // V2版本解冻数量
    private long unfreezeV2;
    // 投票总数
    private long voteTotal;
    // 持有的带价格的代币信息
    private List<WithPriceToken> withPriceTokens;
    // 见证者信息
    private long witness;

    // 账户资源信息
    @Data
    public static class AccountResource {
        // 冻结能量余额
        private FrozenBalanceForEnergy frozenBalanceForEnergy;
    }

    // 冻结能量余额（暂时没有字段，保留为空类）
    @Data
    public static class FrozenBalanceForEnergy {
        // 空类，可以根据需要添加字段
    }

    // 激活权限
    @Data
    public static class ActivePermission {
        private int id; // 权限ID
        private List<PermissionKey> keys; // 权限的键列表
        private String operations; // 操作码
        private String permissionName; // 权限名称
        private int threshold; // 阈值
        private String type; // 权限类型
    }

    // 权限键（地址和权重）
    @Data
    public static class PermissionKey {
        private String address; // 地址
        private int weight; // 权重
    }

    // 带宽相关信息
    @Data
    public static class Bandwidth {
        private Assets assets; // 资产信息（空类）
        private long energyLimit; // 能量限制
        private double energyPercentage; // 能量百分比
        private long energyRemaining; // 剩余能量
        private long energyUsed; // 已用能量
        private long freeNetLimit; // 免费网络限制
        private double freeNetPercentage; // 免费网络百分比
        private long freeNetRemaining; // 免费网络剩余
        private long freeNetUsed; // 免费网络已用
        private long netLimit; // 网络限制
        private double netPercentage; // 网络百分比
        private long netRemaining; // 网络剩余
        private long netUsed; // 网络已用
        private long storageLimit; // 存储限制
        private double storagePercentage; // 存储百分比
        private long storageRemaining; // 剩余存储
        private long storageUsed; // 已用存储
        private long totalEnergyLimit; // 总能量限制
        private long totalEnergyWeight; // 总能量权重
        private long totalNetLimit; // 总网络限制
        private long totalNetWeight; // 总网络权重
    }

    // 资产信息（空类）
    @Data
    public static class Assets {
        // 该类目前为空，可以根据需要添加字段
    }

    // 冻结信息
    @Data
    public static class Frozen {
        private List<Object> balances; // 冻结余额列表（空数组）
        private long total; // 总冻结金额
    }

    // 所有者权限信息
    @Data
    public static class OwnerPermission {
        private List<PermissionKey> keys; // 权限键列表
        private String permissionName; // 权限名称
        private int threshold; // 阈值
    }

    // 代表者信息
    @Data
    public static class Representative {
        private long allowance; // 允许的金额
        private boolean enabled; // 是否启用
        private long lastWithDrawTime; // 上次提现时间
        private String url; // 网址
    }

    // 带价格的代币信息
    @Data
    public static class WithPriceToken {
        private String amount; // 数量
        private String balance; // 余额
        private String tokenAbbr; // 代币缩写
        private int tokenCanShow; // 是否显示
        private int tokenDecimal; // 小数位数
        private String tokenId; // 代币ID
        private String tokenLogo; // 代币Logo
        private String tokenName; // 代币名称
        private double tokenPriceInTrx; // 代币价格（TRX）
        private String tokenType; // 代币类型
        private boolean vip; // 是否VIP
    }
}
