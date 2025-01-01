package com.junyang.entity.response;

import java.util.List;

import com.junyang.entity.dapp.RpcDappEntity;

import lombok.Data;

@Data
public class DappResEntity {

	private String range;
	
	private String success;
	
	private String top;
	
	private List<RpcDappEntity> results;
	
	
}
