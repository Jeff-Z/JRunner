package cn.hartech.jrunner.ui.listener;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import cn.hartech.jrunner.RunningRecordsActivity;
import cn.hartech.jrunner.po.RunRecord;
import cn.hartech.jrunner.ui.custom.CustomViewRunningRecord;

/**
 * OK�����ֻ�App�����ϣ���һ��Activity��View��������һ��View���ж�㡣
 * ����ָ��ĳ��Activity/View�ϴ���������ʱ�����ᴥ�����ϲ�View��onXXXX������
 * ����÷���������󷵻�true�����ʾ���¼��Ѵ����ꡣ
 * �������false�����������¼��Իᴫ�ݵ���ͬλ�õ���һ��Activity/View��������onXXXX������
 * 
 * 
 * ���������¸����¼������������������*ͬʱ*��������
 * ����ʱ������ �¼��� ���� �¼�����ͬʱ�����ã��������Ƿ���true��false
 * Ҳ���Ƿ���true����ֹ��һ���View���ܻ�ȡ�����¼����Ա��������¼���Ӱ��
 * 
 * 
 * onFling �� onScroll�¼�����
 *   һ�������¼��У��������μ�����
 *     onDwon onScroll onScroll onScroll onScroll onScroll onScroll onFling
 *   Ҳ����onScroll��ʹ����һ���������ƶ���Ҳ�ᱻ��μ�����onFling������������뿪ʱ��������
 *   �ְ�ס����ʱ������onScroll
 * 
 * @author Jacky
 * Date:2013-3-26
 *
 */
public class GestureListenerRunRecordItem implements OnGestureListener {

	private RunningRecordsActivity runningRecordsActivity;
	private CustomViewRunningRecord runningRecordItemView;
	private RunRecord runRecord;

	public GestureListenerRunRecordItem(RunningRecordsActivity runningRecordsActivity,
			CustomViewRunningRecord runningRecordItemView, RunRecord runRecord) {

		this.runningRecordsActivity = runningRecordsActivity;

		this.runningRecordItemView = runningRecordItemView;

		this.runRecord = runRecord;

	}

	// ����Ļ���϶��¼���velocityX��Y��ÿ���������
	// һ���϶��¼��������뿪ʱ������
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		return true;
	}

	// �����Ի�����ʾ�Ƿ�ɾ���ܲ���¼
	private void fireConfirmDialog() {

		AlertDialog.Builder builder = new Builder(runningRecordsActivity);

		builder.setTitle("ȷ��ɾ��������¼��");

		builder.setPositiveButton("ȡ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();

			}
		});

		builder.setNegativeButton("ȷ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				runningRecordsActivity.deleteRunRecord(runRecord);

				runningRecordItemView.setVisibility(View.GONE);

				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	// ����Ļ���϶��¼���distanceX��Y���ƶ��ĳ���
	// һ���϶��¼��У����������ܶ�Σ��ְ�ס����ʱ��������
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

		return true;
	}

	// ������ʱ��δ����������
	@Override
	public boolean onDown(MotionEvent e) {

		return true;
	}

	// ����¼�
	@Override
	public boolean onSingleTapUp(MotionEvent e) {

		return true;
	}

	// �����£���δ�ƶ����ɿ�ʱ
	// onShowPress��onDown����������  
	//    onDown�ǣ�һ�����������£������ϲ���onDown�¼�������onShowPress��onDown�¼�������  
	//    һ��ʱ���ڣ����û���ƶ����͵����¼�������Ϊ��onShowPress�¼��� 
	@Override
	public void onShowPress(MotionEvent e) {

	}

	// ����ʱ
	@Override
	public void onLongPress(MotionEvent e) {

		fireConfirmDialog();
	}

}
