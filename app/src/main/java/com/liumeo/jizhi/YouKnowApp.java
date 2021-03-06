package com.liumeo.jizhi;

import android.app.Application;
import android.app.Notification;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

public class YouKnowApp extends Application
{
	@Override
	public void onCreate()
	{
		super.onCreate();
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
		builder.statusBarDrawable = R.drawable.jpush_notification_icon;
		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
				| Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
		builder.notificationDefaults = Notification.DEFAULT_SOUND
				| Notification.DEFAULT_VIBRATE
				| Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
		JPushInterface.setPushNotificationBuilder(1, builder);
	}
}
