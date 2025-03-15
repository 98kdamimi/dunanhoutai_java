package com.junyang.entity.release;

import lombok.Data;

/**
 * @category 硬件版本发布参数
 * @author Hlin
 *
 */
@Data
public class HardwareAddQueryEntity {
	
	private String hardwareVersion;
	
	private String bootloaderUrl;
	
	private String firmwareUrl;

}
