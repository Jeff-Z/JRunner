package cn.hartech.jrunner.po;

import java.util.Date;

/**
 * 
 * 记录一次跑步中多个GPS位置
 * 
 * 即一次onLocationChange生成一条记录
 * 
 * @author Jacky
 * Date:2013-4-6
 *
 */
public class GPSRecord {

	public int runRecordID;

	public Date time;

	public double longitude;

	public double latitude;

	// 距离上个点的位置，单位为米
	public double length;

	public double speed;

	// 本记录所属的跑步记录RunRecord的
	public RunRecord runRecord;

}
