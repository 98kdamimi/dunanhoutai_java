package com.junyang.entity.response;

import lombok.Data;

/**
 * @category 账号统计数量
 * @author Hlin
 *
 */
@Data
public class AccountStatisticsEntity {
	
	private long countToday = 0;
	
	private long countWeek = 0;
	
	private long countMonth = 0;
	
	private long countYear = 0;
	
	private long countAll = 0;
	

}
