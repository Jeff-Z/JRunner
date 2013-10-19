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

	// 读取配置文件及所有配置信息
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
	 * 这里用于判断本周内是否有跑步，有返回true
	 * 
	 * 检查的区间：
	 *  如果是“日”，则当前时间往前推一天
	 *  如果是“周”，则当前时间往前推七天
	 *  月则30天
	 * 
	 * 从RunningRecord表里拿要检查区间的所有跑步记录
	 * 	用于检查是否有跑步（有记录即有）
	 *  是否跑步次数符合要求（记录数）
	 * 	是否跑步长度符合要求（累加起来）
	 * 
	 * @return
	 */
	public boolean checkHasRun() {

		updateProperties();

		Calendar calendar = Calendar.getInstance();

		if (settingPeriod == 0) {

			// 往前推1天
			calendar.add(Calendar.DAY_OF_YEAR, -1);

		} else if (settingPeriod == 1) {

			// 往前推7天
			calendar.add(Calendar.DAY_OF_YEAR, -7);

		} else {

			// 往前推30天
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
					"获取RunRecord数据失败: msg=" + e.getMessage());
			return false;

		} finally {

			// 记得关掉数据连接，否则会很耗电
			if (runRecordDAO != null) {
				runRecordDAO.close();
			}
		}

		// 检查该区间内有没有记录，有没有跑
		if (runRecordList == null || runRecordList.size() == 0) {
			return false;
		}

		// 检查跑步次数
		if (runRecordList.size() < settingTimes + 1) {
			return false;
		}

		// 检查总长度
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
	 * 在Notification中展示提醒内容
	 * 
	 * title = 已经有10天没跑步了！
	 * content = 最近跑步日期：6月10日，跑了1090米。
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
					"获取RunRecord数据失败: msg=" + e.getMessage());

			BellRingerActivity.trigerNotification(context, "Error",
					"msg=" + e.getMessage());

			return;

		} finally {

			// 记得关掉数据连接，否则会很耗电
			if (runRecordDAO != null) {
				runRecordDAO.close();
			}
		}

		if (runRecord == null) {

			BellRingerActivity.trigerNotification(context, "Error", "无最近跑步记录！");
			return;
		}

		Date lastDate = runRecord.startTime;

		SimpleDateFormat df = new SimpleDateFormat("M月d日", Locale.CHINA);

		// content = 最近跑步日期：6月10日。
		String content = "最近跑步日期：" + df.format(lastDate) + "，跑了"
				+ (int) runRecord.length + "米。";

		// 距离今天的天数
		long days = (new Date().getTime() - lastDate.getTime())
				/ (1000 * 60 * 60 * 24);

		// title = 已经有10天没跑步了！
		String title = "已经有" + days + "天没有跑步了！";

		BellRingerActivity.trigerNotification(context, title, content);

	}
}
