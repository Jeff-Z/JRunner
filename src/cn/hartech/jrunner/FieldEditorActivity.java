package cn.hartech.jrunner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import cn.hartech.jrunner.dao.PlayingFieldDAO;
import cn.hartech.jrunner.engine.GPSListenerFieldLocation;
import cn.hartech.jrunner.po.PlayingField;

public class FieldEditorActivity extends Activity {

	public static final String BUNDLE_KEY_PLAYING_FIELD_ID = "BUNDLE_KEY_FIELD";

	private EditText editTextFieldName;
	private Button buttonGPS;

	private PlayingFieldDAO playingFieldDAO;

	private PlayingField playingField;

	private boolean isLocating = false;

	private double longitude, latitude;

	private LocationManager locationManager;
	private GPSListenerFieldLocation gpsListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_field_editor);

		editTextFieldName = (EditText) findViewById(R.id.editText_fieldName);
		buttonGPS = (Button) findViewById(R.id.button_gpslocation);

		// 启动数据库
		playingFieldDAO = new PlayingFieldDAO(this);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		gpsListener = new GPSListenerFieldLocation(this);

		initFormData();
	}

	// 初始化表单数据
	private void initFormData() {

		Bundle bundle = getIntent().getExtras();
		if (bundle == null
				|| bundle
						.getLong(FieldEditorActivity.BUNDLE_KEY_PLAYING_FIELD_ID) == 0) {
			return;
		}

		long fieldID = getIntent().getExtras().getLong(
				FieldEditorActivity.BUNDLE_KEY_PLAYING_FIELD_ID);

		playingField = playingFieldDAO.getByID(fieldID);

		if (playingField != null) {
			editTextFieldName.setText(playingField.name);
			longitude = playingField.longitude;
			latitude = playingField.latitude;
		}
	}

	// 按下“保护”按钮时，可能是新加，也可能是编辑
	public void onClickSave(View view) {

		// null为新建场地的标记
		if (playingField == null) {

			playingField = new PlayingField();
			playingField.name = editTextFieldName.getText().toString();
			playingField.longitude = longitude;
			playingField.latitude = latitude;

			playingFieldDAO.add(playingField);

		}

		playingField.name = editTextFieldName.getText().toString();
		playingField.longitude = longitude;
		playingField.latitude = latitude;

		playingFieldDAO.update(playingField);

		finish();

	}

	// 按下定位按钮时
	public void onClickGPS(View view) {

		// 当前正在定位，再按下则取消定位
		if (isLocating) {

			stopLocating();
			buttonGPS.setText("开始定位");
			isLocating = false;

		} else {

			// 开始监听
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, gpsListener);

			buttonGPS.setText("召唤神龙中...");

			isLocating = true;
		}

	}

	// 被GPS检测到坐标后回调的函数
	public void updateLocation(Location location) {

		longitude = location.getLongitude();
		latitude = location.getLatitude();

		isLocating = false;
		stopLocating();

		fireConfirmDialog();
	}

	// 已完成定位的提醒对话框
	private void fireConfirmDialog() {

		AlertDialog.Builder builder = new Builder(this);

		builder.setTitle("已完成定位");

		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				buttonGPS.setText("开始定位");

				dialog.dismiss();

			}
		});

		builder.create().show();
	}

	// 停止监听的命令
	private void stopLocating() {

		// 取消注册监听
		locationManager.removeUpdates(gpsListener);
	}

	// 按下删除按钮时
	public void onClickDelete(View view) {

		fireDeleteConfirmDialog();

	}

	// 是否删除场地的对话框
	private void fireDeleteConfirmDialog() {

		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("确定删除该场地？");
		builder.setPositiveButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
			}
		});
		builder.setNegativeButton("确认", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				if (playingField != null) {
					playingFieldDAO.delete(playingField);
				}

				dialog.dismiss();

				finish();

			}
		});
		builder.create().show();
	}

	@Override
	protected void onDestroy() {

		isLocating = false;
		stopLocating();

		super.onDestroy();
		if (playingFieldDAO != null) {
			playingFieldDAO.close();
		}
	}

}
