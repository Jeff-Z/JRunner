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
 * һ���ܲ���¼�����������������߼�����
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

	// ��ʶ��ǰ�Ƿ����ܲ���ʱ
	private boolean isRunning = false;

	// ��ǰʹ�õ��ܲ���¼
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
	 * ��ʼ��¼�����浽�ڴ�
	 * 
	 * �������ʼ��¼���ڴ棬stopRecordʱ�ű��浽���ݿ�
	 * 
	 */
	public void startRecord() {

		isRunning = true;

		currentRunRecord = new RunRecord();
		currentRunRecord.gpsRecordList = new ArrayList<GPSRecord>();

		// ��ʼ����
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, gpsListener);

	}

	/**
	 * ֹͣ��¼�������浽���ݿ�
	 */
	public void stopRecordAndSave() {

		isRunning = false;

		// ȡ��ע�����
		locationManager.removeUpdates(gpsListener);

		List<GPSRecord> gpsRecordList = currentRunRecord.gpsRecordList;

		if (gpsRecordList == null || gpsRecordList.size() == 0) {

			runningActivity.printNoRecord();

			return;
		}

		// ����GPS��ϸ�б�Ա����ܲ����и������㣺��ʼ����ʱ�䣬����ʱ�䣬�ܳ��ȣ�ƽ���ٶȵ�
		makeRunReocrd(gpsRecordList);

		// �ѱ����ܲ���¼ ��� ����⵽�ĳ��أ�����еĻ����ĳ����ܲ��ܳ��ȵ���Ϣ
		makePlayingField();

		// �ѱ����ܲ���¼��ӡ��������
		runningActivity.printCurrentRunRecord(currentRunRecord);

		// �ѱ����ܲ���¼���浽���ݿ���
		saveCurrentRunRecord();

	}

	// �ѱ����ܲ���¼ ��� ����⵽�ĳ��أ�����еĻ����ĳ����ܲ��ܳ��ȵ���Ϣ
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
	 * �����û����ֻ����ذ�ťʱ�������ͷŵ�ǰ�������������Դ
	 */
	public void stopRecordWithoutSave() {

		isRunning = false;

		// ȡ��ע�����
		locationManager.removeUpdates(gpsListener);

		currentRunRecord.gpsRecordList = null;
		currentRunRecord = null;

	}

	// �ѱ����ܲ���¼���浽���ݿ���
	// ʹ�����ݿ�������ƿ��Դ��ӿ��������������ٶ�
	private void saveCurrentRunRecord() {

		// ����RunRecord
		int runRecordID = runRecordDAO.add(currentRunRecord);

		// ����PlayingField
		if (currentRunRecord.currentPlayingField != null) {
			playingFieldDAO.update(currentRunRecord.currentPlayingField);
		}

		// ��ʼ����GPSRecord�б�

		// ʹ�����ݿ�������ƿ��Դ��ӿ��������������ٶ�

		// ����ÿ�α����GPS���ݷǳ��󣬶��Һ������"������"ò��ûʲô�ã����Բ�������
		//		gpsRecordDAO.getDatabase().beginTransaction();
		//
		//		List<GPSRecord> gpsRecordList = currentRunRecord.gpsRecordList;
		//
		//		// ����ÿ��GPSRecord
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
	 * ����GPS��ϸ�б�Ա����ܲ����и������㣺��ʼ����ʱ�䣬����ʱ�䣬�ܳ��ȣ�ƽ���ٶȵ�
	 */
	private void makeRunReocrd(List<GPSRecord> gpsRecordList) {

		// ��ȡ��ʼʱ�䣬����ʱ��
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
