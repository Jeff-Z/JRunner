package cn.hartech.jrunner.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import cn.hartech.jrunner.po.PlayingField;
import cn.hartech.jrunner.util.MyUtility;

public class PlayingFieldDAO extends DBHelper {

	public PlayingFieldDAO(Context context) {

		super(context);
	}

	// 添加GPS记录及其所属跑步记录ID
	public void add(PlayingField playingField) {

		String insertSQL = "insert into playing_field values(null, ?, ?, ?, ?, ?, ?)";

		db.execSQL(
				insertSQL,
				new Object[] {
						playingField.name,
						playingField.longitude,
						playingField.latitude,
						playingField.runCount,
						playingField.lengthSum,
						MyUtility
								.format_yyyy_MM_dd_HH_mm_ss(playingField.lastRunDate) });
	}

	public List<PlayingField> getAll() {

		List<PlayingField> list = new ArrayList<PlayingField>();

		Cursor cursor = db.rawQuery(
				"select * from playing_field order by lastRunDate desc, "
						+ "lengthSum desc", null);

		while (cursor.moveToNext()) {

			PlayingField playingField = new PlayingField();

			playingField._id = cursor.getLong(0);

			playingField.name = cursor.getString(1);

			playingField.longitude = cursor.getDouble(2);
			playingField.latitude = cursor.getDouble(3);

			playingField.runCount = cursor.getInt(4);

			playingField.lengthSum = cursor.getInt(5);

			playingField.lastRunDate = MyUtility
					.parse_yyyy_MM_dd_HH_mm_ss(cursor.getString(6));

			list.add(playingField);
		}

		cursor.close();

		return list;
	}

	public PlayingField getByID(long id) {

		Cursor cursor = db.rawQuery(
				"select * from playing_field where _id = ?",
				new String[] { String.valueOf(id) });

		if (!cursor.moveToFirst()) {
			return null;
		}

		PlayingField playingField = new PlayingField();

		playingField._id = cursor.getLong(0);

		playingField.name = cursor.getString(1);

		playingField.longitude = cursor.getDouble(2);
		playingField.latitude = cursor.getDouble(3);

		playingField.runCount = cursor.getInt(4);

		playingField.lengthSum = cursor.getInt(5);

		playingField.lastRunDate = MyUtility.parse_yyyy_MM_dd_HH_mm_ss(cursor
				.getString(6));

		cursor.close();

		return playingField;
	}

	public void deleteAll() {

		db.delete("playing_field", null, null);
	}

	public void delete(PlayingField playingField) {

		db.delete("playing_field", "_id=?",
				new String[] { String.valueOf(playingField._id) });
	}

	public void update(PlayingField playingField) {

		String updateSQL = "update playing_field set "
				+ "name = ?, longitude=?, latitude=?, runCount=?, "
				+ "lengthSum=?, lastRunDate=? where _id=?";

		db.execSQL(
				updateSQL,
				new Object[] {
						playingField.name,
						playingField.longitude,
						playingField.latitude,
						playingField.runCount,
						playingField.lengthSum,
						MyUtility
								.format_yyyy_MM_dd_HH_mm_ss(playingField.lastRunDate),
						playingField._id });
	}

}
