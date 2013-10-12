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

		// 启动数据库
		playingFieldDAO = new PlayingFieldDAO(this);

	}

	@Override
	protected void onResume() {

		super.onResume();

		// 把每条记录渲染到界面
		initPlayingFieldRecordsDrawing();

	}

	// 把每条记录渲染到界面
	private void initPlayingFieldRecordsDrawing() {

		List<PlayingField> fieldList = playingFieldDAO.getAll();

		if (fieldList == null || fieldList.size() == 0) {
			return;
		}

		// 界面中的LinearLayout
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout_playingfieldslist);

		linearLayout.removeAllViews();

		for (PlayingField playingField : fieldList) {

			// 使用graphic方式渲染每一个记录项
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
