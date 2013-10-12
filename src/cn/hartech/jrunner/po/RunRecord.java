package cn.hartech.jrunner.po;

import java.util.Date;
import java.util.List;

/**
 * һ��RunRecord����һ���ܲ���¼
 * 
 * @author Jacky
 * Date:2013-4-6
 *
 */
public class RunRecord {

	public long _id;

	public Date startTime;

	public Date endTime;

	// �ܲ�����ʱ�䣬��λΪ��
	public int duration;

	// �����ܲ����ȣ���λΪ��
	public double length;

	// �����ܲ�ƽ���ٶȣ���λ ��/��
	public double speedAvg;

	// ����λ�ü�¼�ĸ���
	public int gpsRecordCount;

	// ��⵽�Ĳٳ�ID
	public long playingFieldID;

	// �����ܲ�������λ�ü�¼
	public List<GPSRecord> gpsRecordList;

	// ��ǰ��⵽�ĳ���
	public PlayingField currentPlayingField;

}
