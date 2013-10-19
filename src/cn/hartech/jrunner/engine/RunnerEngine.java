package cn.hartech.jrunner.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.LocationManager;
import cn.hartech.jrunner.RunningActivity;
import cn.hartech.jrunner.dao.GPSRecordDAO;
import cn.hartech.jrunner.dao.PlayingFieldDAO;
import cn.hartech.jrunner.dao.RunRecordDAO;
import cn.hartech.jrunner.po.GPSRecord;
import cn.hartech.jrunner.po.PlayingField;
import cn.hartech.jrunner.po.RunRecord;

/**
 * 一个跑步记录器，在这里做各种逻辑计算
 * 
 * @author Jacky Date:2013-4-6
 * 
 */
@SuppressWarnings("unused")
public class RunnerEngine {

	private RunningActivity runningActivity;

	private LocationManager locationManager;

	private GPSListenerRunnigLoocaion gpsListener;

	private RunRecordDAO runRecordDAO;

	private GPSRecordDAO gpsRecordDAO;

	private PlayingFieldDAO playingFieldDAO;

	// 标识当前是否在跑步计时
	private boolean isRunning = false;

	// 当前使用的跑步记录
	private RunRecord currentRunRecord;

	public RunnerEngine(RunningActivity runningActivity,
			RunRecordDAO runRecordDAO, GPSRecordDAO gpsRecordDAO,
			PlayingFieldDAO playingFieldDAO) {

		this.runningActivity = runningActivity;
		this.runRecordDAO = runRecordDAO;
		this.gpsRecordDAO = gpsRecordDAO;
		this.playingFieldDAO = playingFieldDAO;

		locationManager = (LocationManager) (runningActivity
				.getSystemService(Context.LOCATION_SERVICE));
		gpsListener = new GPSListenerRunnigLoocaion(this, runningActivity);
	}

	/**
	 * 开始记录，保存到内存
	 * 
	 * 这里仅开始记录到内存，stopRecord时才保存到数据库
	 * 
	 */
	public void startRecord() {

		isRunning = true;

		currentRunRecord = new RunRecord();
		currentRunRecord.gpsRecordList = new ArrayList<GPSRecord>();

		// 开始监听
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, gpsListener);

	}

	/**
	 * 停止记录，并保存到数据库
	 */
	public void stopRecordAndSave() {

		isRunning = false;

		// 取消注册监听
		locationManager.removeUpdates(gpsListener);

		List<GPSRecord> gpsRecordList = currentRunRecord.gpsRecordList;

		if (gpsRecordList == null || gpsRecordList.size() == 0) {

			runningActivity.printNoRecord();

			return;
		}

		// 根据GPS详细列表对本次跑步进行各种运算：起始结束时间，持续时间，总长度，平均速度等
		makeRunReocrd(gpsRecordList);

		// 把本次跑步记录 添加 到检测到的场地（如果有的话）的场地跑步总长度等信息
		makePlayingField();

		// 把本次跑步记录打印到界面上
		runningActivity.printCurrentRunRecord(currentRunRecord);

		// 把本次跑步记录保存到数据库中
		saveCurrentRunRecord();

	}

	// 把本次跑步记录 添加 到检测到的场地（如果有的话）的场地跑步总长度等信息
	private void makePlayingField() {

		PlayingField playingField = currentRunRecord.currentPlayingField;

		if (playingField == null) {
			return;
		}

		playingField.runCount++;
		playingField.lengthSum += currentRunRecord.length;
		playingField.lastRunDate = currentRunRecord.startTime;

	}

	/**
	 * 用于用户按手机返回按钮时触发，释放当前监听器与各种资源
	 */
	public void stopRecordWithoutSave() {

		isRunning = false;

		// 取消注册监听
		locationManager.removeUpdates(gpsListener);

		currentRunRecord.gpsRecordList = null;
		currentRunRecord = null;

	}

	// 把本次跑步记录保存到数据库中
	// 使用数据库事务机制可以大大加快批量插入数据速度
	private void saveCurrentRunRecord() {

		// 保存RunRecord
		int runRecordID = runRecordDAO.add(currentRunRecord);

		// 保存PlayingField
		if (currentRunRecord.currentPlayingField != null) {
			playingFieldDAO.update(currentRunRecord.currentPlayingField);
		}

		// 开始保存GPSRecord列表

		// 使用数据库事务机制可以大大加快批量插入数据速度

		// 由于每次保存的GPS数据非常大，而且好像这个"大数据"貌似没什么用，所以不保存了
		//		gpsRecordDAO.getDatabase().beginTransaction();
		//
		//		List<GPSRecord> gpsRecordList = currentRunRecord.gpsRecordList;
		//
		//		// 保存每个GPSRecord
		//		for (GPSRecord gpsRecord : gpsRecordList) {
		//
		//			gpsRecord.runRecordID = runRecordID;
		//
		//			gpsRecordDAO.add(gpsRecord);
		//
		//		}
		//
		//		gpsRecordDAO.getDatabase().setTransactionSuccessful();
		//		gpsRecordDAO.getDatabase().endTransaction();

	}

	/**
	 * 根据GPS详细列表对本次跑步进行各种运算：起始结束时间，持续时间，总长度，平均速度等
	 */
	private void makeRunReocrd(List<GPSRecord> gpsRecordList) {

		// 获取开始时间，结束时间
		currentRunRecord.startTime = gpsRecordList.get(0).time;
		currentRunRecord.endTime = gpsRecordList.get(gpsRecordList.size() - 1).time;
		currentRunRecord.gpsRecordCount = gpsRecordList.size();

		currentRunRecord.duration = (int) ((currentRunRecord.endTime.getTime() - currentRunRecord.startTime
				.getTime()) / 1000);

		currentRunRecord.speedAvg = currentRunRecord.length
				/ currentRunRecord.duration;
	}

	public RunRecord getCurrentRunRecord() {

		return currentRunRecord;
	}

	public boolean isRunning() {

		return isRunning;
	}

	public PlayingFieldDAO getPlayingFieldDAO() {
		return playingFieldDAO;
	}

}
