package cn.hartech.jrunner.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import cn.hartech.jrunner.PlayingFieldsActivity;
import cn.hartech.jrunner.po.PlayingField;
import cn.hartech.jrunner.ui.listener.GestureListenerPlayingFieldItem;
import cn.hartech.jrunner.util.MyUtility;

/**
 * ����һ�����ؼ�¼Item��ʹ��graphic��ʽ��Ⱦ��
 * 
 * @author Jacky
 * Date:2013-4-16
 *
 */
public class CustomViewPlayingField extends View {

	private PlayingField playingField;

	private Paint paint;

	private GestureDetector gestureDetector;

	public CustomViewPlayingField(Context context) {

		super(context);
	}

	public CustomViewPlayingField(PlayingFieldsActivity playingFieldsActivity,
			PlayingField playingField) {

		super(playingFieldsActivity);

		this.playingField = playingField;

		// ���ñ�Item�Ŀ�ȣ��߶�
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 182));

		paint = new Paint();

		gestureDetector = new GestureDetector(playingFieldsActivity,
				new GestureListenerPlayingFieldItem(playingFieldsActivity,
						playingField));

	}

	/**
	 * ���ڱ�����Ƕ���������ÿ��������Ⱦ
	 * 
	 * �������������ڽ��ڴ���ʱ������һ�Σ������浽bitmap������
	 * �ڹ��������л���ȥ�ٽ���ʱ����������Ⱦ
	 * 
	 * ʹ�ò��ÿ���Ч������
	 */
	@Override
	public void onDraw(Canvas canvas) {

		super.onDraw(canvas);

		// ����ߵĿ��
		paint.setARGB(255, 60, 201, 255);
		canvas.drawRect(0, 0, 75, 140, paint);

		// ��
		if (playingField.name.length() > 0) {
			paint.setTextSize(53);
			paint.setARGB(255, 255, 255, 255);
			paint.setFakeBoldText(true);
			canvas.drawText(playingField.name.substring(0, 1), 12, 88, paint);
		}

		// �㽭���̴�ѧ
		paint.setTextSize(55);
		paint.setFakeBoldText(false);
		paint.setARGB(255, 60, 201, 255);
		canvas.drawText(playingField.name, 95, 60, paint);

		// 5��
		paint.setTextAlign(Align.RIGHT);
		paint.setFakeBoldText(true);
		canvas.drawText(playingField.runCount + "��", 620, 60, paint);

		// ����8488��
		paint.setTextSize(37);
		paint.setFakeBoldText(false);
		paint.setTextAlign(Align.LEFT);
		canvas.drawText("����" + playingField.lengthSum + "��", 95, 125, paint);

		// �����2012-12-08
		paint.setTextAlign(Align.RIGHT);
		canvas.drawText(
				"�����"
						+ MyUtility.DF_yyyy_MM_dd
								.format(playingField.lastRunDate), 620, 125,
				paint);

	}

	/**
	 * ÿ��������ƶ�һ΢С������δ��������һ�������ڣ��������һ�α�������
	 * ������϶���һ�������ڣ��������������ö�Ρ�
	 * 
	 * 
	 * �Ѹ��¼����ݸ����ǳ�ʼ���õ����ƴ�����
	 * Ϊ�˸��ḻ�Ĵ��������¼�����ʹ���Զ���Gesture��������������¼�
	 * 
	 */
	@Override
	public boolean onTouchEvent(MotionEvent me) {

		return gestureDetector.onTouchEvent(me);
	}

}
