package cn.hartech.jrunner.engine;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.util.Log;
import cn.hartech.jrunner.BellRingerActivity;
import cn.hartech.jrunner.dao.RunRecordDAO;
import cn.hartech.jrunner.po.RunRecord;
import cn.hartech.jrunner.util.MyProperties;

public class BellRingerEngine {

	private Context context;

	public BellRingerEngine(Context context) {

		this.context = context;
	}

	private int settingPeriod, settingTimes, settingLength;

	// ��ȡ�����ļ�������������Ϣ
	private void updateProperties() {

		MyProperties property = new MyProperties();

		settingPeriod = Integer.parseInt(property.getProperty("settingPeriod",
				"0"));

		settingTimes = Integer.parseInt(property.getProperty("settingTimes",
				"0"));

		settingLength = Integer.parseInt(property.getProperty("settingLength",
				"0"));

	}

	/**
	 * ���������жϱ������Ƿ����ܲ����з���true
	 * 
	 * �������䣺
	 *  ����ǡ��ա�����ǰʱ����ǰ��һ��
	 *  ����ǡ��ܡ�����ǰʱ����ǰ������
	 *  ����30��
	 * 
	 * ��RunningRecord������Ҫ�������������ܲ���¼
	 * 	���ڼ���Ƿ����ܲ����м�¼���У�
	 *  �Ƿ��ܲ���������Ҫ�󣨼�¼����
	 * 	�Ƿ��ܲ����ȷ���Ҫ���ۼ�������
	 * 
	 * @return
	 */
	public boolean checkHasRun() {

		updateProperties();

		Calendar calendar = Calendar.getInstance();

		if (settingPeriod == 0) {

			// ��ǰ��1��
			calendar.add(Calendar.DAY_OF_YEAR, -1);

		} else if (settingPeriod == 1) {

			// ��ǰ��7��
			calendar.add(Calendar.DAY_OF_YEAR, -7);

		} else {

			// ��ǰ��30��
			calendar.add(Calendar.DAY_OF_YEAR, -30);
		}

		Date startDate = calendar.getTime();

		List<RunRecord> runRecordList = null;
		RunRecordDAO runRecordDAO = null;

		try {

			runRecordDAO = new RunRecordDAO(context);

			runRecordList = runRecordDAO.getListAfterDate(startDate);

		} catch (Exception e) {

			Log.e("BellRingerEngine ERROR",
					"��ȡRunRecord����ʧ��: msg=" + e.getMessage());
			return false;

		} finally {

			// �ǵùص��������ӣ������ܺĵ�
			if (runRecordDAO != null) {
				runRecordDAO.close();
			}
		}

		// ������������û�м�¼����û����
		if (runRecordList == null || runRecordList.size() == 0) {
			return false;
		}

		// ����ܲ�����
		if (runRecordList.size() < settingTimes + 1) {
			return false;
		}

		// ����ܳ���
		double length = 0.0;
		for (RunRecord runRecord : runRecordList) {

			//			Log.e("XXXX", MyUtility.DF_yyyy_MM_dd_HH_mm_ss
			//					.format(runRecord.startTime));

			length += runRecord.length;
		}

		if (settingLength == 0) {

			// 500
			if (length < 500) {
				return false;
			}

		} else if (settingLength == 1) {

			// 1000
			if (length < 1000) {
				return false;
			}

		} else if (settingLength == 2) {

			// 2000
			if (length < 2000) {
				return false;
			}

		} else if (settingLength == 3) {

			// 5000
			if (length < 5000) {
				return false;
			}

		} else {

			// 10000
			if (length < 10000) {
				return false;
			}

		}

		return true;
	}

	/**
	 * ��Notification��չʾ��������
	 * 
	 * title = �Ѿ���10��û�ܲ��ˣ�
	 * content = ����ܲ����ڣ�6��10�գ�����1090�ס�
	 * 
	 * @return
	 */
	public void fireNotification() {

		RunRecord runRecord = null;
		RunRecordDAO runRecordDAO = null;

		try {

			runRecordDAO = new RunRecordDAO(context);

			runRecord = runRecordDAO.getLatestRunRecord();

		} catch (Exception e) {

			Log.e("BellRingerEngine ERROR",
					"��ȡRunRecord����ʧ��: msg=" + e.getMessage());

			BellRingerActivity.trigerNotification(context, "Error",
					"msg=" + e.getMessage());

			return;

		} finally {

			// �ǵùص��������ӣ������ܺĵ�
			if (runRecordDAO != null) {
				runRecordDAO.close();
			}
		}

		if (runRecord == null) {

			BellRingerActivity.trigerNotification(context, "Error", "������ܲ���¼��");
			return;
		}

		Date lastDate = runRecord.startTime;

		SimpleDateFormat df = new SimpleDateFormat("M��d��", Locale.CHINA);

		// content = ����ܲ����ڣ�6��10�ա�
		String content = "����ܲ����ڣ�" + df.format(lastDate) + "������"
				+ (int) runRecord.length + "�ס�";

		// ������������
		long days = (new Date().getTime() - lastDate.getTime())
				/ (1000 * 60 * 60 * 24);

		// title = �Ѿ���10��û�ܲ��ˣ�
		String title = "�Ѿ���" + days + "��û���ܲ��ˣ�";

		BellRingerActivity.trigerNotification(context, title, content);

	}
}
