package cn.hartech.jrunner.util;

public class Constant {

	// 跑步信息页面上面，时间的更新频率
	public final static int RUNNING_TIME_UPDATE = 1000;

	// 跑步时检测靠近的场地时，靠近多少米才算到达该场地
	public final static int FIELD_MIN_DISTANCE = 100;

	// 配置文件的名称
	public final static String PROPERTY_FILE_NAME = "setting.txt";

	// 本App在手机上的目录名称，DB与配置文件等将保存到该目录下
	public static final String APP_FOLDER = "/JRunner/";

	// Bell Service服务的检查时间间隔
	public final static int BELL_SERVICE_UPDATE = 6 * 60 * 60 * 1000;

}
