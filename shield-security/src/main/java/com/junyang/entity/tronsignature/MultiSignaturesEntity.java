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

    @ApiModelProperty(name = "txID", value = "交易ID", dataType = "String")
    @Field("txID")
    private String txID;

    @ApiModelProperty(name = "fromAddress", value = "发送地址",  required = false, dataType = "String")
    @Field("fromAddress")
    private String fromAddress;

    @ApiModelProperty(name = "nowAddress", value = "当前地址", dataType = "String")
    @Field("nowAddress")
    private String nowAddress;

    @ApiModelProperty(name = "toAddress", value = "接收地址", dataType = "String")
    @Field("toAddress")
    private String toAddress;

    @ApiModelProperty(name = "amount", value = "数量", dataType = "int")
    @Field("amount")
    private int amount;

    @ApiModelProperty(name = "signdata", value = "签名数据", dataType = "Object")
    @Field("signdata")
    private Object signdata;

    @ApiModelProperty(name = "signatureProgress", value = "当前地址", required = false, dataType = "<SignatureProgress>")
    @Field("signatureProgress")
    private Object signatureProgress; // 签名进度列表
    /**
     * 签名进度内部类
     */
    @Data
    public static class SignatureProgress {
        @Field("address")
        private String address; // 签名地址

        @Field("isSign")
        private int isSign; // 是否已签名（1=是，0=否）

        @Field("signTime")
        private long signTime; // 签名时间戳

        @Field("weight")
        private int weight; // 签名权重
    }
}
