package com.junyang.scheduled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.junyang.service.impl.DappServiceImpl;

@Component
public class ScheduledTasks {
	
	@Autowired
	private DappServiceImpl dappServiceImpl;
	
//	@Scheduled(cron = "0/10 * * * * *") 
	public void rpcDappTop() {
	    try {
	    	
	        dappServiceImpl.rpcTop();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
//	@Scheduled(cron = "0/20 * * * * *") 
	public void rpcDappList() {
		dappServiceImpl.rpcDapp();
	}
	
	
	
	
	
}
