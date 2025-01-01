package com.junyang.entity.dapp;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import lombok.Data;

@Data
public class RpcDappEntity {
	
	private String dappId;
	
	private List<String> categories;
	
	private List<String> chains;
	
	private String description;
	
	private String fullDescription;
	
	private String link;
	
	private String logo;
	
	private JSONObject metrics;
	
	private String name;
	
	private JSONArray socialLinks;
	
	private List<RpcDappTagsEntity> tags;
	
	private String website;

}
