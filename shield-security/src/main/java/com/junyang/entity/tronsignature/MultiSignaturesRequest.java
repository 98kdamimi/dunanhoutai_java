package com.junyang.entity.tronsignature;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "多签记录", description = "波场多签记录实体类")
public class MultiSignaturesRequest {

    @ApiModelProperty(name = "visible", value = "是否可见", required = false, dataType = "Boolean")
    private boolean visible;

    @ApiModelProperty(name = "rawaddress", value = "原始地址", required = false, dataType = "String")
    private String rawaddress;

    @ApiModelProperty(name = "txID", value = "交易ID", required = false, dataType = "String")
    @JsonProperty("txID")
    private String txID;

    @ApiModelProperty(name = "rawData", value = "原始数据", required = false, dataType = "RawData")
    @JsonProperty("raw_data")
    private RawData rawData;

    @ApiModelProperty(name = "rawDataHex", value = "原始数据Hex", required = false, dataType = "String")
    @JsonProperty("raw_data_hex")
    private String rawDataHex;

    @ApiModelProperty(name = "signature", value = "签名列表", required = false, dataType = "List<String>")
    private List<String> signature;

    @Data
    @ApiModel(value = "原始数据", description = "交易的原始数据")
    public static class RawData {

        @ApiModelProperty(name = "contract", value = "合约列表", required = false, dataType = "List<Contract>")
        private List<Contract> contract;

        @ApiModelProperty(name = "refBlockBytes", value = "引用区块字节", required = false, dataType = "String")
        @JsonProperty("ref_block_bytes")
        private String refBlockBytes;

        @ApiModelProperty(name = "refBlockHash", value = "引用区块哈希", required = false, dataType = "String")
        @JsonProperty("ref_block_hash")
        private String refBlockHash;

        @ApiModelProperty(name = "expiration", value = "过期时间", required = false, dataType = "Long")
        private long expiration;

        @ApiModelProperty(name = "timestamp", value = "时间戳", required = false, dataType = "Long")
        private long timestamp;

        @Data
        @ApiModel(value = "合约", description = "交易合约")
        public static class Contract {

            @ApiModelProperty(name = "parameter", value = "参数", required = false, dataType = "Parameter")
            private Parameter parameter;

            @ApiModelProperty(name = "type", value = "合约类型", required = false, dataType = "String")
            private String type;

            @Data
            @ApiModel(value = "参数", description = "合约参数")
            public static class Parameter {

                @ApiModelProperty(name = "value", value = "值", required = false, dataType = "Value")
                private Value value;

                @ApiModelProperty(name = "typeUrl", value = "类型URL", required = false, dataType = "String")
                @JsonProperty("type_url")
                private String typeUrl;

                @Data
                @ApiModel(value = "值", description = "参数值")
                public static class Value {

                    @ApiModelProperty(name = "amount", value = "金额", required = false, dataType = "Long")
                    private long amount;

                    @ApiModelProperty(name = "ownerAddress", value = "发送方地址", required = false, dataType = "String")
                    @JsonProperty("owner_address")
                    private String ownerAddress;

                    @ApiModelProperty(name = "toAddress", value = "接收方地址", required = false, dataType = "String")
                    @JsonProperty("to_address")
                    private String toAddress;
                }
            }
        }
    }
}