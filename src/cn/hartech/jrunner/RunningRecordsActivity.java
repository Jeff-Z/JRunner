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

		// �������ݿ�
		runRecordDAO = new RunRecordDAO(this);
		playingFieldDAO = new PlayingFieldDAO(this);

		// ��ÿ����¼��Ⱦ������
		initRunningRecordDrawing();
	}

	// ��ÿ����¼��Ⱦ������
	private void initRunningRecordDrawing() {

		List<RunRecord> recordList = runRecordDAO.getAll();
		List<PlayingField> playingFieldList = playingFieldDAO.getAll();

		// Ϊÿһ���ܲ���¼�ҳ���Ӧ�ĳ���DO����
		RunRecordDAO.fillAllRecordsWithFields(recordList, playingFieldList);

		if (recordList == null || recordList.size() == 0) {
			return;
		}

		// �����е�LinearLayout
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout_runningrecords);

		for (RunRecord runRecord : recordList) {

			// ʹ��graphic��ʽ��Ⱦÿһ����¼��
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
	 * ɾ��ָ�����ܲ���¼
	 * ����ָ����ʱ����
	 * 
	 * @param runRecord
	 */
	public void deleteRunRecord(RunRecord runRecord) {

		runRecordDAO.delete(runRecord);
	}

}
