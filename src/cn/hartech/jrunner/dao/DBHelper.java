package cn.hartech.jrunner.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import cn.hartech.jrunner.util.MyUtility;

/**
 * SQL Lite数据操作的帮助类，该类可以完成：
 * 
 *   1，统一从该类获取DB操作对象（事实上尽量数据库操作都调用本类自定义的方法，如下面的3）。
 *   2，如果目标数据库文件不存在，自动创建该文件，并创建表（onCreate）
 *   3，插入、查询、更新等操作均在本类完成，承担DAO层职责
 * 
 * 
 * DB文件也在本类指定，即本类即代表着一个物理数据库，这里屏蔽了对该数据库各种SQL操作。
 * 如果要增加一个数据库，增加一个类似于本类的类。
 * 
 * 
 * 如果设备有插卡，则在 /mnt/sdcard/APP_FOLDER 目录下创建该数据库
 * 如果没插卡，则在/data/data/com.hartech.stock/databases/内创建
 * 
 * 
 * 本类使用方法：
 *    在Activity内onCreate时：
 *    	stockDBHelper = new StockDBHelper(this);
 *    
 *    在事件方法中：
 *    	stockDBHelper.addRecords(17.4, 4.5); stockDBHelper.getAllRecords();
 *    
 *    在onDestroy方法内：
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

	// 本类对应的数据库文件名
	private static final String DB_FILE = "jrunner.db";

	// 当有数据库结构变更时，*手工*改动该数字
	// 当程序检测到本类的VERSION与现有卡里DB文件的Version不同时，onUpgrade方法将被自动调用
	// 该数字如果改小了，将启动不了，抛异常
	private static final int VERSION = 1;

	// 可以操作数据库的DB对象
	protected SQLiteDatabase db;

	/**
	* 初始化本对象，仅需传入当前的Activity
	* 
	* DB文件在本类内部已指定
	* 
	* @param context
	*/
	public DBHelper(Context context) {

		// 获取当前SDCard里本App的数据目录，将保存在/data/data/yourApp目录内
		super(context, MyUtility.getAppFolder() + DB_FILE, null, VERSION);

		db = this.getWritableDatabase();
	}

	/**
	 * 如果检测到没该db文件，在创建该类时将会调用onCreated方法自动创建表，初始化数据
	 */
	@Override
	public void onCreate(SQLiteDatabase database) {

		// 创建run_record表
		String createTableSQL = "create table run_record"
				+ "(_id integer primary key autoincrement, start_time, "
				+ "end_time, duration, length, speed_avg, gps_record_count, playingFieldID)";

		database.execSQL(createTableSQL);

		// 创建gps_record表
		createTableSQL = "create table gps_record"
				+ "(_id integer primary key autoincrement, run_record_id, "
				+ "time, longitude, latitude)";

		database.execSQL(createTableSQL);

		// 创建playing_field表
		createTableSQL = "create table playing_field"
				+ "(_id integer primary key autoincrement, name, "
				+ "longitude, latitude, runCount, lengthSum, lastRunDate)";

		database.execSQL(createTableSQL);
	}

	/**
	 * 但检测到本类的VERSION与现有卡里DB文件的Version不同时，本方法将被自动调用
	 * 用于执行数据库变更SQL
	 */
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {

		Log.e("DBHelper onUpgrade()", "Database Version change! From "
				+ oldVersion + " -> " + newVersion);

	}

	// 用户可以直接获取DB对象进行操作
	public SQLiteDatabase getDatabase() {

		return db;
	}

}
