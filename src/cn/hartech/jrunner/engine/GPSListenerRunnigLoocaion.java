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
 * 检测跑步场地的处理也全在此类中：
 * 
 *  1, 在本类初始化时即拿出所有场地列表 
 *  2, 在每次更新位置时（一秒一次）： 
 *    a, 每隔五秒检测一下当前位置与所有场地的距离，取里面最短的，且最短距离小于100米 
 *    b, 如果之前取到了，本次跑步后面均不再检测场地
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

	// 上一个经纬度，用于计算位移
	private double preLongitude = Double.MIN_VALUE;

	private double preLatitude;

	public GPSListenerRunnigLoocaion(RunnerEngine runnerEngine,
			RunningActivity runningActivity) {

		this.runnerEngine = runnerEngine;
		this.runningActivity = runningActivity;

		this.playingFieldList = runnerEngine.getPlayingFieldDAO().getAll();
	}

	/**
	 * 实际使用中，经常调用该方法
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

		// 检测最靠近的场地
		detectPlayingField(currentRunRecord, gpsRecord);

		// 向界面实时打日记
		runningActivity.printRealTimeData(currentRunRecord);

	}

	// 用于下面的计数每五秒检测一次
	private int count = 0;

	/**
	 * 检测最靠近的场地
	 * 
	 * 在每次更新位置时（一秒一次）被调用一次：
	 *  
	 *    a, 每隔五秒检测一下当前位置与所有场地的距离，取里面最短的，且最短距离小于100米 
	 *    b, 如果之前取到了，本次跑步后面均不再检测场地
	 *    
	 * @param currentRunRecord
	 * @param gpsRecord
	 */
	private void detectPlayingField(RunRecord currentRunRecord,
			GPSRecord gpsRecord) {

		// 如果之前取到了，本次跑步后面均不再检测场地
		if (currentRunRecord.currentPlayingField != null) {
			return;
		}

		if (playingFieldList == null || playingFieldList.size() == 0) {
			return;
		}

		count++;
		// 每隔五秒检测一下
		if (count % 5 != 1) {
			return;
		}

		float minDistance = Float.MAX_VALUE;
		float[] results = new float[1];
		PlayingField currentPlayingField = null;

		// 检测一下当前位置与所有场地的距离，取里面最短的
		for (PlayingField playingField : playingFieldList) {

			Location.distanceBetween(gpsRecord.latitude, gpsRecord.longitude,
					playingField.latitude, playingField.longitude, results);

			if (results[0] < minDistance) {

				minDistance = results[0];
				currentPlayingField = playingField;
			}
		}

		// 且最短距离必须小于100米
		if (minDistance > Constant.FIELD_MIN_DISTANCE) {
			return;
		}

		// 把该场地登记到本次跑步记录中，*并且后面都不再检测场地了*
		currentRunRecord.currentPlayingField = currentPlayingField;
		currentRunRecord.playingFieldID = currentPlayingField._id;
	}

	// 计算本次记录点与上一个记录点的位移，单位为米
	private double makeLenth(Location location) {

		// 如果是第一个起点，则位移返回0
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
	 * 实际使用中，会极少次扑捉到 TEMPORARILY_UNAVAILABLE，AVAILABLE 状态。
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	/**
	 * 这个方法是在手机系统设置->GPS设置页面中，把GPS位置服务打上勾时激发
	 */
	@Override
	public void onProviderEnabled(String provider) {

	}

	/**
	 * 这个方法是在手机系统设置->GPS设置页面中，把GPS位置服务取消打勾时激发
	 */
	@Override
	public void onProviderDisabled(String provider) {

	}

}
