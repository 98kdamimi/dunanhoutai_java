package com.junyang.entity.tronsignature;

import java.util.List;

import lombok.Data;

/**
 * @category 多签交易数据
 * @description 该实体类用于表示多签交易数据，包含了多签交易的相关信息。
 * @author Hlin
 */
@Data
public class TronSignatureEntity {
     // 交易金额
     private long amount;
     // 调用值（通常与合约调用相关）
     private String callValue;
     // 合约数据
     private ContractData contractData;
     // 合约类型
     private String contractType;
     // 当前交易信息
     private CurrentTransaction currentTransaction;
     // 备用的当前交易数据（可能为冗余字段）
     private String currentTransaction2;
     // 当前交易的权重
     private int currentWeight;
     // 交易过期时间
     private long expireTime;
     // 发送方地址
     private String fromAddress;
     // 函数选择器（标识调用的函数）
     private String functionSelector;
     // 交易的哈希值
     private String hash;
     // 是否已签名（0为未签名，1为已签名）
     private int isSign;
     // 发起者地址
     private String originatorAddress;
     // 签名进度列表
     private List<SignatureProgress> signatureProgress;
     // 当前交易状态
     private int state;
     // 签名阈值（达到该值则交易完成）
     private int threshold;
     // 接收方地址
     private String toAddress;
     // 代币信息
     private TokenInfo tokenInfo;
     
     /**
      * 合约数据
      * @description 包含合约的金额、发起者地址和接收者地址
      */
     @Data
     public static class ContractData {
         // 合约金额
         private long amount;
         // 合约发起者地址
         private String owner_address;
         // 合约接收者地址
         private String to_address;
     }

     /**
      * 当前交易信息
      * @description 包含交易的原始数据及签名信息
      */
     @Data
     public static class CurrentTransaction {
         // 原始交易数据
         private RawData raw_data;
         // 原始数据的十六进制表示
         private String raw_data_hex;
         // 签名列表
         private List<String> signature;
     }

     /**
      * 原始数据
      * @description 包含交易的基本信息，如块数据、时间戳、合约数据等
      */
     @Data
     public static class RawData {
         // 参考区块字节数
         private String ref_block_bytes;
         // 参考区块哈希
         private String ref_block_hash;
         // 交易过期时间
         private long expiration;
         // 交易附加数据
         private String data;
         // 交易合约列表
         private List<Contract> contract;
         // 脚本
         private String scripts;
         // 交易时间戳
         private long timestamp;
     }

     /**
      * 合约数据
      * @description 包含合约类型及相关参数
      */
     @Data
     public static class Contract {
         // 合约类型
         private String type;
         // 合约参数
         private Parameter parameter;
     }

     /**
      * 合约参数
      * @description 包含参数的类型和具体值
      */
     @Data
     public static class Parameter {
         // 参数类型的URL
         private String type_url;
         // 参数值
         private Value value;
     }

     /**
      * 参数值
      * @description 包含合约参数的金额、发起者地址和接收者地址
      */
     @Data
     public static class Value {
         // 合约金额
         private long amount;
         // 合约发起者地址
         private String owner_address;
         // 合约接收者地址
         private String to_address;
     }

     /**
      * 签名进度
      * @description 包含签名进度的信息，包括签名者地址、签名状态、签名时间和权重
      */
     @Data
     public static class SignatureProgress {
         // 签名者地址
         private String address;
         // 是否已签名（0为未签名，1为已签名）
         private int isSign;
         // 签名时间
         private long signTime;
         // 签名者的权重
         private int weight;
     }

     /**
      * 代币信息
      * @description 包含有关代币的详细信息，包括代币的名称、符号、等级、价格等
      */
     @Data
     public static class TokenInfo {
         // 代币符号（例如：TRX）
         private String tokenAbbr;
         // 是否可以显示
         private int tokenCanShow;
         // 代币的小数位数
         private int tokenDecimal;
         // 代币ID
         private String tokenId;
         // 代币等级
         private String tokenLevel;
         // 代币Logo
         private String tokenLogo;
         // 代币名称
         private String tokenName;
         // 代币类型
         private String tokenType;
         // 是否VIP代币
         private boolean vip;
     }
}
