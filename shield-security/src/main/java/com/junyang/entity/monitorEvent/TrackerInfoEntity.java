package com.junyang.entity.monitorEvent;

import java.util.List;

import lombok.Data;

@Data
public class TrackerInfoEntity {
	

    private String page;
    private String limit;
    private String totalPage;
    private List<Tracker> trackerList;

    @Data
    public static class Tracker {
        private String event;
        private String trackerId;
        private String chainShortName;
        private String webhookUrl;
        private String trackerName;
        private String updateTime;
        private String isActive;
    }

}
