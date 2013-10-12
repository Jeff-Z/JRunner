package cn.hartech.jrunner;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import cn.hartech.jrunner.engine.BellRingerEngine;
import cn.hartech.jrunner.util.Constant;
import cn.hartech.jrunner.util.MyProperties;

/**
 * AlarmManager 是 Android 系统封装的用于管理 RTC 的模块，
 * RTC (Real Time Clock) 是一个独立的硬件时钟，可以在 CPU 休眠时正常运行，
 * 在预设的时间到达时，通过中断唤醒 CPU。
 * 
 * 而如果使用Service做轮询执行，Service需要一直驻在内存中，对于轮询不频繁的程序，没必要这么做。
 * 
 * 
 * 本类演示了：
 * 	1，如何向系统闹钟注册提醒任务。
 *  2，如何接收系统闹铃事件广播。
 *  3，手机从开机后接收系统广播，并启动第一条。
 *  
 * 
 * 手机轮时任务有两种方式：
 * 
 * 	1，启动一后台Service，该Service在后台使用postDelay或Thread.sleep方式一直循环运行
 *     这里问题是时间无法保证准确，如定每分钟05秒启动，
 *     如果被其他程序（大内存）影响到重启本Servie，本Servie循环会暂停，延迟到0x秒才运行
 *     这里的业务代码是在Service线程中运行
 *     而且Service需要一直驻在内存中，对于轮询不频繁的程序，没必要这么做。
 *     
 *  2，注册到系统AlarmManager服务，由该服务到某个时间点运行业务代码
 *     可以保证在每分钟05秒启动，如果期间有其他大程序运行，则失败到下n分钟的05秒启动
 *     这里是在AlarmManager线程中执行业务代码
 * 
 * @author jin.zheng
 * @date 2013-6-4
 *
 */
public class BellRingerBroadcastReceiver extends BroadcastReceiver {

	public static final String ACTION_ID = "android.alarm.cn.hartech.jrunner.bellringerservice";

	/**
	 * 1，如何向系统闹钟注册提醒任务。
	 * 
	 * @param context
	 * @param timeInMillis
	 */
	public static void setAlarm(Context context) {

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		/**
			<intent-filter>
		        <action android:name="android.alarm.cn.hartech.jrunner.bellringerservice" />
		        <action android:name="android.intent.action.BOOT_COMPLETED" />
		    </intent-filter>
		 */
		Intent intent = new Intent(ACTION_ID);

		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		// 启动时间（毫米），这是指自1970年到现在的时间
		// 如果这是时间发生在过去，则注册闹钟时即启动一次
		long triggerAtTime = (System.currentTimeMillis() / Constant.BELL_SERVICE_UPDATE)
				* Constant.BELL_SERVICE_UPDATE;

		// 闹铃间隔， 这里设为10分钟闹一次
		int interval = Constant.BELL_SERVICE_UPDATE;

		// 注册闹钟循环调用
		// RTC 在指定的时刻，发送广播，但不唤醒设备 
		// RTC_WAKEUP 在指定的时刻，发送广播，并唤醒设备 
		// ELAPSED_REALTIME 指的是相对于系统启动的时间，上面两个是绝对时间
		alarmManager.setRepeating(AlarmManager.RTC, triggerAtTime, interval,
				sender);

		// 如果只闹钟一次
		//		alarmManager.set(AlarmManager.RTC, triggerAtTime, sender);

	}

	/**
	 * 取消该闹钟
	 * 
	 * @param context
	 * 
	 */
	public static void cancelAlarm(Context context) {

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		Intent intent = new Intent(ACTION_ID);

		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		// 与上面的intent匹配（filterEquals(intent)）的闹钟会被取消  
		alarmManager.cancel(sender);

	}

	/**
	 * 
	 * 2，如何接收系统闹铃事件广播。
	 * 3，手机从开机后接收系统广播，并启动第一条。
	 * 
	 * 在XML注册需要监听的广播：

	    <!-- 闹钟调用的服务 与 开机自动调用服务 都在此类 -->
	    <receiver android:name="cn.hartech.jrunner.BellRingerBroadcastReceiver">
	        <intent-filter>
	            <action android:name="android.alarm.cn.hartech.jrunner.bellringerservice" />
	            <action android:name="android.intent.action.BOOT_COMPLETED" />
	        </intent-filter>
	    </receiver>
	    
	    <!-- 添加权限 -->
	    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" /> 
	 * 
	 */
	public void onReceive(Context context, Intent intent) {

		// 接收到闹钟的定时广播，做相应业务动作
		if (ACTION_ID.equals(intent.getAction())) {

			Log.e("Receive", "xxxx");

			BellRingerEngine bellRingerEngine = new BellRingerEngine(context);

			// 检查是否有跑步
			if (!bellRingerEngine.checkHasRun()) {

				bellRingerEngine.fireNotification();

			}

		}

		// 系统启动时
		else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

			MyProperties property = new MyProperties();

			boolean isServiceOn = Boolean.valueOf(property.getProperty(
					"settingIsServiceOn", "false"));

			if (isServiceOn) {

				// 开机时启动闹钟
				setAlarm(context);
			}

			// 系统启动时将调用本方法
			Log.e("BOOT", "xxxx");

		}
	}
}
