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
 * 代表一个场地记录Item（使用graphic方式渲染）
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

		// 设置本Item的宽度，高度
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 182));

		paint = new Paint();

		gestureDetector = new GestureDetector(playingFieldsActivity,
				new GestureListenerPlayingFieldItem(playingFieldsActivity,
						playingField));

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
		paint.setARGB(255, 60, 201, 255);
		canvas.drawRect(0, 0, 75, 140, paint);

		// 浙
		if (playingField.name.length() > 0) {
			paint.setTextSize(53);
			paint.setARGB(255, 255, 255, 255);
			paint.setFakeBoldText(true);
			canvas.drawText(playingField.name.substring(0, 1), 12, 88, paint);
		}

		// 浙江工商大学
		paint.setTextSize(55);
		paint.setFakeBoldText(false);
		paint.setARGB(255, 60, 201, 255);
		canvas.drawText(playingField.name, 95, 60, paint);

		// 5次
		paint.setTextAlign(Align.RIGHT);
		paint.setFakeBoldText(true);
		canvas.drawText(playingField.runCount + "次", 620, 60, paint);

		// 共跑8488米
		paint.setTextSize(37);
		paint.setFakeBoldText(false);
		paint.setTextAlign(Align.LEFT);
		canvas.drawText("共跑" + playingField.lengthSum + "米", 95, 125, paint);

		// 最近：2012-12-08
		paint.setTextAlign(Align.RIGHT);
		canvas.drawText(
				"最近："
						+ MyUtility.DF_yyyy_MM_dd
								.format(playingField.lastRunDate), 620, 125,
				paint);

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
