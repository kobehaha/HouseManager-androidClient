package com.utl;


import java.io.File;

import android.app.Activity;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.special.ResideMenuDemo.R;

public class ImageLoadHouseFactory {//客户端图片加载器，不同的主要是round 和背景图片，线程数

	public static ImageLoader imageLoader;
	private DisplayImageOptions options;

	@SuppressWarnings("deprecation")
	public ImageLoadHouseFactory(Activity activity, int n) {//传入activity和加载线程的数量,第二个参数为圆的参数
		imageLoader = ImageLoader.getInstance();// 图片加载类
		// 图片加载设置类
		File cacheDir = StorageUtils.getOwnCacheDirectory(// 设置缓存
				activity, "imageloader/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(// 配置文件
				activity)
				.threadPoolSize(n)
				.// 线程池大小
				threadPriority(Thread.NORM_PRIORITY - 2)
				.// 线程优先级
				tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCache(new UnlimitedDiskCache(cacheDir))
				.denyCacheImageMultipleSizesInMemory().build();

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageOnFail(R.drawable.ic_error)
				.showImageForEmptyUri(R.drawable.ic_empty).cacheOnDisc(true)
				// 设置缓存到sd，内存中
				.displayer(new RoundedBitmapDisplayer(0)).cacheInMemory(true)
				.considerExifParams(true).build();// 设置不同形态的图片
		
		imageLoader.init(config);

	}

	public static ImageLoader getImageLoader() {
		
		
		
		
		return imageLoader;
	}

	public DisplayImageOptions getOptions() {
		return options;
	}

}
