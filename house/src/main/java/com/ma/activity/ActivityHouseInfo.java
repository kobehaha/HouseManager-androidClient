package com.ma.activity;

import java.io.File;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.beans.House;
import com.ma.interfac.OwnerInterface;
import com.ma.service.ServiceOwner;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.se.house.BasicActivity;
import com.special.ResideMenuDemo.R;
import com.utl.MyImgSwitcherHandle;

public class ActivityHouseInfo extends BasicActivity{
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageSwitcher mImageSwitcher;
	private DisplayImageOptions options_cache;
	private Gallery mGallery;
	private TextView tv_house_brief, tv_size, tv_price, tv_owner_info;
	private String str_house_name, str_house_location, str_house_brief,
			str_owner_name, str_owner_weichart, str_owner_sex;
	private int house_price, house_size, house_year, owner_phone, owner_year,
			house_id;
	private String[] str_picture;
	private House mHouse;
	private Image_adapter adapter;
	private MyImgSwitcherHandle handle;
	private Drawable[] drawables;
	private OwnerInterface getOwner = new ServiceOwner();
	private com.beans.Owner owner;

	@TargetApi(19)
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.maactivityhouseinfo);
		get_intent();
		config();

		init();
		new Asny_get_info().execute();

	}

	@SuppressWarnings("deprecation")
	private void config() {// 配置文件

		File cacheDir = StorageUtils.getOwnCacheDirectory(
				ActivityHouseInfo.this, "imageloader/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				ActivityHouseInfo.this)
				.threadPoolSize(4)
				.// 线程池大小
				threadPriority(Thread.NORM_PRIORITY - 2)
				.// 线程优先级
				tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCache(new UnlimitedDiskCache(cacheDir))
				.denyCacheImageMultipleSizesInMemory().build();

		// 设置缓存到sd，内存中
		options_cache = new DisplayImageOptions.Builder().cacheOnDisc(true)
				.cacheInMemory(true).build();// 设置不同形态的图片
		imageLoader.init(config);

	}

	private void get_intent() {
		Bundle bundle = getIntent().getExtras();
		mHouse = (House) bundle.getSerializable("house");// 获取序列化的对象
		str_picture = mHouse.getPicture_url();
		drawables = new Drawable[str_picture.length];
		str_house_brief = mHouse.getBrief();
		str_house_location = mHouse.getLocation();
		str_house_name = mHouse.getName();
		house_price = mHouse.getPrice();
		house_size = mHouse.getSize();
		house_year = mHouse.getYear();
		house_id = mHouse.getId();
		System.out.println("选中房源传过来的数据是 " + "房源id=" + house_id + " 房源年限="
				+ house_year + "  房源价格=" + house_price + "  房源名称是="
				+ str_house_name + "房源简介是=" + String.valueOf(str_house_brief)
				+ "  房源图片url＝" + String.valueOf(str_picture[0]));

	}

	@SuppressWarnings("deprecation")
	private void init() {
		tv_house_brief = (TextView) findViewById(R.id.tv_ma_house_info);
		tv_owner_info = (TextView) findViewById(R.id.tv_ma_house_owner_info);
		tv_price = (TextView) findViewById(R.id.tv_ma_house_price);
		tv_size = (TextView) findViewById(R.id.tv_ma_house_size);
		tv_price.setText(String.valueOf(house_price) + "元");
		tv_size.setText(String.valueOf(house_size) + "平方米");
		tv_house_brief.setText("这是一套位于" + str_house_location + "," + "年限为"
				+ String.valueOf(house_year) + "年" + "的精品房源" + "   名为"
				+ str_house_name + "具体简介如下  " + str_house_brief);
		adapter = new Image_adapter();// 适配器
		mGallery = (Gallery) findViewById(R.id.ga_house_picture);
		mImageSwitcher = (ImageSwitcher) findViewById(R.id.is_house_picture);

	}

	public ImageSwitcher getImgSwitcher() {
		return mImageSwitcher;
	}

	@SuppressWarnings("deprecation")
	public Gallery getGallery() {
		return mGallery;
	}

	@SuppressWarnings("deprecation")
	private void loading() {
		mImageSwitcher.setFactory(new ViewFactory() {// 工厂接口

					@Override
					public View makeView() {
						return new ImageView(ActivityHouseInfo.this);
					}
				});
		handle = new MyImgSwitcherHandle(ActivityHouseInfo.this, str_picture,
				drawables, adapter);// 处理监听事件

		mImageSwitcher.setOnTouchListener(handle);
		mGallery.setSpacing(20);
		mGallery.setAdapter(adapter);
		mGallery.setOnItemClickListener(handle);
		mGallery.setSelection(str_picture.length / 2);
		str_owner_name = owner.getName();
		str_owner_weichart = owner.getWeichart();
		owner_phone = owner.getPhone();
		owner_year = owner.getAge();
		str_owner_sex = owner.getSex();
		tv_owner_info.setText("卖家姓名:  " + str_owner_name + "性别: "
				+ str_owner_sex + "  微信号" + str_owner_weichart + "  电话"
				+ String.valueOf(owner_phone) + "  年龄"
				+ String.valueOf(owner_year));

	}

	public class Image_adapter extends BaseAdapter {

		@Override
		public int getCount() {
			return str_picture.length;
		}

		@Override
		public Object getItem(int position) {
			return str_picture[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("NewApi")
		@SuppressWarnings("deprecation")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = new ImageView(ActivityHouseInfo.this);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setBackground(drawables[position]);

			imageView.setLayoutParams(new Gallery.LayoutParams(150, 150));

			return imageView;
		}

		// 根据中央的位移量，利用getScale返回views的大小
		public float getScale(boolean focused, int offset) {
			return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
		}
	}

	private class Asny_get_info extends AsyncTask<Void, Void, Integer> {
		private ProgressDialog progressDialog = ProgressDialog.show(
				ActivityHouseInfo.this, "正在加载", "请稍后");

		@Override
		protected void onPostExecute(Integer result) {
			progressDialog.dismiss();

			super.onPostExecute(result);
			if (result == 1) {
				loading();

			} else {
				Toast.makeText(ActivityHouseInfo.this, "获取资料失败，请检查网路",
						Toast.LENGTH_SHORT).show();
			}

		}

		@Override
		protected Integer doInBackground(Void... params) {
			int result = 0;
			for (int i = 0; i < str_picture.length; i++) {
				Bitmap bitmap = imageLoader.loadImageSync(str_picture[i],
						options_cache);
				@SuppressWarnings("deprecation")
				Drawable drawable = new BitmapDrawable(bitmap);
				drawables[i] = drawable;

			}
			try {

				owner = getOwner.get_owner(house_id);
				result = 1;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return result;
		}
	}

}
