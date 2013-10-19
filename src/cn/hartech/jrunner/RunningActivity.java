package cn.hartech.jrunner;

import java.text.DecimalFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import cn.hartech.jrunner.dao.GPSRecordDAO;
import cn.hartech.jrunner.dao.PlayingFieldDAO;
import cn.hartech.jrunner.dao.RunRecordDAO;
import cn.hartech.jrunner.engine.RunnerEngine;
import cn.hartech.jrunner.po.GPSRecord;
import cn.hartech.jrunner.po.RunRecord;
import cn.hartech.jrunner.ui.dialog.DialogStopRunning;
import cn.hartech.jrunner.util.Constant;
import cn.hartech.jrunner.util.MyUtility;

public class RunningActivity extends Activity {

	private RunnerEngine runnerEngine;

	private RunRecordDAO runRecordDAO;

	private GPSRecordDAO gpsRecordDAO;

	private PlayingFieldDAO playingFieldDAO;

	private TextView textViewSpeed;

	private TextView textViewDistance;

	private TextView textViewTime;

	private TextView textViewPlayingField;

	private Date startTime;

	private Handler handler = new Handler();

	// ����һ����ʱ����
	private Runnable runnable = new Runnable() {

		public void run() {

			RunningActivity.this.runTimeTask();

			handler.postDelayed(this, Constant.RUNNING_TIME_UPDATE);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_running);

		textViewSpeed = (TextView) findViewById(R.id.textViewSpeed);
		textViewDistance = (TextView) findViewById(R.id.textViewDistance);
		textViewTime = (TextView) findViewById(R.id.textViewTime);
		textViewPlayingField = (TextView) findViewById(R.id.textViewPlayingField);

		// ����ҳ��ʱ�����ܲ�����
		startRunnerEngine();

		// ������ʱ����
		handler.postDelayed(runnable, Constant.RUNNING_TIME_UPDATE);

	}

	// ��ʱ�����������ڸ���ʱ��
	protected void runTimeTask() {

		if (startTime == null) {
			startTime = new Date();
		}

		long second = (new Date().getTime() - startTime.getTime()) / 1000;

		long minute = second / 60;

		second = second % 60;

		String timeStr = "";

		if (minute < 10) {
			timeStr += "0" + minute;
		} else {
			timeStr += minute;
		}

		timeStr += ":";

		if (second < 10) {
			timeStr += "0" + second;
		} else {
			timeStr += second;
		}

		textViewTime.setText(timeStr);

	}

	// ����ҳ��ʱ�����ܲ�����
	private void startRunnerEngine() {

		// �������ݿ�
		runRecordDAO = new RunRecordDAO(this);
		gpsRecordDAO = new GPSRecordDAO(this);
		playingFieldDAO = new PlayingFieldDAO(this);

		// ����һ���ܲ���¼����
		runnerEngine = new RunnerEngine(this, runRecordDAO, gpsRecordDAO,
				playingFieldDAO);

		// ��ʼ����
		runnerEngine.startRecord();
	}

	// ҳ����Stop��ť�����¼�
	public void onClickStopRunning(View view) {

		DialogStopRunning newDialog = new DialogStopRunning(this);

		newDialog.show(getFragmentManager(), "dialog");
	}

	// ��ѡ�񱣴��˳�ʱ
	public void stopRunningAndSave() {

		runnerEngine.stopRecordAndSave();

		// ������ Activity.finish()����ʱ��������û����� BACK ��һ��
		finish();

	}

	// ��ѡ�񲻱����˳�ʱ
	public void stopRunningWithoutSave() {

		runnerEngine.stopRecordWithoutSave();

		// ������ Activity.finish()����ʱ��������û����� BACK ��һ��
		finish();

	}

	@Override
	protected void onDestroy() {

		// ȡ����ʱ����
		handler.removeCallbacks(runnable);

		super.onDestroy();
		if (runRecordDAO != null) {
			runRecordDAO.close();
		}

		if (gpsRecordDAO != null) {
			gpsRecordDAO.close();
		}

		if (playingFieldDAO != null) {
			playingFieldDAO.close();
		}
	}

	/**
	 * �����ֻ����˰�ť�������ǰ���ڼ�¼����ʾ�Ի���
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			DialogStopRunning newDialog = new DialogStopRunning(this);

			newDialog.show(getFragmentManager(), "dialog");

			return true;
		}
		return false;
	}

	/**
	 * �����ʵʱ��ӡ�����ܲ�����
	 * 
	 * @param gpsRecordList
	 */
	public void printRealTimeData(RunRecord currentRunRecord) {

		GPSRecord gpsRecord = currentRunRecord.gpsRecordList
				.get(currentRunRecord.gpsRecordList.size() - 1);

		textViewSpeed.setText(MyUtility.setScale(gpsRecord.speed, 1) + "");

		textViewDistance.setText(DF_0000.format(currentRunRecord.length));

		if (currentRunRecord.currentPlayingField != null) {
			textViewPlayingField
					.setText(currentRunRecord.currentPlayingField.name
							.substring(0, 2));
		}

	}

	public final static DecimalFormat DF_0000 = new DecimalFormat("0000");

	/**
	 * �������ʾ�޼�¼����
	 */
	public void printNoRecord() {

		Toast.makeText(this, "��GPS��¼������", Toast.LENGTH_SHORT).show();

	}

	/**
	 * �ڰ���Stop���������ӡ�����ܲ�������Ϣ
	 * 
	 * @param currentRunRecord
	 */
	public void printCurrentRunRecord(RunRecord currentRunRecord) {

		String log = "�����ܲ���·�̣�"
				+ MyUtility.setScale(currentRunRecord.length, 0) + "��";
		log += "\n��ʱ�䣺" + currentRunRecord.duration / 60 + "�� ";
		log += currentRunRecord.duration % 60 + "�� ";
		log += "\nƽ���ٶȣ�" + MyUtility.setScale(currentRunRecord.speedAvg, 2)
				+ "��/��";

		Toast.makeText(this, log, Toast.LENGTH_SHORT).show();

	}

	public RunnerEngine getRunnerEngine() {

		return runnerEngine;
	}

}
