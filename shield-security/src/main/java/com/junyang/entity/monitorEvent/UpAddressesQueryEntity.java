package com.junyang.entity.monitorEvent;

import java.util.List;

import lombok.Data;

@Data
public class UpAddressesQueryEntity {
	
	private String trackerId;
	
	private String action;
	
	private String chainShortName;
	
	private List<String> addresses;

}
