package cn.hartech.jrunner.ui.listener;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import cn.hartech.jrunner.FieldEditorActivity;
import cn.hartech.jrunner.PlayingFieldsActivity;
import cn.hartech.jrunner.po.PlayingField;

/**
 * OK，在手机App窗口上，是一个Activity或View叠加着另一个View，有多层。
 * 当手指在某个Activity/View上触发、滑动时，将会触发最上层View的onXXXX方法。
 * 如果该方法处理完后返回true，则表示该事件已处理完。
 * 如果返回false，则处理完后该事件仍会传递到其同位置的下一层Activity/View，触发器onXXXX方法。
 * 
 * 
 * 而本类以下各个事件如果满足条件，将会*同时*被激发。
 * 拖拉时，按下 事件与 拖拉 事件将会同时被调用，不管它们返回true或false
 * 也就是返回true是阻止下一层的View不能获取到本事件，对本类其他事件不影响
 * 
 * 
 * onFling 与 onScroll事件区别：
 *   一个拖拉事件中，将会依次激发：
 *     onDwon onScroll onScroll onScroll onScroll onScroll onScroll onFling
 *   也就是onScroll即使是在一个像素内移动，也会被多次激发，onFling仅会在最后手离开时被激发。
 *   手按住不动时不激发onScroll
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

	// 长按时
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

	// 在屏幕上拖动事件，velocityX、Y是每秒的像素数
	// 一个拖动事件，在手离开时被激发
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		return true;
	}

	// 在屏幕上拖动事件，distanceX、Y是移动的长度
	// 一个拖动事件中，将被激发很多次，手按住不动时不激发。
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

		return true;
	}

	// 轻击事件
	@Override
	public boolean onSingleTapUp(MotionEvent e) {

		return true;
	}

	// 当按下，其未移动、松开时
	// onShowPress和onDown的区别在于  
	//    onDown是，一旦触摸屏按下，就马上产生onDown事件，但是onShowPress是onDown事件产生后，  
	//    一段时间内，如果没有移动鼠标和弹起事件，就认为是onShowPress事件。 
	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onDown(MotionEvent e) {

		return true;
	}

}
