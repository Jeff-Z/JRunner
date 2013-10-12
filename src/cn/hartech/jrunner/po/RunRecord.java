package cn.hartech.jrunner.po;

import java.util.Date;
import java.util.List;

/**
 * 一个RunRecord代表一次跑步记录
 * 
 * @author Jacky
 * Date:2013-4-6
 *
 */
public class RunRecord {

	public long _id;

	public Date startTime;

	public Date endTime;

	// 跑步持续时间，单位为秒
	public int duration;

	// 本次跑步长度，单位为米
	public double length;

	// 本次跑步平均速度，单位 米/秒
	public double speedAvg;

	// 所有位置记录的个数
	public int gpsRecordCount;

	// 检测到的操场ID
	public long playingFieldID;

	// 本次跑步的所有位置记录
	public List<GPSRecord> gpsRecordList;

	// 当前检测到的场地
	public PlayingField currentPlayingField;

}
