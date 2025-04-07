package com.junyang.entity.instance;

import lombok.Data;

@Data
public class NotificationConfigEntity {

	private int threshold;
	private boolean pushEnable;
	private boolean priceAlertEnable;
	private boolean btcAndEthPriceAlertEnable;
	private boolean favoriteTokensPriceAlertEnable;
	private boolean accountActivityPushEnable;
	private String registrationId;
	private String locale;
	private String currency;
	private String instanceId;

}
