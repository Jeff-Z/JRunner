package cn.hartech.jrunner.po;

import java.util.Date;

/**
 * ����һ���ٳ���¼
 * 
 * @author Jacky
 * Date:2013-4-14
 *
 */
public class PlayingField {

	public long _id;

	// �ٳ�����
	public String name;

	public double longitude;

	public double latitude;

	// ���˼���
	public int runCount;

	// �����˼���
	public int lengthSum;

	// ���һ���ܲ�ʱ��
	public Date lastRunDate;

}
