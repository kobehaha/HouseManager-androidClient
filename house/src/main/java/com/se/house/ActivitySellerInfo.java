package com.se.house;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beans.Seller;
import com.ma.activity.ActivityShowPicture;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.special.ResideMenuDemo.R;

@SuppressLint("CommitPrefEdits")
public class ActivitySellerInfo extends Activity {
	public static SharedPreferences sharedPreferences;
	public static ImageLoader imageLoader2 = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private TextView tv_name, tv_sex, tv_time, tv_age, tv_phone, tv_degree,
			tv_weichart;
	private Button btn_modify;
	private ImageView img_ico, img_back;
	private Seller seller;
	private com.se.inter.use.SellerInterface getInfo;
	private String url = "";

	public final static String config = "share_config_seller_info";
	public Object string;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		setContentView(R.layout.seactivitysellerinfo);
		init();
		get_picture_config();
		new AsynGetSellerInfo().execute();// 加载资料

	}

	@SuppressWarnings("deprecation")
	private void get_picture_config() {
		File file = StorageUtils.getOwnCacheDirectory(ActivitySellerInfo.this,
				"imageloader/Cache");
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				ActivitySellerInfo.this).threadPoolSize(1)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCache(new UnlimitedDiskCache(file))
				.denyCacheImageMultipleSizesInMemory().build();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageOnFail(R.drawable.ic_error)
				.showImageForEmptyUri(R.drawable.ic_empty).cacheOnDisc(true)
				.displayer(new RoundedBitmapDisplayer(200)).cacheInMemory(true)
				.considerExifParams(true).build();
		imageLoader2.init(configuration);

	}

	private void init() {
		getInfo = new com.se.service.ServiceSeller();// 获取服务
		tv_weichart = (TextView) findViewById(R.id.tv_se_info_weichart);
		tv_degree = (TextView) findViewById(R.id.tv_se_info_degree);
		tv_age = (TextView) findViewById(R.id.tv_se_info_age);
		tv_sex = (TextView) findViewById(R.id.tv_se_info_sex);
		tv_time = (TextView) findViewById(R.id.tv_se_info_time);
		tv_name = (TextView) findViewById(R.id.tv_se_info_name);
		tv_phone = (TextView) findViewById(R.id.tv_se_info_phone);
		tv_sex = (TextView) findViewById(R.id.tv_se_info_sex);
		btn_modify = (Button) findViewById(R.id.btn_se_info_modify);
		img_back = (ImageView) findViewById(R.id.img_se_info_back2);
		img_ico = (ImageView) findViewById(R.id.img_se_info_icon);
		img_ico.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ActivitySellerInfo.this,
						ActivityShowPicture.class);
				Bundle bundle = new Bundle();
				bundle.putString("url", url);

				intent.putExtras(bundle);
				Log.v("管理员信息传递的uri", url);
				startActivity(intent);

			}
		});
		img_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});
		btn_modify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(ActivitySellerInfo.this,
						ActivityModifySellerInfo.class));// 切换activity

			}
		});

	}

	private class AsynGetSellerInfo extends AsyncTask<Void, Void, Integer> {
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (result == 1) {

				imageLoader2.displayImage(url, img_ico, options);
				tv_age.setText(String.valueOf(seller));
				tv_name.setText(seller.getName());
				tv_phone.setText(String.valueOf(seller));
				tv_time.setText(seller.getTime());
				tv_sex.setText(seller.getSex());
				tv_degree.setText(seller.getDegree());
				tv_weichart.setText(seller.getWeichart());
				imageLoader2.displayImage(seller.getUrl(), img_ico, options);

				save_data(seller.getAge(), seller.getPhone(), seller.getSex(),
						seller.getTime(), seller.getName(), seller.getDegree(),
						seller.getWeichart());
				Toast.makeText(ActivitySellerInfo.this, "获取资料成功",
						Toast.LENGTH_SHORT).show();

			} else {
				get_dat();// 获取本地资料
				Toast.makeText(ActivitySellerInfo.this, "网络获取资料失败，请检查网络",
						Toast.LENGTH_SHORT).show();

			}
		}

		@Override
		protected Integer doInBackground(Void... arg0) {
			int result = 0;
			try {
				seller = getInfo.getSellerInfo();
				Log.v("seller     ", seller.toString());
				url = seller.getUrl();// 获取url
				result = 1;
			} catch (NullPointerException e) {
				e.printStackTrace();
				return result;

			} catch (Exception e) {
			}
			return result;

		}
	}

	public void save_data(int age, int phone, java.lang.String sex,
			java.lang.String time, java.lang.String name, String degree,
			String weichart) {
		sharedPreferences = getSharedPreferences(config, Activity.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString("sex", sex);
		editor.putString("time", time);
		editor.putString("name", name);
		editor.putInt("age", age);
		editor.putInt("phone", phone);
		editor.putString("degree", degree);
		editor.putString("weichart", weichart);
		editor.commit();
	}

	public void get_dat() {
		SharedPreferences sharedPreferences = getSharedPreferences(config,
				Activity.MODE_PRIVATE);
		tv_degree.setText(sharedPreferences.getString("degree", "未保存过信息"));
		tv_age.setText(String.valueOf((sharedPreferences.getInt("age", 0))));
		tv_name.setText(sharedPreferences.getString("name", "未保存过数据"));
		tv_phone.setText(String.valueOf(sharedPreferences.getInt("phone", 0)));
		tv_sex.setText(sharedPreferences.getString("sex", "未保存过数据"));
		tv_time.setText(sharedPreferences.getString("time", "未保存过数据"));
		tv_weichart.setText(sharedPreferences.getString("weichart", "未保存数据"));

	}
}
