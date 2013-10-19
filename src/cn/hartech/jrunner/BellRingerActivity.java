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

		// ���ָ�������ļ������ڣ��򴴽�һ�����ļ�
		property = new MyProperties();

		initCheckBox();

		initSpinnerListeners();
	}

	// ����������Ĺ�ѡ��
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

					// ע�����ӣ������ӵ��������
					BellRingerBroadcastReceiver
							.setAlarm(BellRingerActivity.this);

					Toast.makeText(BellRingerActivity.this,
							"Bell Ringer Started.", Toast.LENGTH_SHORT).show();

				} else {

					// ȡ������ע��
					BellRingerBroadcastReceiver
							.cancelAlarm(BellRingerActivity.this);

					// �������Notification��ʾ��ȡ����
					BellRingerActivity
							.cancelNotification(BellRingerActivity.this);

					Toast.makeText(BellRingerActivity.this,
							"Bell Ringer Stoped.", Toast.LENGTH_SHORT).show();

				}
			}
		});
	}

	private void initSpinnerListeners() {

		// �������
		initSpinner(R.id.spinner_period, "settingPeriod");

		// �������ܲ�����
		initSpinner(R.id.spinner_times, "settingTimes");

		// �������ܲ��ܳ�
		initSpinner(R.id.spinner_length, "settingLength");

		// ���ѷ�ʽ
		initSpinner(R.id.spinner_ring_type, "settingRingType");

	}

	/**
	 * ��ʼ��һ��Spinner
	 *   1���������ļ���ȡ������Ϣ
	 *   2�����ü�������
	 *   3�����û�ѡ��ʱ�����浽�����ļ���
	 * 
	 * @param spinnerID  Spinner��R.ID
	 * @param settingName �������˵��������ļ���KEY
	 */
	private void initSpinner(int spinnerID, final String settingName) {

		Spinner spinner = (Spinner) findViewById(spinnerID);

		// �������ļ���ȡ��������ã�����ʼ�������˵���λ��
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

	// ��Ϊ���ѵ�Ψһ��ʶ
	// ����ظ�ʹ��ͬһ��ID��ʾ���ѣ��������ѻ��滻δ��ʧ��������
	private static final int MyNotificationID = R.id.checkBox_startService;

	@SuppressWarnings("deprecation")
	public static void trigerNotification(Context context, String title,
			String content) {

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// ����  ͼ�꣬��״̬�����������֣�ʲôʱ�俪ʼ��ʾ
		Notification notification = new Notification(R.drawable.ic_launcher,
				title, System.currentTimeMillis());

		// ���õ����ʾ����ʾ��Activity
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, RunningRecordsActivity.class),
				PendingIntent.FLAG_CANCEL_CURRENT);

		// ��״̬����ʾ�Ĵ���⣬С���⣨����ͬʱ��ʾ��һ�����Ϸ������壬һ��������һ��С���壩
		notification.setLatestEventInfo(context, title, content, contentIntent);

		// ���֪ͨ��֪ͨ�Զ���ʧ
		// ���Ҫһֱ�ڣ�ʹ��FLAG_NO_CLEAR
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// ����Ĭ�ϵĶ�������֪ͨ��Ĭ��������𶯣���Ҳ�𶯣�
		// notification.defaults = Notification.DEFAULT_ALL;

		// ��ʾ��Notification��ָ��ID����������ȡ����Notification
		notificationManager.notify(MyNotificationID, notification);
	}

	/**
	 * �������ⲿȡ����֪ͨ�������֪ͨ������ʾ�������ʧ��
	 * 
	 * @param context
	 */
	public static void cancelNotification(Context context) {

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// ����ʹ�����·�ʽȡ��Notification
		notificationManager.cancel(MyNotificationID);

	}
}
