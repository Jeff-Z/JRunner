package cn.hartech.jrunner.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Environment;

public class MyUtility {

	public static final SimpleDateFormat DF_HH_mm_ss = new SimpleDateFormat(
			"HH:mm:ss", Locale.CHINA);

	public static final SimpleDateFormat DF_yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.CHINA);

	public static final SimpleDateFormat DF_yyyy_MM_dd = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.CHINA);

	// ������Ҫ�Ǽ����쳣����
	public static Date parse_yyyy_MM_dd_HH_mm_ss(String dateStr) {

		try {

			return DF_yyyy_MM_dd_HH_mm_ss.parse(dateStr);

		} catch (Exception e) {

			return new Date(0);
		}

	}

	// ������Ҫ�Ǽ����쳣����
	public static String format_yyyy_MM_dd_HH_mm_ss(Date date) {

		try {

			return DF_yyyy_MM_dd_HH_mm_ss.format(date);

		} catch (Exception e) {

			return "";
		}
	}

	// ����С��λ��
	public static String setScale(double value, int scale) {

		BigDecimal decimal = new BigDecimal(value);

		// �������룬������λС��
		value = decimal.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();

		return String.valueOf(value);
	}

	// �����ܹ������������5��07��
	public static String getMinuteAndSecond(int seconds) {

		int minute = seconds / 60;

		seconds = seconds % 60;

		String result = String.valueOf(minute);

		result += "��";

		if (seconds < 10) {
			result += "0" + seconds;
		} else {
			result += seconds;
		}

		result += "��";

		return result;
	}

	// ���أ��գ��� ��
	public static String getWeekChineseWeekDay(Date date) {

		@SuppressWarnings("deprecation")
		int weekDay = date.getDay();

		// 0 = Sunday, 1 = Monday, 2 = Tuesday, 3 = Wednesday, 
		// 4 = Thursday, 5 = Friday, 6 = Saturday
		switch (weekDay) {

		case 0:
			return "��";

		case 1:
			return "һ";

		case 2:
			return "��";

		case 3:
			return "��";

		case 4:
			return "��";

		case 5:
			return "��";

		case 6:
			return "��";

		default:
			return "";
		}
	}

	/**
	 * ��ȡ��ǰ���濨�ﱾApp������Ŀ¼�����û���򷵻�""����������/data/data/yourAppĿ¼��
	 * 
	 * ����п����������ƣ�"/mnt/sdcard/stock/"
	 * 
	 * @return
	 */
	public static String getAppFolder() {

		// �Ƿ��д��濨
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			try {

				String scardDir = Environment.getExternalStorageDirectory()
						.getCanonicalPath();

				return scardDir + Constant.APP_FOLDER;

			} catch (IOException e) {
				return "";
			}

		} else {

			return "";
		}
	}
}
