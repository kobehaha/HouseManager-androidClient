package com.ma.photo.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.adpter.AlbumGridViewAdapter;
import com.ma.activity.ActivityAddHouseInfo;
import com.ma.photo.utl.AlbumHelper;
import com.ma.photo.utl.Bimp;
import com.ma.photo.utl.Image_bucket;
import com.ma.photo.utl.Image_item;
import com.ma.photo.utl.Public_way;
import com.special.ResideMenuDemo.R;

@SuppressLint("InlinedApi")
public class ActivityAlbumAddHouse extends Activity {
	// 显示手机里的所有图片的列表控件
	private GridView gridView;
	// 当手机里没有图片时，提示用户没有图片的控件
	private TextView tv;
	// gridView的adapter
	private AlbumGridViewAdapter gridImageAdapter;
	// 完成按钮
	private Button okButton;
	// 返回按钮
	private Button back;
	// 取消按钮
	private Button cancel;
	private Intent intent;
	// 预览按钮
	private Button preview;
	private Context mContext;
	private ArrayList<Image_item> dataList;
	private AlbumHelper helper;
	public static List<Image_bucket> contentList;
	public static Bitmap bitmap;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		setContentView(R.layout.plugincameraalbum);
		Public_way.activities.add(this);
		// 注册一个广播，这个广播主要是用于在GalleryActivity进行预览时，防止当所有图片都删除完后，再回到该页面时被取消选中的图片仍处于选中状态
		IntentFilter filter = new IntentFilter("data.broadcast.action");
		registerReceiver(broadcastReceiver, filter);
		bitmap = BitmapFactory.decodeResource(null,
				R.drawable.plugin_camera_no_pictures);
		mContext = ActivityAlbumAddHouse.this;
		init();
		initListener();
		// 这个函数主要用来控制预览和完成按钮的状态
		isShowOkBt();
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			gridImageAdapter.notifyDataSetChanged();
		}
	};

	// 预览按钮的监听
	private class PreviewListener implements OnClickListener {
		public void onClick(View v) {
			if (Bimp.mArrayList.size() > 0) {
				intent.putExtra("position", "1");
				intent.setClass(ActivityAlbumAddHouse.this,
						GalleryActivity.class);
				startActivity(intent);
			}
		}

	}

	// 完成按钮的监听
	private class AlbumSendListener implements OnClickListener {
		public void onClick(View v) {
			intent.setClass(mContext, ActivityAddHouseInfo.class);
			Bundle bundle = new Bundle();
			bundle.putInt("sure", 0);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();

		}
	}

	// 返回按钮监听
	private class BackListener implements OnClickListener {
		public void onClick(View v) {
			intent.setClass(ActivityAlbumAddHouse.this, ImageFile.class);

			Bundle bundle = new Bundle();
			bundle.putInt("sure", 0);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

	// 取消按钮的监听
	private class CancelListener implements OnClickListener {
		public void onClick(View v) {
			Bimp.mArrayList.clear();

			intent.setClass(mContext, ActivityAddHouseInfo.class);
			Bundle bundle = new Bundle();
			bundle.putInt("sure", 1);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

	// 初始化，给一些对象赋值
	private void init() {
		helper = AlbumHelper.getHelper();// 实力化albumhelper
		helper.init(getApplicationContext());

		contentList = helper.getImagesBucketList(false);
		dataList = new ArrayList<Image_item>();
		for (int i = 0; i < contentList.size(); i++) {
			dataList.addAll(contentList.get(i).imageList);
		}
		back = (Button) findViewById(R.id.back);
		cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(new CancelListener());
		back.setOnClickListener(new BackListener());
		preview = (Button) findViewById(R.id.preview);
		preview.setOnClickListener(new PreviewListener());
		intent = getIntent();
		// Bundle bundle = intent.getExtras();
		gridView = (GridView) findViewById(R.id.myGrid);
		gridImageAdapter = new AlbumGridViewAdapter(this, dataList,
				Bimp.mArrayList);
		gridView.setAdapter(gridImageAdapter);
		tv = (TextView) findViewById(R.id.myText);
		gridView.setEmptyView(tv);
		okButton = (Button) findViewById(R.id.ok_button);
		okButton.setText("完成" + "(" + Bimp.mArrayList.size() + "/"
				+ Public_way.num + ")");
	}

	private void initListener() {

		gridImageAdapter
				.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(final ToggleButton toggleButton,
							int position, boolean isChecked, Button chooseBt) {
						if (Bimp.mArrayList.size() >= Public_way.num) {
							toggleButton.setChecked(false);
							chooseBt.setVisibility(View.GONE);
							if (!removeOneData(dataList.get(position))) {
								Toast.makeText(ActivityAlbumAddHouse.this,
										R.string.only_choose_num, 200).show();
							}
							return;
						}
						if (isChecked) {
							chooseBt.setVisibility(View.VISIBLE);
							Bimp.mArrayList.add(dataList.get(position));
							okButton.setText("完成" + "("
									+ Bimp.mArrayList.size() + "/"
									+ Public_way.num + ")");
						} else {
							Bimp.mArrayList.remove(dataList.get(position));
							chooseBt.setVisibility(View.GONE);
							okButton.setText("完成" + "("
									+ Bimp.mArrayList.size() + "/"
									+ Public_way.num + ")");
						}
						isShowOkBt();
					}
				});

		okButton.setOnClickListener(new AlbumSendListener());

	}

	private boolean removeOneData(Image_item imageItem) {
		if (Bimp.mArrayList.contains(imageItem)) {
			Bimp.mArrayList.remove(imageItem);
			okButton.setText("完成" + "(" + Bimp.mArrayList.size() + "/"
					+ Public_way.num + ")");
			return true;
		}
		return false;
	}

	public void isShowOkBt() {
		if (Bimp.mArrayList.size() > 0) {
			okButton.setText("完成" + "(" + Bimp.mArrayList.size() + "/"
					+ Public_way.num + ")");
			preview.setPressed(true);
			okButton.setPressed(true);
			preview.setClickable(true);
			okButton.setClickable(true);
			okButton.setTextColor(Color.WHITE);
			preview.setTextColor(Color.WHITE);
		} else {
			okButton.setText("完成" + "(" + Bimp.mArrayList.size() + "/"
					+ Public_way.num + ")");
			preview.setPressed(false);
			preview.setClickable(false);
			okButton.setPressed(false);
			okButton.setClickable(false);
			okButton.setTextColor(Color.parseColor("#E1E0DE"));
			preview.setTextColor(Color.parseColor("#E1E0DE"));
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			intent.setClass(ActivityAlbumAddHouse.this, ImageFile.class);
			startActivity(intent);
		}
		return false;

	}

	@Override
	protected void onRestart() {
		isShowOkBt();
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(broadcastReceiver);
		super.onDestroy();
	}

}
