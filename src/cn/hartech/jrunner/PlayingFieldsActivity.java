package cn.hartech.jrunner;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import cn.hartech.jrunner.dao.PlayingFieldDAO;
import cn.hartech.jrunner.po.PlayingField;
import cn.hartech.jrunner.ui.custom.CustomViewPlayingField;

public class PlayingFieldsActivity extends Activity {

	private PlayingFieldDAO playingFieldDAO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_playingfields);

		// �������ݿ�
		playingFieldDAO = new PlayingFieldDAO(this);

	}

	@Override
	protected void onResume() {

		super.onResume();

		// ��ÿ����¼��Ⱦ������
		initPlayingFieldRecordsDrawing();

	}

	// ��ÿ����¼��Ⱦ������
	private void initPlayingFieldRecordsDrawing() {

		List<PlayingField> fieldList = playingFieldDAO.getAll();

		if (fieldList == null || fieldList.size() == 0) {
			return;
		}

		// �����е�LinearLayout
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout_playingfieldslist);

		linearLayout.removeAllViews();

		for (PlayingField playingField : fieldList) {

			// ʹ��graphic��ʽ��Ⱦÿһ����¼��
			CustomViewPlayingField recordItemView = new CustomViewPlayingField(
					this, playingField);

			linearLayout.addView(recordItemView);
		}

	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

		if (playingFieldDAO != null) {
			playingFieldDAO.close();
		}
	}

	public void onClickAddField(View view) {

		Intent intent = new Intent(this, FieldEditorActivity.class);
		startActivity(intent);
	}

}
