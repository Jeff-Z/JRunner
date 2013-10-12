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
 * 代表一个跑步记录界面Item（使用graphic方式渲染）
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

		// 设置本Item的宽度，高度
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 175));

		paint = new Paint();

		gestureDetector = new GestureDetector(context,
				new GestureListenerRunRecordItem(context, this, runRecord));

	}

	/**
	 * 由于本框框不是动画，无需每次重新渲染
	 * 
	 * 所以下面代码仅在仅在创建时被调用一次，并保存到bitmap对象中
	 * 在滚动，在切换出去再进来时，均不再渲染
	 * 
	 * 使用不用考虑效率问题
	 */
	@Override
	public void onDraw(Canvas canvas) {

		super.onDraw(canvas);

		// 最左边的框框
		paint.setARGB(255, 255, 94, 162);
		canvas.drawRect(0, 0, 75, 140, paint);

		// 日
		paint.setTextSize(53);
		paint.setARGB(255, 255, 255, 255);
		paint.setFakeBoldText(true);
		canvas.drawText(MyUtility.getWeekChineseWeekDay(runRecord.startTime),
				12, 86, paint);

		// 浙江工商大学
		paint.setTextSize(50);
		paint.setARGB(255, 255, 94, 162);
		paint.setFakeBoldText(false);
		if (runRecord.currentPlayingField == null) {
			canvas.drawText("一定是火星", 90, 53, paint);
		} else {
			canvas.drawText(runRecord.currentPlayingField.name, 90, 53, paint);
		}

		// 2088米
		canvas.drawText((int) runRecord.length + "米", 90, 124, paint);

		// 5分32秒
		paint.setTextSize(35);
		paint.setTextAlign(Align.RIGHT);
		canvas.drawText(MyUtility.getMinuteAndSecond(runRecord.duration), 620,
				35, paint);

		// 4.8米/秒
		canvas.drawText(MyUtility.setScale(runRecord.speedAvg, 1) + "米/秒", 620,
				85, paint);

		// 2013-04-05
		paint.setTextSize(40);
		canvas.drawText(MyUtility.DF_yyyy_MM_dd.format(runRecord.startTime),
				620, 135, paint);

	}

	/**
	 * 每点击、或移动一微小步（手未起来）（一个像素内）都会调用一次本方法。
	 * 即如果拖动在一个像素内，本方法都被调用多次。
	 * 
	 * 
	 * 把该事件传递给我们初始化好的手势处理器
	 * 为了更丰富的处理本手势事件，将使用自定义Gesture监听器来处理该事件
	 * 
	 */
	@Override
	public boolean onTouchEvent(MotionEvent me) {

		return gestureDetector.onTouchEvent(me);
	}

}
