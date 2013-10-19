package cn.hartech.jrunner.ui.listener;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import cn.hartech.jrunner.FieldEditorActivity;
import cn.hartech.jrunner.PlayingFieldsActivity;
import cn.hartech.jrunner.po.PlayingField;

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
public class GestureListenerPlayingFieldItem implements OnGestureListener {

	private PlayingFieldsActivity playingFieldsActivity;

	private PlayingField playingField;

	public GestureListenerPlayingFieldItem(
			PlayingFieldsActivity playingFieldsActivity,
			PlayingField playingField) {

		this.playingFieldsActivity = playingFieldsActivity;

		this.playingField = playingField;

	}

	// ����ʱ
	@Override
	public void onLongPress(MotionEvent e) {

		Intent intent = new Intent(playingFieldsActivity,
				FieldEditorActivity.class);

		Bundle bundle = new Bundle();
		bundle.putLong(FieldEditorActivity.BUNDLE_KEY_PLAYING_FIELD_ID,
				playingField._id);
		intent.putExtras(bundle);

		playingFieldsActivity.startActivity(intent);

	}

	// ����Ļ���϶��¼���velocityX��Y��ÿ���������
	// һ���϶��¼��������뿪ʱ������
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		return true;
	}

	// ����Ļ���϶��¼���distanceX��Y���ƶ��ĳ���
	// һ���϶��¼��У����������ܶ�Σ��ְ�ס����ʱ��������
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

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

	@Override
	public boolean onDown(MotionEvent e) {

		return true;
	}

}
