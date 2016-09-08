package com.ma.activity;

import java.io.File;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.beans.Manager;
import com.ma.interfac.ManagerInterface;
import com.ma.service.ServiceManager;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.se.house.BasicActivity;
import com.special.ResideMenuDemo.R;
import com.utl.Url;

public class ActivityManagerInfo extends BasicActivity {
	public static final String CONFIG = "ma_info_config";
	private ImageLoader imageLoader = ImageLoader.getInstance();// 图片加载类
	private DisplayImageOptions options;// 图片加载设置类
	private Manager manager;
	private ManagerInterface getInfo;
	private ImageView m_ico, m_back;
	private TextView name, sex, age, phone, time;
	private String sname, ssex, stime, url;
	private int sage, sphone;
	private Button btn_modify;

	@TargetApi(19)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// 先从网络上获取

		setContentView(R.layout.maactivitymainfo);
		init();
		config();
		new AsynGetManagerInfo().execute();

	}

	public void SaveInfo(String url, String name, String sex, int age,
			int phone, String time) {// 保存seller_id,password

		SharedPreferences sp = ActivityManagerInfo.this.getSharedPreferences(
				CONFIG, MODE_PRIVATE);
		SharedPreferences.Editor edit = sp.edit();
		edit.putString("url", url);
		System.out.println("保存的url" + url);
		edit.putString("name", name);
		edit.putInt("age", age);
		edit.putInt("phone", phone);// 保存信息
		edit.putString("time", time);
		edit.putString("sex", sex);

		edit.commit();
	}

	@SuppressWarnings("deprecation")
	private void config() {// 配置文件

		File cacheDir = StorageUtils.getOwnCacheDirectory(// 设置缓存
				ActivityManagerInfo.this, "imageloader/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(// 配置文件
				ActivityManagerInfo.this)
				.threadPoolSize(1)
				.// 线程池大小
				threadPriority(1)
				.// 线程优先级
				tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCache(new UnlimitedDiskCache(cacheDir))
				.denyCacheImageMultipleSizesInMemory().build();

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageOnFail(R.drawable.ic_error)
				.showImageForEmptyUri(R.drawable.ic_empty).cacheOnDisc(true)
				// 设置缓存到sd，内存中
				.displayer(new RoundedBitmapDisplayer(200)).cacheInMemory(true)
				.considerExifParams(true).build();// 设置不同形态的图片
		imageLoader.init(config);

	}

	private void init() {
		getInfo = new ServiceManager();// 实力化该类
		m_back = (ImageView) findViewById(R.id.img_ma_info_back);
		m_ico = (ImageView) findViewById(R.id.img_ma_info_icon);
		name = (TextView) findViewById(R.id.tv_ma_info_name);
		sex = (TextView) findViewById(R.id.tv_ma_info_sex);
		age = (TextView) findViewById(R.id.tv_ma_info_age);
		phone = (TextView) findViewById(R.id.tv_ma_info_phone);
		time = (TextView) findViewById(R.id.tv_ma_info_time);
		btn_modify = (Button) findViewById(R.id.btn_ma_info_modify);

		m_ico.setOnClickListener(new OnClickListener() {// 加载大图片

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ActivityManagerInfo.this,
						ActivityShowPicture.class);
				Bundle bundle = new Bundle();
				bundle.putString("url", url);
				intent.putExtras(bundle);
				Log.v("管理员信息传递的uri", url);
				startActivity(intent);

			}
		});

		// m_ico.setImageBitmap(bim);

		btn_modify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(ActivityManagerInfo.this,
						ActivityModifyManagerInfo.class));

			}
		});
		m_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});

	}

	public class AsynGetManagerInfo extends AsyncTask<Void, Void, Integer> {
		@Override
		protected void onPostExecute(Integer result) {
			if (result == 0) {
				getMaInfo(ActivityManagerInfo.this);
				setInfo();
				Toast.makeText(ActivityManagerInfo.this, "获取资料失败",
						Toast.LENGTH_SHORT).show();

			} else {
				name.setText(manager.getName());
				sex.setText(manager.getSex());
				age.setText(String.valueOf(manager.getAge()));
				phone.setText(String.valueOf(manager.getPhone()));
				time.setText(manager.getTime());
				imageLoader.displayImage(url, m_ico, options);
				Toast.makeText(ActivityManagerInfo.this, "获取资料成功",
						Toast.LENGTH_SHORT).show();

			}
			super.onPostExecute(result);
		}

		@Override
		protected Integer doInBackground(Void... arg0) {
			int result = 0;
			try {
				manager = getInfo.get_manager();
				sage = manager.getAge();
				ssex = manager.getSex();
				sphone = manager.getPhone();
				sname = manager.getName();
				stime = manager.getTime();
				url = "http://" + Url.url + File.separator
						+ manager.getUrl().replaceAll("#", "");

				Log.v("加载图片信息的url=", url);

				SaveInfo(url, sname, ssex, sage, sphone, stime);
				result = 1;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return result;
		}
	}

	@Override
	protected void onRestart() {// restart更新数据
		setInfo();
		super.onRestart();
	}

	private void setInfo() {
		name.setText(sname);
		sex.setText(ssex);
		age.setText(String.valueOf(sage));
		phone.setText(String.valueOf(sphone));
		time.setText(stime);

	}

	private void getMaInfo(Context context) {
		// 从存储的shared中获取出来如果存在
		SharedPreferences sp = context.getSharedPreferences(CONFIG,
				Context.MODE_PRIVATE);
		// mpath = sp.getString("path",
		// String.valueOf(R.drawable.head_icon_user));
		sname = sp.getString("name", "未添加资料");
		stime = sp.getString("time", "未添加资料");
		sage = sp.getInt("age", 0);
		sphone = sp.getInt("phone", 000);
		ssex = sp.getString("sex", "未添加资料");

	}

}
