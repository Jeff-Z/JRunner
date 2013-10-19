package cn.hartech.jrunner.engine;

import java.util.Date;
import java.util.List;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import cn.hartech.jrunner.RunningActivity;
import cn.hartech.jrunner.po.GPSRecord;
import cn.hartech.jrunner.po.PlayingField;
import cn.hartech.jrunner.po.RunRecord;
import cn.hartech.jrunner.util.Constant;

/**
 * ����ܲ����صĴ���Ҳȫ�ڴ����У�
 * 
 *  1, �ڱ����ʼ��ʱ���ó����г����б� 
 *  2, ��ÿ�θ���λ��ʱ��һ��һ�Σ��� 
 *    a, ÿ��������һ�µ�ǰλ�������г��صľ��룬ȡ������̵ģ�����̾���С��100�� 
 *    b, ���֮ǰȡ���ˣ������ܲ���������ټ�ⳡ��
 * 
 * 
 * @author jin.zheng
 * @date 2013-4-30
 * 
 */
public class GPSListenerRunnigLoocaion implements LocationListener {

	private RunnerEngine runnerEngine;

	private RunningActivity runningActivity;

	private List<PlayingField> playingFieldList;

	// ��һ����γ�ȣ����ڼ���λ��
	private double preLongitude = Double.MIN_VALUE;

	private double preLatitude;

	public GPSListenerRunnigLoocaion(RunnerEngine runnerEngine,
			RunningActivity runningActivity) {

		this.runnerEngine = runnerEngine;
		this.runningActivity = runningActivity;

		this.playingFieldList = runnerEngine.getPlayingFieldDAO().getAll();
	}

	/**
	 * ʵ��ʹ���У��������ø÷���
	 */
	@Override
	public void onLocationChanged(Location location) {

		GPSRecord gpsRecord = new GPSRecord();

		gpsRecord.time = new Date();
		gpsRecord.longitude = location.getLongitude();
		gpsRecord.latitude = location.getLatitude();
		gpsRecord.speed = location.getSpeed();
		gpsRecord.length = makeLenth(location);

		RunRecord currentRunRecord = runnerEngine.getCurrentRunRecord();
		gpsRecord.runRecord = currentRunRecord;

		currentRunRecord.length += gpsRecord.length;
		currentRunRecord.gpsRecordList.add(gpsRecord);

		// �������ĳ���
		detectPlayingField(currentRunRecord, gpsRecord);

		// �����ʵʱ���ռ�
		runningActivity.printRealTimeData(currentRunRecord);

	}

	// ��������ļ���ÿ������һ��
	private int count = 0;

	/**
	 * �������ĳ���
	 * 
	 * ��ÿ�θ���λ��ʱ��һ��һ�Σ�������һ�Σ�
	 *  
	 *    a, ÿ��������һ�µ�ǰλ�������г��صľ��룬ȡ������̵ģ�����̾���С��100�� 
	 *    b, ���֮ǰȡ���ˣ������ܲ���������ټ�ⳡ��
	 *    
	 * @param currentRunRecord
	 * @param gpsRecord
	 */
	private void detectPlayingField(RunRecord currentRunRecord,
			GPSRecord gpsRecord) {

		// ���֮ǰȡ���ˣ������ܲ���������ټ�ⳡ��
		if (currentRunRecord.currentPlayingField != null) {
			return;
		}

		if (playingFieldList == null || playingFieldList.size() == 0) {
			return;
		}

		count++;
		// ÿ��������һ��
		if (count % 5 != 1) {
			return;
		}

		float minDistance = Float.MAX_VALUE;
		float[] results = new float[1];
		PlayingField currentPlayingField = null;

		// ���һ�µ�ǰλ�������г��صľ��룬ȡ������̵�
		for (PlayingField playingField : playingFieldList) {

			Location.distanceBetween(gpsRecord.latitude, gpsRecord.longitude,
					playingField.latitude, playingField.longitude, results);

			if (results[0] < minDistance) {

				minDistance = results[0];
				currentPlayingField = playingField;
			}
		}

		// ����̾������С��100��
		if (minDistance > Constant.FIELD_MIN_DISTANCE) {
			return;
		}

		// �Ѹó��صǼǵ������ܲ���¼�У�*���Һ��涼���ټ�ⳡ����*
		currentRunRecord.currentPlayingField = currentPlayingField;
		currentRunRecord.playingFieldID = currentPlayingField._id;
	}

	// ���㱾�μ�¼������һ����¼���λ�ƣ���λΪ��
	private double makeLenth(Location location) {

		// ����ǵ�һ����㣬��λ�Ʒ���0
		if (preLongitude == Double.MIN_VALUE) {
			preLatitude = location.getLatitude();
			preLongitude = location.getLongitude();
			return 0.0;
		}

		float[] results = new float[1];

		Location.distanceBetween(preLatitude, preLongitude,
				location.getLatitude(), location.getLongitude(), results);

		preLatitude = location.getLatitude();
		preLongitude = location.getLongitude();

		return results[0];
	}

	/**
	 * ʵ��ʹ���У��Ἣ�ٴ���׽�� TEMPORARILY_UNAVAILABLE��AVAILABLE ״̬��
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	/**
	 * ������������ֻ�ϵͳ����->GPS����ҳ���У���GPSλ�÷�����Ϲ�ʱ����
	 */
	@Override
	public void onProviderEnabled(String provider) {

	}

	/**
	 * ������������ֻ�ϵͳ����->GPS����ҳ���У���GPSλ�÷���ȡ����ʱ����
	 */
	@Override
	public void onProviderDisabled(String provider) {

	}

}
