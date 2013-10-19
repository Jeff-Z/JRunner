package cn.hartech.jrunner.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import cn.hartech.jrunner.po.PlayingField;
import cn.hartech.jrunner.po.RunRecord;
import cn.hartech.jrunner.util.MyUtility;

public class RunRecordDAO extends DBHelper {

	public RunRecordDAO(Context context) {

		super(context);
	}

	// ����ܲ���¼���������½���ID�����ڰ����Ӧ��GPS��¼
	public int add(RunRecord runRecord) {

		String insertSQL = "insert into run_record values(null, ?, ?, ?, ?, ?, ?, ?)";

		db.execSQL(
				insertSQL,
				new Object[] {
						MyUtility.DF_yyyy_MM_dd_HH_mm_ss
								.format(runRecord.startTime),
						MyUtility.DF_yyyy_MM_dd_HH_mm_ss
								.format(runRecord.endTime), runRecord.duration,
						runRecord.length, runRecord.speedAvg,
						runRecord.gpsRecordCount, runRecord.playingFieldID });

		Cursor cursor = db.rawQuery("select LAST_INSERT_ROWID()", null);
		cursor.moveToFirst();

		return cursor.getInt(0);
	}

	public List<RunRecord> getAll() {

		List<RunRecord> list = new ArrayList<RunRecord>();

		Cursor cursor = db.rawQuery(
				"select * from run_record order by _id desc", null);

		while (cursor.moveToNext()) {

			RunRecord runRecord = new RunRecord();

			runRecord._id = cursor.getLong(0);

			try {

				runRecord.startTime = MyUtility.DF_yyyy_MM_dd_HH_mm_ss
						.parse(cursor.getString(1));
				runRecord.endTime = MyUtility.DF_yyyy_MM_dd_HH_mm_ss
						.parse(cursor.getString(2));

			} catch (ParseException e) {
				e.printStackTrace();
			}

			runRecord.duration = cursor.getInt(3);
			runRecord.length = cursor.getDouble(4);
			runRecord.speedAvg = cursor.getDouble(5);
			runRecord.gpsRecordCount = cursor.getInt(6);
			runRecord.playingFieldID = cursor.getLong(7);

			list.add(runRecord);
		}

		cursor.close();

		return list;
	}

	/**
	 * ����ָ������֮��������г��ذ󶨵ļ�¼
	 * 
	 * @param startDate
	 * @return
	 */
	public List<RunRecord> getListAfterDate(Date startDate) {

		List<RunRecord> list = new ArrayList<RunRecord>();

		Cursor cursor = db
				.rawQuery(
						"select * from run_record where start_time >= ? and playingFieldID > 0",
						new String[] { MyUtility.DF_yyyy_MM_dd_HH_mm_ss
								.format(startDate) });

		while (cursor.moveToNext()) {

			RunRecord runRecord = new RunRecord();

			runRecord._id = cursor.getLong(0);

			try {

				runRecord.startTime = MyUtility.DF_yyyy_MM_dd_HH_mm_ss
						.parse(cursor.getString(1));
				runRecord.endTime = MyUtility.DF_yyyy_MM_dd_HH_mm_ss
						.parse(cursor.getString(2));

			} catch (ParseException e) {
				e.printStackTrace();
			}

			runRecord.duration = cursor.getInt(3);
			runRecord.length = cursor.getDouble(4);
			runRecord.speedAvg = cursor.getDouble(5);
			runRecord.gpsRecordCount = cursor.getInt(6);
			runRecord.playingFieldID = cursor.getLong(7);

			list.add(runRecord);
		}

		cursor.close();

		return list;
	}

	/**
	 * ����������ܲ���¼���г��ذ󶨵�
	 * 
	 * @param startDate
	 * @return
	 */
	public RunRecord getLatestRunRecord() {

		Cursor cursor = db
				.rawQuery(
						"select * from run_record where playingFieldID > 0 order by start_time desc limit 1",
						null);

		if (!cursor.moveToFirst()) {
			return null;
		}

		RunRecord runRecord = new RunRecord();

		runRecord._id = cursor.getLong(0);

		try {

			runRecord.startTime = MyUtility.DF_yyyy_MM_dd_HH_mm_ss.parse(cursor
					.getString(1));
			runRecord.endTime = MyUtility.DF_yyyy_MM_dd_HH_mm_ss.parse(cursor
					.getString(2));

		} catch (ParseException e) {
			e.printStackTrace();
		}

		runRecord.duration = cursor.getInt(3);
		runRecord.length = cursor.getDouble(4);
		runRecord.speedAvg = cursor.getDouble(5);
		runRecord.gpsRecordCount = cursor.getInt(6);
		runRecord.playingFieldID = cursor.getLong(7);

		cursor.close();

		return runRecord;
	}

	public void deleteAll() {

		db.delete("run_record", null, null);
	}

	public void delete(RunRecord runRecord) {

		db.delete("run_record", "_id=?",
				new String[] { String.valueOf(runRecord._id) });
	}

	// Ϊÿһ���ܲ���¼�ҳ���Ӧ�ĳ���DO����
	public static void fillAllRecordsWithFields(List<RunRecord> recordList,
			List<PlayingField> playingFieldList) {

		if (recordList == null || recordList.size() == 0) {
			return;
		}

		if (playingFieldList == null || playingFieldList.size() == 0) {
			return;
		}

		for (RunRecord runRecord : recordList) {

			runRecord.currentPlayingField = getPlayingFieldByID(
					playingFieldList, runRecord.playingFieldID);
		}
	}

	private static PlayingField getPlayingFieldByID(
			List<PlayingField> playingFieldList, long id) {

		for (PlayingField playingField : playingFieldList) {

			if (playingField._id == id) {
				return playingField;
			}
		}

		return null;

	}

}
