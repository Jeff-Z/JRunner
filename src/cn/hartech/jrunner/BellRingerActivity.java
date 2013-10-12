package cn.hartech.jrunner;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;
import cn.hartech.jrunner.util.MyProperties;

public class BellRingerActivity extends Activity {

	private MyProperties property;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bell_ringer);

		// 如果指定配置文件不存在，则创建一个新文件
		property = new MyProperties();

		initCheckBox();

		initSpinnerListeners();
	}

	// 启动检查服务的勾选框
	private void initCheckBox() {

		CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox_startService);

		boolean isServiceOn = Boolean.valueOf(property.getProperty(
				"settingIsServiceOn", "false"));

		checkBox.setChecked(isServiceOn);

		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				property.put("settingIsServiceOn", String.valueOf(isChecked));
				property.saveMyPropertyFile();

				if (isChecked) {

					// 注册闹钟，由闹钟调起检查服务
					BellRingerBroadcastReceiver
							.setAlarm(BellRingerActivity.this);

					Toast.makeText(BellRingerActivity.this,
							"Bell Ringer Started.", Toast.LENGTH_SHORT).show();

				} else {

					// 取消闹钟注册
					BellRingerBroadcastReceiver
							.cancelAlarm(BellRingerActivity.this);

					// 如果正有Notification显示，取消它
					BellRingerActivity
							.cancelNotification(BellRingerActivity.this);

					Toast.makeText(BellRingerActivity.this,
							"Bell Ringer Stoped.", Toast.LENGTH_SHORT).show();

				}
			}
		});
	}

	private void initSpinnerListeners() {

		// 检查周期
		initSpinner(R.id.spinner_period, "settingPeriod");

		// 周期内跑步次数
		initSpinner(R.id.spinner_times, "settingTimes");

		// 周期内跑步总程
		initSpinner(R.id.spinner_length, "settingLength");

		// 提醒方式
		initSpinner(R.id.spinner_ring_type, "settingRingType");

	}

	/**
	 * 初始化一个Spinner
	 *   1，从配置文件读取配置信息
	 *   2，配置监听代码
	 *   3，当用户选择时，保存到配置文件里
	 * 
	 * @param spinnerID  Spinner的R.ID
	 * @param settingName 该下拉菜单在配置文件的KEY
	 */
	private void initSpinner(int spinnerID, final String settingName) {

		Spinner spinner = (Spinner) findViewById(spinnerID);

		// 从配置文件读取保存的配置，并初始化下拉菜单的位置
		int position = Integer.parseInt(property.getProperty(settingName, "0"));
		spinner.setSelection(position);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				property.put(settingName, String.valueOf(position));
				property.saveMyPropertyFile();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	// 作为提醒的唯一标识
	// 如果重复使用同一个ID显示提醒，则新提醒会替换未消失的老提醒
	private static final int MyNotificationID = R.id.checkBox_startService;

	@SuppressWarnings("deprecation")
	public static void trigerNotification(Context context, String title,
			String content) {

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// 设置  图标，在状态栏滚动的文字，什么时间开始显示
		Notification notification = new Notification(R.drawable.ic_launcher,
				title, System.currentTimeMillis());

		// 设置点击提示后显示的Activity
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, RunningRecordsActivity.class),
				PendingIntent.FLAG_CANCEL_CURRENT);

		// 在状态栏显示的大标题，小标题（两者同时显示，一个在上方大字体，一个在其下一行小字体）
		notification.setLatestEventInfo(context, title, content, contentIntent);

		// 点击通知后通知自动消失
		// 如果要一直在，使用FLAG_NO_CLEAR
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// 调用默认的短信声音通知（默认如果有震动，则也震动）
		// notification.defaults = Notification.DEFAULT_ALL;

		// 显示该Notification，指定ID，可以用于取消该Notification
		notificationManager.notify(MyNotificationID, notification);
	}

	/**
	 * 用于在外部取消该通知（如果该通知正在显示，则会消失）
	 * 
	 * @param context
	 */
	public static void cancelNotification(Context context) {

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// 可以使用以下方式取消Notification
		notificationManager.cancel(MyNotificationID);

	}
}
