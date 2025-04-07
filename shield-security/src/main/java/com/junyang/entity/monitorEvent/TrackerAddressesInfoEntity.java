package com.junyang.entity.monitorEvent;

import java.util.List;

import lombok.Data;

@Data
public class TrackerAddressesInfoEntity {
	
    private String page;

    private String limit;

    private String totalPage;

    private List<AddressInfo> addressList;

    @Data
    public static class AddressInfo {

        private String address;

        private String eventCount;

        private String createTime;

    }

}
