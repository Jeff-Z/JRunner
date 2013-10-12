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

	// 定义一个定时任务
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

		// 启动页面时启动跑步引擎
		startRunnerEngine();

		// 启动定时调度
		handler.postDelayed(runnable, Constant.RUNNING_TIME_UPDATE);

	}

	// 定时任务，这里用于更新时间
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

	// 启动页面时启动跑步引擎
	private void startRunnerEngine() {

		// 启动数据库
		runRecordDAO = new RunRecordDAO(this);
		gpsRecordDAO = new GPSRecordDAO(this);
		playingFieldDAO = new PlayingFieldDAO(this);

		// 启动一个跑步记录引擎
		runnerEngine = new RunnerEngine(this, runRecordDAO, gpsRecordDAO,
				playingFieldDAO);

		// 开始计数
		runnerEngine.startRecord();
	}

	// 页面点击Stop按钮激发事件
	public void onClickStopRunning(View view) {

		DialogStopRunning newDialog = new DialogStopRunning(this);

		newDialog.show(getFragmentManager(), "dialog");
	}

	// 并选择保存退出时
	public void stopRunningAndSave() {

		runnerEngine.stopRecordAndSave();

		// 当调用 Activity.finish()方法时，结果和用户按下 BACK 键一样
		finish();

	}

	// 并选择不保存退出时
	public void stopRunningWithoutSave() {

		runnerEngine.stopRecordWithoutSave();

		// 当调用 Activity.finish()方法时，结果和用户按下 BACK 键一样
		finish();

	}

	@Override
	protected void onDestroy() {

		// 取消定时任务
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
	 * 监听手机后退按钮，如果当前是在记录，提示对话后
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
	 * 向界面实时打印各种跑步数据
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
	 * 向界面显示无记录产生
	 */
	public void printNoRecord() {

		Toast.makeText(this, "无GPS记录产生。", Toast.LENGTH_SHORT).show();

	}

	/**
	 * 在按下Stop后往界面打印本次跑步整体信息
	 * 
	 * @param currentRunRecord
	 */
	public void printCurrentRunRecord(RunRecord currentRunRecord) {

		String log = "本次跑步总路程："
				+ MyUtility.setScale(currentRunRecord.length, 0) + "米";
		log += "\n总时间：" + currentRunRecord.duration / 60 + "分 ";
		log += currentRunRecord.duration % 60 + "秒 ";
		log += "\n平均速度：" + MyUtility.setScale(currentRunRecord.speedAvg, 2)
				+ "米/秒";

		Toast.makeText(this, log, Toast.LENGTH_SHORT).show();

	}

	public RunnerEngine getRunnerEngine() {

		return runnerEngine;
	}

}
