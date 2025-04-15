package com.junyang.entity.tronsignature;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection = "multisignatures")
@ApiModel(value = "多签记录", description = "波场多签记录实体类")
public class MultiSignaturesEntity {

    @ApiModelProperty(name = "visible", value = "是否可见", required = false, dataType = "Boolean")
    @Field("visible")
    private boolean visible;

    @ApiModelProperty(name = "rawaddress", value = "原始地址", required = false, dataType = "String")
    @Field("rawaddress")
    private String rawaddress;

    @ApiModelProperty(name = "txID", value = "交易ID", required = false, dataType = "String")
    @Field("txID")
    private String txID;

    @ApiModelProperty(name = "rawData", value = "原始数据", required = false, dataType = "RawData")
    @Field("raw_data")
    private RawData rawData;

    @ApiModelProperty(name = "rawDataHex", value = "原始数据Hex", required = false, dataType = "String")
    @Field("raw_data_hex")
    private String rawDataHex;

    @ApiModelProperty(name = "signature", value = "签名列表", required = false, dataType = "List<String>")
    @Field("signature")
    private List<String> signature;

    @Data
    @ApiModel(value = "原始数据", description = "交易的原始数据")
    public static class RawData {

        @ApiModelProperty(name = "contract", value = "合约列表", required = false, dataType = "List<Contract>")
        @Field("contract")
        private List<Contract> contract;

        @ApiModelProperty(name = "refBlockBytes", value = "引用区块字节", required = false, dataType = "String")
        @Field("ref_block_bytes")
        private String refBlockBytes;

        @ApiModelProperty(name = "refBlockHash", value = "引用区块哈希", required = false, dataType = "String")
        @Field("ref_block_hash")
        private String refBlockHash;

        @ApiModelProperty(name = "expiration", value = "过期时间", required = false, dataType = "Long")
        @Field("expiration")
        private long expiration;

        @ApiModelProperty(name = "timestamp", value = "时间戳", required = false, dataType = "Long")
        @Field("timestamp")
        private long timestamp;

        @Data
        @ApiModel(value = "合约", description = "交易合约")
        public static class Contract {

            @ApiModelProperty(name = "parameter", value = "参数", required = false, dataType = "Parameter")
            @Field("parameter")
            private Parameter parameter;

            @ApiModelProperty(name = "type", value = "合约类型", required = false, dataType = "String")
            @Field("type")
            private String type;

            @Data
            @ApiModel(value = "参数", description = "合约参数")
            public static class Parameter {

                @ApiModelProperty(name = "value", value = "值", required = false, dataType = "Value")
                @Field("value")
                private Value value;

                @ApiModelProperty(name = "typeUrl", value = "类型URL", required = false, dataType = "String")
                @Field("type_url")
                private String typeUrl;

                @Data
                @ApiModel(value = "值", description = "参数值")
                public static class Value {

                    @ApiModelProperty(name = "amount", value = "金额", required = false, dataType = "Long")
                    @Field("amount")
                    private long amount;

                    @ApiModelProperty(name = "ownerAddress", value = "发送方地址", required = false, dataType = "String")
                    @Field("owner_address")
                    private String ownerAddress;

                    @ApiModelProperty(name = "toAddress", value = "接收方地址", required = false, dataType = "String")
                    @Field("to_address")
                    private String toAddress;
                }
            }
        }
    }


    @ApiModelProperty(name = "ownerPermission", value = "所有者权限", required = false, dataType = "OwnerPermission")
    @Field("owner_permission")
    private OwnerPermission ownerPermission;

    @Data
    @ApiModel(value = "所有者权限", description = "所有者权限信息")
    public static class OwnerPermission {

        @ApiModelProperty(name = "keys", value = "密钥列表", required = false, dataType = "List<Key>")
        @Field("keys")
        private List<Key> keys;

        @ApiModelProperty(name = "threshold", value = "阈值", required = false, dataType = "Integer")
        @Field("threshold")
        private Integer threshold;

        @ApiModelProperty(name = "permissionName", value = "权限名称", required = false, dataType = "String")
        @Field("permission_name")
        private String permissionName;

        @Data
        @ApiModel(value = "密钥", description = "密钥信息")
        public static class Key {

            @ApiModelProperty(name = "address", value = "地址", required = false, dataType = "String")
            @Field("address")
            private String address;

            @ApiModelProperty(name = "weight", value = "权重", required = false, dataType = "Integer")
            @Field("weight")
            private Integer weight;
        }
    }

}