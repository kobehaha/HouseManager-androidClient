package com.utl;

import java.io.File;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.ma.activity.ActivityAddHouseInfo;

public class Uri_util {

	public static String get_app_file_path() {// 获取存储路径
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ File.separator
				+ "house.data"
				+ File.separator;
		return path;

	}

	public static Bitmap uri_to_path(ActivityAddHouseInfo context, Uri image)
			throws FileNotFoundException {
		String scheme = image.getScheme();
		if (scheme.equals("content")) {
			return init_from_content_uri(context, image);
		} else if (image.getScheme().equals("file")) {
			return init_from_file(context, image);

		}

		return null;
	}

	private static Bitmap init_from_file(Context context, Uri image) {
		Bitmap mBitmap = null;

		String mPath = image.getPath();
		String extension = MimeTypeMap.getFileExtensionFromUrl(mPath);// 是否支持该种文件
		if (TextUtils.isEmpty(extension)) {
			// getMimeTypeFromExtension() doesn't handle spaces in filenames nor
			// can it handle
			// urlEncoded strings. Let's try one last time at finding the
			// extension.
			int dotPos = mPath.lastIndexOf('.');
			if (0 <= dotPos) {
				extension = mPath.substring(dotPos + 1);
			}
		}

		return mBitmap;
	}

	private static Bitmap init_from_content_uri(ActivityAddHouseInfo context,
			Uri uri) throws FileNotFoundException {
		Bitmap mbitmap = null;
		if (uri != null) {

		}
		String[] pojo = { MediaStore.Images.Media.DATA };

		@SuppressWarnings("deprecation")
		Cursor cursor = context.managedQuery(uri, pojo, null, null, null);
		if (cursor != null) {
			ContentResolver cr = context.getContentResolver();
			int colunm_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String path = cursor.getString(colunm_index);// 解析文件路径
			System.out.println(path);

			// 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，你选择的文件就不一定是图片了，
			// 这样的话，我们判断文件的后缀名 如果是图片格式的话，那么才可以

			if (path.endsWith("jpg") || path.endsWith("png")||path.endsWith("imge")) {

				mbitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

			}

		}
		return mbitmap;
	}

	public static String get_path(Activity activity, Uri uri) {// 获图片路径
		String path = null;

		if (uri != null) {
			String[] pojo = { MediaStore.Images.Media.DATA };

			@SuppressWarnings("deprecation")
			Cursor cursor = activity.managedQuery(uri, pojo, null, null, null);
			if (cursor != null) {
				int colunm_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				path = cursor.getString(colunm_index);// 解析文件路径

			}

		}
		return path;
	}
}
