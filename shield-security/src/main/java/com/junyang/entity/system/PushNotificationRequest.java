package com.junyang.entity.system;

import java.util.List;

import lombok.Data;

@Data
public class PushNotificationRequest {
	private String platform;
    private Audience audience;
    private Notification notification;
    private Options options;

    // Inner class for Audience
    @Data
    public static class Audience {
        private List<String> registration_id;
    }

    // Inner class for Notification
    @Data
    public static class Notification {
        private String alert;
    }

    // Inner class for Options
    @Data
    public static class Options {
        private int time_to_live;
        private boolean apns_production;
    }
}
