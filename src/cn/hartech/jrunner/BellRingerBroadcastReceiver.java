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
 * AlarmManager �� Android ϵͳ��װ�����ڹ��� RTC ��ģ�飬
 * RTC (Real Time Clock) ��һ��������Ӳ��ʱ�ӣ������� CPU ����ʱ�������У�
 * ��Ԥ���ʱ�䵽��ʱ��ͨ���жϻ��� CPU��
 * 
 * �����ʹ��Service����ѯִ�У�Service��Ҫһֱפ���ڴ��У�������ѯ��Ƶ���ĳ���û��Ҫ��ô����
 * 
 * 
 * ������ʾ�ˣ�
 * 	1�������ϵͳ����ע����������
 *  2����ν���ϵͳ�����¼��㲥��
 *  3���ֻ��ӿ��������ϵͳ�㲥����������һ����
 *  
 * 
 * �ֻ���ʱ���������ַ�ʽ��
 * 
 * 	1������һ��̨Service����Service�ں�̨ʹ��postDelay��Thread.sleep��ʽһֱѭ������
 *     ����������ʱ���޷���֤׼ȷ���綨ÿ����05��������
 *     ������������򣨴��ڴ棩Ӱ�쵽������Servie����Servieѭ������ͣ���ӳٵ�0x�������
 *     �����ҵ���������Service�߳�������
 *     ����Service��Ҫһֱפ���ڴ��У�������ѯ��Ƶ���ĳ���û��Ҫ��ô����
 *     
 *  2��ע�ᵽϵͳAlarmManager�����ɸ÷���ĳ��ʱ�������ҵ�����
 *     ���Ա�֤��ÿ����05������������ڼ���������������У���ʧ�ܵ���n���ӵ�05������
 *     ��������AlarmManager�߳���ִ��ҵ�����
 * 
 * @author jin.zheng
 * @date 2013-6-4
 *
 */
public class BellRingerBroadcastReceiver extends BroadcastReceiver {

	public static final String ACTION_ID = "android.alarm.cn.hartech.jrunner.bellringerservice";

	/**
	 * 1�������ϵͳ����ע����������
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

		// ����ʱ�䣨���ף�������ָ��1970�굽���ڵ�ʱ��
		// �������ʱ�䷢���ڹ�ȥ����ע������ʱ������һ��
		long triggerAtTime = (System.currentTimeMillis() / Constant.BELL_SERVICE_UPDATE)
				* Constant.BELL_SERVICE_UPDATE;

		// �������� ������Ϊ10������һ��
		int interval = Constant.BELL_SERVICE_UPDATE;

		// ע������ѭ������
		// RTC ��ָ����ʱ�̣����͹㲥�����������豸 
		// RTC_WAKEUP ��ָ����ʱ�̣����͹㲥���������豸 
		// ELAPSED_REALTIME ָ���������ϵͳ������ʱ�䣬���������Ǿ���ʱ��
		alarmManager.setRepeating(AlarmManager.RTC, triggerAtTime, interval,
				sender);

		// ���ֻ����һ��
		//		alarmManager.set(AlarmManager.RTC, triggerAtTime, sender);

	}

	/**
	 * ȡ��������
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

		// �������intentƥ�䣨filterEquals(intent)�������ӻᱻȡ��  
		alarmManager.cancel(sender);

	}

	/**
	 * 
	 * 2����ν���ϵͳ�����¼��㲥��
	 * 3���ֻ��ӿ��������ϵͳ�㲥����������һ����
	 * 
	 * ��XMLע����Ҫ�����Ĺ㲥��

	    <!-- ���ӵ��õķ��� �� �����Զ����÷��� ���ڴ��� -->
	    <receiver android:name="cn.hartech.jrunner.BellRingerBroadcastReceiver">
	        <intent-filter>
	            <action android:name="android.alarm.cn.hartech.jrunner.bellringerservice" />
	            <action android:name="android.intent.action.BOOT_COMPLETED" />
	        </intent-filter>
	    </receiver>
	    
	    <!-- ���Ȩ�� -->
	    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" /> 
	 * 
	 */
	public void onReceive(Context context, Intent intent) {

		// ���յ����ӵĶ�ʱ�㲥������Ӧҵ����
		if (ACTION_ID.equals(intent.getAction())) {

			Log.e("Receive", "xxxx");

			BellRingerEngine bellRingerEngine = new BellRingerEngine(context);

			// ����Ƿ����ܲ�
			if (!bellRingerEngine.checkHasRun()) {

				bellRingerEngine.fireNotification();

			}

		}

		// ϵͳ����ʱ
		else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

			MyProperties property = new MyProperties();

			boolean isServiceOn = Boolean.valueOf(property.getProperty(
					"settingIsServiceOn", "false"));

			if (isServiceOn) {

				// ����ʱ��������
				setAlarm(context);
			}

			// ϵͳ����ʱ�����ñ�����
			Log.e("BOOT", "xxxx");

		}
	}
}
