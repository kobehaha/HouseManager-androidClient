package com.ma.activity;

import java.io.File;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.special.ResideMenuDemo.R;

public class ActivityShowPicture extends Activity {
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageView mImageView;
	private DisplayImageOptions options;
	private String url;

	@TargetApi(19)
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
		setContentView(R.layout.maactivityshowbig);
		init();
		config();
		getPicture();
		imageLoader.displayImage(url, mImageView, options);
		Log.v("test....", url);
	}

	private void getPicture() {
		Bundle bundle = getIntent().getExtras();
		url = bundle.getString("url");

	}

	private void init() {
		mImageView = (ImageView) findViewById(R.id.img_big_picture);

	}

	@SuppressWarnings("deprecation")
	private void config() {// 配置文件

		File cacheDir = StorageUtils.getOwnCacheDirectory(// 设置缓存
				ActivityShowPicture.this, "imageloader/Cache2");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(// 配置文件
				ActivityShowPicture.this).threadPoolSize(1)
				.// 线程池大小
				threadPriority(4).threadPriority(1)
				.
				// 线程优先级

				discCache(new UnlimitedDiskCache(cacheDir))
				.denyCacheImageMultipleSizesInMemory().build();

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageOnFail(R.drawable.ic_error)
				.showImageForEmptyUri(R.drawable.ic_empty).cacheOnDisc(true)
				// 设置缓存到sd，内存中
				.displayer(new RoundedBitmapDisplayer(20)).cacheInMemory(true)
				.considerExifParams(true).build();// 设置不同形态的图片
		imageLoader.destroy();
		imageLoader.init(config);

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

}
