package cn.hartech.jrunner.po;

import java.util.Date;

/**
 * 
 * ��¼һ���ܲ��ж��GPSλ��
 * 
 * ��һ��onLocationChange����һ����¼
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

	// �����ϸ����λ�ã���λΪ��
	public double length;

	public double speed;

	// ����¼�������ܲ���¼RunRecord��
	public RunRecord runRecord;

}
