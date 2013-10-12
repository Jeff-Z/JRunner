package cn.hartech.jrunner.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import cn.hartech.jrunner.po.GPSRecord;

public class GPSRecordDAO extends DBHelper {

	public GPSRecordDAO(Context context) {

		super(context);
	}

	// 添加GPS记录及其所属跑步记录ID
	public void add(GPSRecord gpsRecord) {

		String insertSQL = "insert into gps_record values(null, ?, ?, ?, ?)";

		db.execSQL(insertSQL, new Object[] { gpsRecord.runRecordID,
				gpsRecord.time.getTime(), gpsRecord.longitude,
				gpsRecord.latitude });
	}

	public List<GPSRecord> getAll() {

		List<GPSRecord> list = new ArrayList<GPSRecord>();

		Cursor cursor = db.rawQuery("select * from gps_record order by _id",
				null);

		while (cursor.moveToNext()) {

			GPSRecord gpsRecord = new GPSRecord();

			gpsRecord.runRecordID = cursor.getInt(1);

			gpsRecord.time = new Date(cursor.getLong(2));

			gpsRecord.longitude = cursor.getDouble(3);
			gpsRecord.latitude = cursor.getDouble(4);

			list.add(gpsRecord);
		}

		cursor.close();

		return list;
	}

	public void deleteAll() {

		db.delete("gps_record", null, null);
	}

}
