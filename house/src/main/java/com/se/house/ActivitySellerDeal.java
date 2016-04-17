package com.se.house;

import java.io.File;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.beans.Bargain;
import com.beans.House;
import com.ma.interfac.HouseInterface;
import com.ma.interfac.OwnerInterface;
import com.ma.service.ServiceHouse;
import com.ma.service.ServiceOwner;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.se.house.ActivitySellerBuyerRecord.AsynDelete;
import com.se.house.DialogBargain.Builder;
import com.se.inter.use.SellerInterface;
import com.se.service.ServiceSeller;
import com.special.ResideMenuDemo.R;
import com.utl.MyImgSwitcherHandle2;

//处理activitysellerdeal

public class ActivitySellerDeal extends BasicActivity {

	// 获取房源信息
	private HouseInterface houseInterface = new ServiceHouse();

	private ImageLoader imageLoader = ImageLoader.getInstance();

	private ImageSwitcher mImageSwitcher;

	private DisplayImageOptions options_cache;

	private Gallery mGallery;

	private TextView tv_house_brief, tv_size, tv_price, tv_owner_info;

	private String str_house_name, str_house_location, str_house_brief,
			str_owner_name, str_owner_weichart, str_owner_sex;
	private int house_price, house_size, house_year, owner_phone, owner_year,
			house_id, buyer_id;

	private String[] str_picture;

	private House mHouse;

	private Image_adapter2 adapter;

	private MyImgSwitcherHandle2 handle;// 图片切换处理器

	private Drawable[] drawables;

	private OwnerInterface getOwner = new ServiceOwner();// 获取owner 服务

	private SellerInterface sellerInterface = new ServiceSeller();// 获取seller服务

	private com.beans.Owner owner;

	private Button btnCancel, btnSure;

	@TargetApi(19)
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.seactivitydeal);
		get_intent();
		config();

		init();
		new AsnyGetInfo().execute();

	}

	@SuppressWarnings("deprecation")
	private void config() {// 配置文件

		File cacheDir = StorageUtils.getOwnCacheDirectory(
				ActivitySellerDeal.this, "imageloader/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				ActivitySellerDeal.this)
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

	private void get_intent() {// 获取houseid

		Bundle bundle = getIntent().getExtras();

		house_id = bundle.getInt("houseId");
		buyer_id = bundle.getInt("buyerId");

		System.out.println("get house id is " + house_id);

	}

	@SuppressWarnings("deprecation")
	private void init() {// 初始化

		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnSure = (Button) findViewById(R.id.btn_deal);

		tv_house_brief = (TextView) findViewById(R.id.tv_ma_house_info);
		tv_owner_info = (TextView) findViewById(R.id.tv_ma_house_owner_info);
		tv_price = (TextView) findViewById(R.id.tv_ma_house_price);
		tv_size = (TextView) findViewById(R.id.tv_ma_house_size);

		adapter = new Image_adapter2();// 适配器
		mGallery = (Gallery) findViewById(R.id.ga_house_picture);
		mImageSwitcher = (ImageSwitcher) findViewById(R.id.is_house_picture);

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				finish();

			}
		});

		btnSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 获取交易dialog
				final com.se.house.DialogBargain.Builder builder = new DialogBargain.Builder(
						ActivitySellerDeal.this);
				builder.setTitle("提示");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								String price = Builder.editText.getText()
										.toString();
								Toast.makeText(ActivitySellerDeal.this,
										"交易价格为   " + price, Toast.LENGTH_SHORT)
										.show();
								Bargain bargain = new Bargain();
								bargain.setBuyerId(String.valueOf(buyer_id));
								bargain.setHouseId(String.valueOf(house_id));
								bargain.setSellerAccount(Seller_account.seller_id);
								bargain.setPrice(Integer.valueOf(price));
								new AsnyDeal().execute(bargain);
								dialog.dismiss();


							}
						});

				builder.setNegativeButton("取消",
						new android.content.DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});

				builder.create().show();

			}
		});

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
						return new ImageView(ActivitySellerDeal.this);
					}
				});

		handle = new MyImgSwitcherHandle2(ActivitySellerDeal.this, str_picture,
				drawables, adapter);

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

		tv_price.setText(String.valueOf(house_price) + "元");
		tv_size.setText(String.valueOf(house_size) + "平方米");
		tv_house_brief.setText("这是一套位于" + str_house_location + "," + "年限为"
				+ String.valueOf(house_year) + "年" + "的精品房源" + "   名为"
				+ str_house_name + "具体简介如下  " + str_house_brief);
		tv_owner_info.setText("卖家姓名:  " + str_owner_name + "性别: "
				+ str_owner_sex + "  微信号" + str_owner_weichart + "  电话"
				+ String.valueOf(owner_phone) + "  年龄"
				+ String.valueOf(owner_year));

	}

	public class Image_adapter2 extends BaseAdapter {

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
			ImageView imageView = new ImageView(ActivitySellerDeal.this);
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

	private class AsnyDeal extends AsyncTask<Bargain, Void, Boolean> {

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				Toast.makeText(ActivitySellerDeal.this, "交易成功",
						Toast.LENGTH_SHORT).show();

			} else {
				Toast.makeText(ActivitySellerDeal.this, "交易失败，请检查网络",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected Boolean doInBackground(Bargain... arg0) {
			boolean flag = false;
			try {
				flag = sellerInterface.dealBargain(arg0[0]);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return flag;
		}

	}

	private class AsnyGetInfo extends AsyncTask<Void, Void, Integer> {
		private ProgressDialog progressDialog = ProgressDialog.show(
				ActivitySellerDeal.this, "正在加载", "请稍后");

		@Override
		protected void onPostExecute(Integer result) {
			progressDialog.dismiss();

			super.onPostExecute(result);
			if (result == 1) {
				loading();

			} else {
				Toast.makeText(ActivitySellerDeal.this, "获取资料失败，请检查网路",
						Toast.LENGTH_SHORT).show();
			}

		}

		@Override
		protected Integer doInBackground(Void... params) {
			int result = 0;

			try {
				mHouse = houseInterface.getHouseById(house_id);// 获取房源信息

				str_picture = mHouse.getPicture_url();
				drawables = new Drawable[str_picture.length];
				str_house_brief = mHouse.getBrief();
				str_house_location = mHouse.getLocation();
				str_house_name = mHouse.getName();
				house_price = mHouse.getPrice();
				house_size = mHouse.getSize();
				house_year = mHouse.getYear();
				System.out.println("找到的数据是 " + "房源id=" + house_id + " 房源年限="
						+ house_year + "  房源价格=" + house_price + "  房源名称是="
						+ str_house_name + "房源简介是="
						+ String.valueOf(str_house_brief) + "  房源图片url＝"
						+ String.valueOf(str_picture[0]));

				for (int i = 0; i < str_picture.length; i++) {
					// 加载图片
					Bitmap bitmap = imageLoader.loadImageSync(str_picture[i],
							options_cache);
					@SuppressWarnings("deprecation")
					Drawable drawable = new BitmapDrawable(bitmap);
					drawables[i] = drawable;

				}

			} catch (Exception e1) {
				e1.printStackTrace();
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
