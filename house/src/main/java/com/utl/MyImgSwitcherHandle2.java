package com.utl;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.se.house.ActivitySellerDeal;
import com.se.house.ActivitySellerDeal.Image_adapter2;
import com.special.ResideMenuDemo.R;

public class MyImgSwitcherHandle2 implements OnTouchListener,
		OnItemClickListener {
	private ActivitySellerDeal context; // 记录传入的对象
	private String[] picture_url;
	private Drawable[] drawables;
	private Image_adapter2 adapter;

	private int position = 0; // 当前显示的图片的下标
	private int downX = 0; // 滑动切换图片时，按下时的X坐标
	private int upX = 0; // 滑动切换图片时，抬起时的X坐标

	public MyImgSwitcherHandle2(ActivitySellerDeal asny_get_info,
			String[] picture_url, Drawable[] drawables, Image_adapter2 adapter2) {
		this.context = asny_get_info;
		this.picture_url = picture_url;
		this.drawables = drawables;
		this.adapter = adapter2;

		load_first();

	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void load_first() {

		((ActivitySellerDeal) context).getImgSwitcher().setBackgroundDrawable(
				drawables[drawables.length / 2]);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getX(); // 获取按下时的X坐标
			break;

		case MotionEvent.ACTION_UP:
			upX = (int) event.getX();
			if (downX > upX) {

				if (position >= picture_url.length - 1) {
					Toast.makeText(context, "最后一张", Toast.LENGTH_SHORT).show();
				} else {
					// 设置左划动画
					((ActivitySellerDeal) context).getImgSwitcher()
							.setInAnimation(
									AnimationUtils.loadAnimation(context,
											R.anim.right_in));
					((ActivitySellerDeal) context).getImgSwitcher()
							.setOutAnimation(
									AnimationUtils.loadAnimation(context,
											R.anim.left_out));

					position++;
					showPhoto();
				}
			} else if (downX < upX) {

				if (position <= 0) {
					Toast.makeText(context, "第一张", Toast.LENGTH_SHORT).show();
				} else {
					// 设置右划动画
					((ActivitySellerDeal) context).getImgSwitcher()
							.setInAnimation(
									AnimationUtils.loadAnimation(context,
											R.anim.left_in));
					((ActivitySellerDeal) context).getImgSwitcher()
							.setOutAnimation(
									AnimationUtils.loadAnimation(context,
											R.anim.right_out));

					position--;
					showPhoto();
				}
			}
			break;
		default:
			break;
		}

		return true;
	}

	@SuppressLint("NewApi")
	public void showPhoto() {

		((ActivitySellerDeal) context).getImgSwitcher().setBackground(
				drawables[position]);

		((ActivitySellerDeal) context).getGallery().setSelection(position);
	}

	@SuppressLint("NewApi")
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		if (this.position < position) {

			// 设置左划动画
			((ActivitySellerDeal) context).getImgSwitcher().setInAnimation(
					AnimationUtils.loadAnimation(context, R.anim.right_in));
			((ActivitySellerDeal) context).getImgSwitcher().setOutAnimation(
					AnimationUtils.loadAnimation(context, R.anim.left_out));

			((ActivitySellerDeal) context).getImgSwitcher().setBackground(
					drawables[position]);

			this.position = position;
			adapter.notifyDataSetChanged();

		} else if (this.position > position) {

			// 设置右划动画
			((ActivitySellerDeal) context).getImgSwitcher().setInAnimation(
					AnimationUtils.loadAnimation(context, R.anim.left_in));
			((ActivitySellerDeal) context).getImgSwitcher().setOutAnimation(
					AnimationUtils.loadAnimation(context, R.anim.right_out));

			((ActivitySellerDeal) context).getImgSwitcher().setBackground(
					drawables[position]);

			this.position = position;
			adapter.notifyDataSetChanged();
		}

	}
}
