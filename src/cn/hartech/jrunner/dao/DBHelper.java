package cn.hartech.jrunner.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import cn.hartech.jrunner.util.MyUtility;

/**
 * SQL Lite���ݲ����İ����࣬���������ɣ�
 * 
 *   1��ͳһ�Ӹ����ȡDB����������ʵ�Ͼ������ݿ���������ñ����Զ���ķ������������3����
 *   2�����Ŀ�����ݿ��ļ������ڣ��Զ��������ļ�����������onCreate��
 *   3�����롢��ѯ�����µȲ������ڱ�����ɣ��е�DAO��ְ��
 * 
 * 
 * DB�ļ�Ҳ�ڱ���ָ���������༴������һ���������ݿ⣬���������˶Ը����ݿ����SQL������
 * ���Ҫ����һ�����ݿ⣬����һ�������ڱ�����ࡣ
 * 
 * 
 * ����豸�в忨������ /mnt/sdcard/APP_FOLDER Ŀ¼�´��������ݿ�
 * ���û�忨������/data/data/com.hartech.stock/databases/�ڴ���
 * 
 * 
 * ����ʹ�÷�����
 *    ��Activity��onCreateʱ��
 *    	stockDBHelper = new StockDBHelper(this);
 *    
 *    ���¼������У�
 *    	stockDBHelper.addRecords(17.4, 4.5); stockDBHelper.getAllRecords();
 *    
 *    ��onDestroy�����ڣ�
		@Override
		protected void onDestroy() {
			super.onDestroy();
			if (dbHelper != null) {
				dbHelper.close();
			}
		}
 * 
 * @author jin.zheng
 * @date 2013-3-24
 *
 */
public class DBHelper extends SQLiteOpenHelper {

	// �����Ӧ�����ݿ��ļ���
	private static final String DB_FILE = "jrunner.db";

	// �������ݿ�ṹ���ʱ��*�ֹ�*�Ķ�������
	// �������⵽�����VERSION�����п���DB�ļ���Version��ͬʱ��onUpgrade���������Զ�����
	// �����������С�ˣ����������ˣ����쳣
	private static final int VERSION = 1;

	// ���Բ������ݿ��DB����
	protected SQLiteDatabase db;

	/**
	* ��ʼ�������󣬽��贫�뵱ǰ��Activity
	* 
	* DB�ļ��ڱ����ڲ���ָ��
	* 
	* @param context
	*/
	public DBHelper(Context context) {

		// ��ȡ��ǰSDCard�ﱾApp������Ŀ¼����������/data/data/yourAppĿ¼��
		super(context, MyUtility.getAppFolder() + DB_FILE, null, VERSION);

		db = this.getWritableDatabase();
	}

	/**
	 * �����⵽û��db�ļ����ڴ�������ʱ�������onCreated�����Զ���������ʼ������
	 */
	@Override
	public void onCreate(SQLiteDatabase database) {

		// ����run_record��
		String createTableSQL = "create table run_record"
				+ "(_id integer primary key autoincrement, start_time, "
				+ "end_time, duration, length, speed_avg, gps_record_count, playingFieldID)";

		database.execSQL(createTableSQL);

		// ����gps_record��
		createTableSQL = "create table gps_record"
				+ "(_id integer primary key autoincrement, run_record_id, "
				+ "time, longitude, latitude)";

		database.execSQL(createTableSQL);

		// ����playing_field��
		createTableSQL = "create table playing_field"
				+ "(_id integer primary key autoincrement, name, "
				+ "longitude, latitude, runCount, lengthSum, lastRunDate)";

		database.execSQL(createTableSQL);
	}

	/**
	 * ����⵽�����VERSION�����п���DB�ļ���Version��ͬʱ�������������Զ�����
	 * ����ִ�����ݿ���SQL
	 */
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {

		Log.e("DBHelper onUpgrade()", "Database Version change! From "
				+ oldVersion + " -> " + newVersion);

	}

	// �û�����ֱ�ӻ�ȡDB������в���
	public SQLiteDatabase getDatabase() {

		return db;
	}

}
