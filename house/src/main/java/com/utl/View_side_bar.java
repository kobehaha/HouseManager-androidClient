package com.utl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.special.ResideMenuDemo.R;

public class View_side_bar extends View {// 自定义View的监听器，自定义设置，触摸分发事件
	private OnTouchingLetterChangedListener onChangedListner;
	private Paint paint = new Paint();
	private static String[] b = { "↑", "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"U", "V", "W", "X", "Y", "Z", "#" };
	int choose = -1;
	private TextView mTextView;

	public void setText(TextView textView) {
		this.mTextView = textView;
	}

	public View_side_bar(Context context) {
		super(context);
	}

	public View_side_bar(Context context, AttributeSet attribute) {
		super(context, attribute);
	}

	public View_side_bar(Context context, AttributeSet attributeSet, int defStyle) {
		super(context, attributeSet, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = getHeight();
		int width = getWidth();
		int single = height / b.length;
		for (int i = 0; i < b.length; i++) {
			paint.setColor(Color.BLACK);
			paint.setAntiAlias(true);
			paint.setTextSize(40);
			if (i == choose) {
				paint.setColor(Color.parseColor("#3399ff"));

			}
			float x = width / 2 - paint.measureText(b[i]) / 2;// 设置宽度
			float y = single * i + single;// 画图高度
			canvas.drawText(b[i], x, y, paint);
			paint.reset();

		}

	}

	// ViewGroup的dispatchTouchEvent是真正在执行“分发”工作，
	// 而View的dispatchTouchEvent方法，并不执行分发工作，或者说它
	// 分发的对象就是自己，决定是否把touch事件交给自己处理，
	// 而处理的方法，便是onTouchEvent事件

	@SuppressWarnings("deprecation")
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();
		final int oldchoose = choose;
		final OnTouchingLetterChangedListener listener = onChangedListner;
		final int c = (int) (y / getHeight() * b.length);

		switch (action) {
		case MotionEvent.ACTION_UP:
			setBackgroundDrawable(new ColorDrawable(0x00000000));// 默认背景
			choose = -1;//
			invalidate();
			if (mTextView != null) {
				mTextView.setVisibility(View.INVISIBLE);
			}

			break;

		default:
			setBackgroundResource(R.drawable.sidebar_background);
			if (c != oldchoose && c < b.length) {
				if (listener != null) {
					listener.onTouchingLetterChanged(b[c]);
				}
				if (mTextView != null) {
					mTextView.setText(b[c]);
					mTextView.setVisibility(View.VISIBLE);
				}

				choose = c;
				invalidate();// 重新绘制

			}
			break;
		}
		return super.dispatchTouchEvent(event);
	}

	/*
	 * View 设置监听器
	 */
	public void setOnTouchingLetterChangedListner(
			OnTouchingLetterChangedListener touch) {
		this.onChangedListner = touch;
	}

	/*
	 * 接口
	 */
	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}

}
