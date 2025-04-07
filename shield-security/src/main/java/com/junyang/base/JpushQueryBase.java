package com.junyang.base;

import java.util.List;

import lombok.Data;

@Data
public class JpushQueryBase {
	
	private String title;
	
	private String content;
	
	private List<String> registrationId;

}
