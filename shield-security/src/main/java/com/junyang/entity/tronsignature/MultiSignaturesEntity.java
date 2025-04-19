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

    @ApiModelProperty(name = "amount", value = "数量", dataType = "double")
    @Field("amount")
    private double amount;

    @ApiModelProperty(name = "signdata", value = "签名数据", dataType = "Object")
    @Field("signdata")
    private Object signdata;

    @ApiModelProperty(name = "lastsigndata", value = "签名数据", dataType = "Object")
    @Field("lastsigndata")
    private Object lastsigndata;

    @ApiModelProperty(name = "threshold", value = "交易阈值", dataType = "int")
    @Field("threshold")
    private int threshold;

    @ApiModelProperty(name = "tokeninfo", value = "代币详情", required = false, dataType = "Object")
    @Field("tokeninfo")
    private Object tokeninfo;

    @ApiModelProperty(name = "signatureProgress", value = "签名进度列表",required = false)
    @Field("signatureProgress")
    private List<SignatureProgress> signatureProgress;

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

        @Field("signdata")
        private Object signdata;
    }
}
