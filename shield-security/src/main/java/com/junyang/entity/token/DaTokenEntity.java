package com.junyang.entity.token;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@Document(collection = "tokens")
@ApiModel(value = "盾安token", description = "盾安token")
public class DaTokenEntity {

	private String id; // "_id" field
	private String address; // "address" field
	private String impl; // "impl" field
	private String chainId; // "chainId" field
	private int v; // "__v" field
	private boolean addToIndex; // "addToIndex" field
	private boolean checked; // "checked" field
	private String cmcId; // "cmcId" field (can be null)
	private String coingeckoId; // "coingeckoId" field
	private Date createdAt; // "createdAt" field
	private int decimals; // "decimals" field
	private boolean isNative; // "isNative" field
	private String logoURI; // "logoURI" field
	private long marketCap; // "marketCap" field (stored as numberLong in MongoDB)
	private String name; // "name" field
	private int riskLevel; // "riskLevel" field
	private boolean security; // "security" field
	private List<String> source; // "source" field (a list of strings)
	private String status; // "status" field
	private String symbol; // "symbol" field
	private Date updatedAt; // "updatedAt" field
	private boolean verified; // "verified" field

}
