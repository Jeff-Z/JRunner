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

		// �������ݿ�
		playingFieldDAO = new PlayingFieldDAO(this);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		gpsListener = new GPSListenerFieldLocation(this);

		initFormData();
	}

	// ��ʼ��������
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

	// ���¡���������ťʱ���������¼ӣ�Ҳ�����Ǳ༭
	public void onClickSave(View view) {

		// nullΪ�½����صı��
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

	// ���¶�λ��ťʱ
	public void onClickGPS(View view) {

		// ��ǰ���ڶ�λ���ٰ�����ȡ����λ
		if (isLocating) {

			stopLocating();
			buttonGPS.setText("��ʼ��λ");
			isLocating = false;

		} else {

			// ��ʼ����
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, gpsListener);

			buttonGPS.setText("�ٻ�������...");

			isLocating = true;
		}

	}

	// ��GPS��⵽�����ص��ĺ���
	public void updateLocation(Location location) {

		longitude = location.getLongitude();
		latitude = location.getLatitude();

		isLocating = false;
		stopLocating();

		fireConfirmDialog();
	}

	// ����ɶ�λ�����ѶԻ���
	private void fireConfirmDialog() {

		AlertDialog.Builder builder = new Builder(this);

		builder.setTitle("����ɶ�λ");

		builder.setPositiveButton("ȷ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				buttonGPS.setText("��ʼ��λ");

				dialog.dismiss();

			}
		});

		builder.create().show();
	}

	// ֹͣ����������
	private void stopLocating() {

		// ȡ��ע�����
		locationManager.removeUpdates(gpsListener);
	}

	// ����ɾ����ťʱ
	public void onClickDelete(View view) {

		fireDeleteConfirmDialog();

	}

	// �Ƿ�ɾ�����صĶԻ���
	private void fireDeleteConfirmDialog() {

		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("ȷ��ɾ���ó��أ�");
		builder.setPositiveButton("ȡ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
			}
		});
		builder.setNegativeButton("ȷ��", new OnClickListener() {

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
