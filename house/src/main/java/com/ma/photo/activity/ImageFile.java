package com.ma.photo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.adpter.FolderAdapter;
import com.ma.activity.ActivityAddHouseInfo;
import com.ma.photo.utl.Bimp;
import com.ma.photo.utl.Public_way;
import com.special.ResideMenuDemo.R;

/**
 * 这个类主要是用来进行显示包含图片的文件夹
 * 
 */
public class ImageFile extends Activity {

	private FolderAdapter folderAdapter;
	private Button bt_cancel;
	private Context mContext;

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
		setContentView(R.layout.plugincameraimagefile);

		Public_way.activities.add(this);
		mContext = this;
		bt_cancel = (Button) findViewById(R.id.cancel);
		bt_cancel.setOnClickListener(new CancelListener());
		GridView gridView = (GridView) findViewById(R.id.fileGridView);
		TextView textView = (TextView) findViewById(R.id.heand_kobe);
		textView.setText("相册");
		folderAdapter = new FolderAdapter(this);
		gridView.setAdapter(folderAdapter);
	}

	private class CancelListener implements OnClickListener {// 取消按钮的监听
		public void onClick(View v) {
			// 清空选择的图片
			Bimp.mArrayList.clear();
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putInt("sure", 0);
			intent.putExtras(bundle);
			intent.setClass(mContext, ActivityAddHouseInfo.class);
			startActivity(intent);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent2 = new Intent();
			Bundle bundle = new Bundle();
			bundle.putInt("sure", 0);
			intent2.putExtras(bundle);
			intent2.setClass(mContext, ActivityAddHouseInfo.class);
			startActivity(intent2);
		}

		return true;
	}

}
