package cn.hartech.jrunner;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import cn.hartech.jrunner.dao.PlayingFieldDAO;
import cn.hartech.jrunner.dao.RunRecordDAO;
import cn.hartech.jrunner.po.PlayingField;
import cn.hartech.jrunner.po.RunRecord;
import cn.hartech.jrunner.ui.custom.CustomViewRunningRecord;

public class RunningRecordsActivity extends Activity {

	private RunRecordDAO runRecordDAO;
	private PlayingFieldDAO playingFieldDAO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_running_records);

		// 启动数据库
		runRecordDAO = new RunRecordDAO(this);
		playingFieldDAO = new PlayingFieldDAO(this);

		// 把每条记录渲染到界面
		initRunningRecordDrawing();
	}

	// 把每条记录渲染到界面
	private void initRunningRecordDrawing() {

		List<RunRecord> recordList = runRecordDAO.getAll();
		List<PlayingField> playingFieldList = playingFieldDAO.getAll();

		// 为每一个跑步记录找出对应的场地DO对象
		RunRecordDAO.fillAllRecordsWithFields(recordList, playingFieldList);

		if (recordList == null || recordList.size() == 0) {
			return;
		}

		// 界面中的LinearLayout
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout_runningrecords);

		for (RunRecord runRecord : recordList) {

			// 使用graphic方式渲染每一个记录项
			CustomViewRunningRecord recordItemView = new CustomViewRunningRecord(
					this, runRecord);

			linearLayout.addView(recordItemView);
		}

	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

		if (runRecordDAO != null) {
			runRecordDAO.close();
		}

		if (playingFieldDAO != null) {
			playingFieldDAO.close();
		}
	}

	/**
	 * 删除指定的跑步记录
	 * 在手指长按时激发
	 * 
	 * @param runRecord
	 */
	public void deleteRunRecord(RunRecord runRecord) {

		runRecordDAO.delete(runRecord);
	}

}
