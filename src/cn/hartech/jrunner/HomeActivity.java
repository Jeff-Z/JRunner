package cn.hartech.jrunner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_home);

	}

	public void onClickStartRunning(View view) {

		Intent intent = new Intent(this, RunningActivity.class);
		startActivity(intent);
	}

	public void onClickPlayingFields(View view) {

		Intent intent = new Intent(this, PlayingFieldsActivity.class);
		startActivity(intent);

	}

	public void onClickRunningRecords(View view) {

		Intent intent = new Intent(this, RunningRecordsActivity.class);
		startActivity(intent);

	}

	public void onClickBellRinger(View view) {

		Intent intent = new Intent(this, BellRingerActivity.class);
		startActivity(intent);

	}

}
