package com.ma.photo.utl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;

public class AlbumHelper {
	final String TAG = getClass().getSimpleName();
	Context context;
	ContentResolver cr;

	HashMap<String, String> thumbnailList = new HashMap<String, String>();// 只需要获取path路径

	List<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();// 获取相册的所有信息
	HashMap<String, Image_bucket> bucketList = new HashMap<String, Image_bucket>();

	private static AlbumHelper instance;

	private AlbumHelper() {
	}

	public static AlbumHelper getHelper() {// 实例化
		if (instance == null) {
			instance = new AlbumHelper();
		}
		return instance;
	}

	public void init(Context context) {
		if (this.context == null) {
			this.context = context;
			cr = context.getContentResolver();
		}
	}

	private void getThumbnail() {// 略缩图的属性列
		String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID,
				Thumbnails.DATA };
		Cursor cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection,
				null, null, null);
		getThumbnailColumnData(cursor);
	}

	private void getThumbnailColumnData(Cursor cur) {// 获取略缩列的资料
		if (cur.moveToFirst()) {
			int _id;
			int image_id;
			String image_path;
			int _idColumn = cur.getColumnIndex(Thumbnails._ID);
			int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
			int dataColumn = cur.getColumnIndex(Thumbnails.DATA);

			do {
				// Get the field values
				_id = cur.getInt(_idColumn);
				image_id = cur.getInt(image_idColumn);
				image_path = cur.getString(dataColumn);

				// Do something with the values.
				// Log.i(TAG, _id + " image_id:" + image_id + " path:"
				// + image_path + "---");
				// HashMap<String, String> hash = new HashMap<String, String>();
				// hash.put("image_id", image_id + "");
				// hash.put("path", image_path);
				// thumbnailList.add(hash);
				thumbnailList.put("" + image_id, image_path);
			} while (cur.moveToNext());
		}
	}

	void getAlbum() {// 获取相册
		String[] projection = { Albums._ID, Albums.ALBUM, Albums.ALBUM_ART,
				Albums.ALBUM_KEY, Albums.ARTIST, Albums.NUMBER_OF_SONGS };// 属性列
		Cursor cursor = cr.query(Albums.EXTERNAL_CONTENT_URI, projection, null,
				null, null);
		getAlbumColumnData(cursor);

	}

	private void getAlbumColumnData(Cursor cur) {
		if (cur.moveToFirst()) {
			int _id;
			String album;
			String albumArt;
			String albumKey;
			String artist;
			int numOfSongs;

			int _idColumn = cur.getColumnIndex(Albums._ID);
			int albumColumn = cur.getColumnIndex(Albums.ALBUM);
			int albumArtColumn = cur.getColumnIndex(Albums.ALBUM_ART);
			int albumKeyColumn = cur.getColumnIndex(Albums.ALBUM_KEY);
			int artistColumn = cur.getColumnIndex(Albums.ARTIST);
			int numOfSongsColumn = cur.getColumnIndex(Albums.NUMBER_OF_SONGS);

			do {
				// Get the field values
				_id = cur.getInt(_idColumn);
				album = cur.getString(albumColumn);
				albumArt = cur.getString(albumArtColumn);
				albumKey = cur.getString(albumKeyColumn);
				artist = cur.getString(artistColumn);
				numOfSongs = cur.getInt(numOfSongsColumn);

				// Do something with the values.
				Log.i(TAG, _id + " album:" + album + " albumArt:" + albumArt
						+ "albumKey: " + albumKey + " artist: " + artist
						+ " numOfSongs: " + numOfSongs + "---");
				HashMap<String, String> hash = new HashMap<String, String>();
				hash.put("_id", _id + "");
				hash.put("album", album);
				hash.put("albumArt", albumArt);
				hash.put("albumKey", albumKey);
				hash.put("artist", artist);
				hash.put("numOfSongs", numOfSongs + "");
				albumList.add(hash);

			} while (cur.moveToNext());

		}
	}

	boolean hasBuildImagesBucketList = false;

	void buildImagesBucketList() {
		long startTime = System.currentTimeMillis();

		getThumbnail();

		String columns[] = new String[] { Media._ID, Media.BUCKET_ID,
				Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME, Media.TITLE,
				Media.SIZE, Media.BUCKET_DISPLAY_NAME };
		Cursor cur = cr.query(Media.EXTERNAL_CONTENT_URI, columns, null, null,
				null);
		if (cur.moveToFirst()) {
			int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
			int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
			int photoNameIndex = cur.getColumnIndexOrThrow(Media.DISPLAY_NAME);
			int photoTitleIndex = cur.getColumnIndexOrThrow(Media.TITLE);
			int photoSizeIndex = cur.getColumnIndexOrThrow(Media.SIZE);
			int bucketDisplayNameIndex = cur
					.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
			int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);
			int picasaIdIndex = cur.getColumnIndexOrThrow(Media.PICASA_ID);
			int totalNum = cur.getCount();

			do {
				String _id = cur.getString(photoIDIndex);
				String name = cur.getString(photoNameIndex);
				String path = cur.getString(photoPathIndex);
				String title = cur.getString(photoTitleIndex);
				String size = cur.getString(photoSizeIndex);
				String bucketName = cur.getString(bucketDisplayNameIndex);
				String bucketId = cur.getString(bucketIdIndex);
				String picasaId = cur.getString(picasaIdIndex);

				Log.i(TAG, _id + ", bucketId: " + bucketId + ", picasaId: "
						+ picasaId + " name:" + name + " path:" + path
						+ " title: " + title + " size: " + size + " bucket: "
						+ bucketName + "---");

				Image_bucket bucket = bucketList.get(bucketId);
				if (bucket == null) {
					bucket = new Image_bucket();
					bucketList.put(bucketId, bucket);
					bucket.imageList = new ArrayList<Image_item>();
					bucket.bucketName = bucketName;
				}
				bucket.count++;
				Image_item imageItem = new Image_item();
				imageItem.imge_id = _id;
				imageItem.imge_path = path;
				imageItem.imge_thumbnail_path = thumbnailList.get(_id);
				bucket.imageList.add(imageItem);

			} while (cur.moveToNext());
		}

		Iterator<Entry<String, Image_bucket>> itr = bucketList.entrySet()
				.iterator();
		while (itr.hasNext()) {
			Map.Entry<String, Image_bucket> entry = (Map.Entry<String, Image_bucket>) itr
					.next();
			Image_bucket bucket = entry.getValue();
			Log.d(TAG, entry.getKey() + ", " + bucket.bucketName + ", "
					+ bucket.count + " ---------- ");
			for (int i = 0; i < bucket.imageList.size(); ++i) {
				Image_item image = bucket.imageList.get(i);
				Log.d(TAG, "----- " + image.imge_id + ", " + image.imge_path
						+ ", " + image.imge_thumbnail_path);
			}
		}
		hasBuildImagesBucketList = true;
		long endTime = System.currentTimeMillis();
		Log.d(TAG, "use time: " + (endTime - startTime) + " ms");
	}

	public List<Image_bucket> getImagesBucketList(boolean refresh) {// 获取刷新了的imgebucketlist
		if (refresh || (!refresh && !hasBuildImagesBucketList)) {
			buildImagesBucketList();
		}
		List<Image_bucket> tmpList = new ArrayList<Image_bucket>();
		Iterator<Entry<String, Image_bucket>> itr = bucketList.entrySet()
				.iterator();
		while (itr.hasNext()) {
			Map.Entry<String, Image_bucket> entry = (Map.Entry<String, Image_bucket>) itr
					.next();
			tmpList.add(entry.getValue());
		}
		return tmpList;
	}

	String getOriginalImagePath(String image_id) {// 获取最原始图片路径
		String path = null;
		Log.i(TAG, "---(^o^)----" + image_id);
		String[] projection = { Media._ID, Media.DATA };
		Cursor cursor = cr.query(Media.EXTERNAL_CONTENT_URI, projection,
				Media._ID + "=" + image_id, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			path = cursor.getString(cursor.getColumnIndex(Media.DATA));

		}
		return path;
	}

}
