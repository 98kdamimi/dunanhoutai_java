package com.junyang.utils;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.push.model.notification.PlatformNotification;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JPushUtils {

    private static JPushClient jPushClient;

    
    private static String APP_KEY = "3bcc3c92a3b3c901829e73ed";
    private static String MASTER_SECRET = "41fb303e1104373acefa97db";

    public JPushUtils() {
        // 初始化 JPushClient
        if (jPushClient == null) {
            jPushClient = new JPushClient(MASTER_SECRET, APP_KEY);
        }
    }

    /**
     * 根据 registrationIds 推送通知
     *
     * @param registrationIds 目标设备的 registrationId 列表
     * @param title           推送通知的标题
     * @param content         推送通知的内容
     * @return 推送结果
     */
    public static PushResult sendPushNotification(List<String> registrationIds, String title, String content) {
        PushPayload payload = buildPushPayload(registrationIds, title, content);
        try {
            // 发送推送
            return jPushClient.sendPush(payload);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 构建推送 payload
     *
     * @param registrationIds 目标设备的 registrationId 列表
     * @param title           推送通知的标题
     * @param content         推送通知的内容
     * @return PushPayload
     */
    private static PushPayload buildPushPayload(List<String> registrationIds, String title, String content) {
        // 构建推送消息
        return PushPayload.newBuilder()
                .setPlatform(Platform.all()) // 推送到所有平台
                .setAudience(Audience.registrationId(registrationIds)) // 指定注册 ID
                .setNotification(
                        Notification.newBuilder()
                                .setAlert(content) // 推送内容
                                .addPlatformNotification(
                                        AndroidNotification.newBuilder() // 针对 Android 平台的通知
                                                .setAlert(content) // 设置推送内容
                                                .addExtra("title", title) // 添加额外参数
                                                .build()
                                )
                                .addPlatformNotification(
                                        IosNotification.newBuilder() // 针对 iOS 平台的通知
                                                .setAlert(content) // 设置推送内容
                                                .addExtra("title", title) // 添加额外参数
                                                .build()
                                )
                                .build()
                )
                .setMessage(Message.newBuilder()
                        .setMsgContent(content) // 设置自定义消息内容
                        .addExtra("title", title) // 添加额外参数
                        .build()) // 自定义消息
                .build();
    }


    /**
     * 构建推送 Payload (仅限 Android 平台)
     *
     * @param registrationIds 目标设备的 registrationId 列表
     * @param title           推送通知的标题
     * @param content         推送通知的内容
     * @return PushPayload
     */
    public static PushPayload buildAndroidPushPayload(List<String> registrationIds, String title, String content) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android()) // 仅推送到 Android 平台
                .setAudience(Audience.registrationId(registrationIds)) // 指定 registrationId
                .setNotification(
                        Notification.android(content, title, null)
                )
                .build();
    }

    /**
     * 构建推送 Payload (仅限 iOS 平台)
     *
     * @param registrationIds 目标设备的 registrationId 列表
     * @param title           推送通知的标题
     * @param content         推送通知的内容
     * @return PushPayload
     */
    public static PushPayload buildIOSPushPayload(List<String> registrationIds, String title, String content) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios()) // 仅推送到 iOS 平台
                .setAudience(Audience.registrationId(registrationIds)) // 指定 registrationId
                .setNotification(
                        Notification.ios(content, null) // iOS 推送内容
                )
                .build();
    }
}
