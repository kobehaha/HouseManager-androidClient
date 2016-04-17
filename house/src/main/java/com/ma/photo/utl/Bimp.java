package com.ma.photo.utl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Bimp {

	public static int max = 0;
	public static ArrayList<Image_item> mArrayList = new ArrayList<Image_item>();// 选择好的图片

	public static Bitmap change_image_size(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));// 通过流找到该文件
		BitmapFactory.Options options = new BitmapFactory.Options();// 处理bitmap，防止内存溢出
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);// 如果我们把它设为true，那么BitmapFactory.decodeFile(String
														// path, Options
														// opt)并不会真的返回一个Bitmap给你，它仅仅会把它的宽，高取回来给你，这样就不会占用太多的内存，也就不会那么频繁的发生OOM了。
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 1000)
					&& (options.outHeight >> i < 1000)) {// options.outWidth
															// 和
															// options.outHeight就是我们想要的宽和高了。
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;

		}
		return bitmap;

	}

}
