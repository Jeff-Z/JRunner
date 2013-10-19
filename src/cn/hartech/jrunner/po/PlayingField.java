package cn.hartech.jrunner.po;

import java.util.Date;

/**
 * 代表一条操场记录
 * 
 * @author Jacky
 * Date:2013-4-14
 *
 */
public class PlayingField {

	public long _id;

	// 操场名称
	public String name;

	public double longitude;

	public double latitude;

	// 跑了几次
	public int runCount;

	// 共跑了几米
	public int lengthSum;

	// 最后一次跑步时间
	public Date lastRunDate;

}
