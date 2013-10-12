package cn.hartech.jrunner.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import cn.hartech.jrunner.RunningRecordsActivity;
import cn.hartech.jrunner.po.RunRecord;
import cn.hartech.jrunner.ui.listener.GestureListenerRunRecordItem;
import cn.hartech.jrunner.util.MyUtility;

/**
 * ����һ���ܲ���¼����Item��ʹ��graphic��ʽ��Ⱦ��
 * 
 * @author Jacky
 * Date:2013-4-14
 *
 */
public class CustomViewRunningRecord extends View {

	private RunRecord runRecord;

	private Paint paint;

	private GestureDetector gestureDetector;

	public CustomViewRunningRecord(Context context) {

		super(context);
	}

	public CustomViewRunningRecord(RunningRecordsActivity context,
			RunRecord runRecord) {

		super(context);

		this.runRecord = runRecord;

		// ���ñ�Item�Ŀ�ȣ��߶�
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 175));

		paint = new Paint();

		gestureDetector = new GestureDetector(context,
				new GestureListenerRunRecordItem(context, this, runRecord));

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
		paint.setARGB(255, 255, 94, 162);
		canvas.drawRect(0, 0, 75, 140, paint);

		// ��
		paint.setTextSize(53);
		paint.setARGB(255, 255, 255, 255);
		paint.setFakeBoldText(true);
		canvas.drawText(MyUtility.getWeekChineseWeekDay(runRecord.startTime),
				12, 86, paint);

		// �㽭���̴�ѧ
		paint.setTextSize(50);
		paint.setARGB(255, 255, 94, 162);
		paint.setFakeBoldText(false);
		if (runRecord.currentPlayingField == null) {
			canvas.drawText("һ���ǻ���", 90, 53, paint);
		} else {
			canvas.drawText(runRecord.currentPlayingField.name, 90, 53, paint);
		}

		// 2088��
		canvas.drawText((int) runRecord.length + "��", 90, 124, paint);

		// 5��32��
		paint.setTextSize(35);
		paint.setTextAlign(Align.RIGHT);
		canvas.drawText(MyUtility.getMinuteAndSecond(runRecord.duration), 620,
				35, paint);

		// 4.8��/��
		canvas.drawText(MyUtility.setScale(runRecord.speedAvg, 1) + "��/��", 620,
				85, paint);

		// 2013-04-05
		paint.setTextSize(40);
		canvas.drawText(MyUtility.DF_yyyy_MM_dd.format(runRecord.startTime),
				620, 135, paint);

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
