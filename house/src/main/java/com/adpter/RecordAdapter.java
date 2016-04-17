package com.adpter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beans.Bargain;
import com.beans.Record;
import com.beans.Seller;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.special.ResideMenuDemo.R;
import com.utl.Url;

public class RecordAdapter extends BaseAdapter {
	public static ImageLoader imageLoader = ImageLoader.getInstance();// 图片加载类
	private static final int TYPE_CATEGORY_ITEM = 0;
	private static final int TYPE_ITEM = 1;
	private ArrayList<Record> mList;
	private LayoutInflater mInflater;
	private Seller seller;

	public RecordAdapter(Context context, ArrayList<Record> records,
			Seller seller) {
		this.seller = seller;
		mList = records;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		int count = 0;
		if (null != mList) {
			for (Record record : mList) {
				count += record.getItemCount();
			}
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		if (null == mList || position < 0 || position > getCount()) {
			return null;
		}
		int recordFirstIndex = 0;
		for (Record record : mList) {
			int size = record.getItemCount();
			int recordIndex = position - recordFirstIndex;
			if (recordIndex < size) {
				return record.getItem(recordIndex);
			}
			recordFirstIndex += size;
		}
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		int itemViewType = getItemViewType(position);
		switch (itemViewType) {
		case TYPE_CATEGORY_ITEM:
			if (null == convertView) {
				convertView = mInflater.inflate(R.layout.listtestitem, null);
			}

			TextView textView = (TextView) convertView
					.findViewById(R.id.header);
			String itemValue = (String) getItem(position);
			textView.setText(itemValue);
			break;

		case TYPE_ITEM:
			ViewHolder2 viewHolder = null;

			if (null == convertView) {

				convertView = mInflater.inflate(R.layout.listtestitem2, null);

				viewHolder = new ViewHolder2();
				viewHolder.buyer = (TextView) convertView
						.findViewById(R.id.tv_buyer1);
				viewHolder.seller = (TextView) convertView
						.findViewById(R.id.tv_seller1);
				viewHolder.time = (TextView) convertView
						.findViewById(R.id.tv_time1);
				viewHolder.contentIcon = (ImageView) convertView
						.findViewById(R.id.content_icon);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder2) convertView.getTag();
				convertView.setLeft(50);
			}

			// 绑定数据
			Bargain bargain = (Bargain) getItem(position);
			viewHolder.time.setText(bargain.getTimeOk());
			viewHolder.buyer.setText(bargain.getTime());
			viewHolder.seller.setText(seller.getName());
			imageLoader.displayImage(
					"http://" + Url.url + "/" + seller.getUrl(),
					viewHolder.contentIcon);// 加载图片
			break;
		}

		return convertView;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return getItemViewType(position) != TYPE_CATEGORY_ITEM;
	}

	private class ViewHolder {
		TextView content;
		ImageView contentIcon;
	}

	private class ViewHolder2 {
		TextView buyer, time, seller;
		ImageView contentIcon;
	}

	@Override
	public int getItemViewType(int position) {
		// 异常情况处理
		if (null == mList || position < 0 || position > getCount()) {
			return TYPE_ITEM;
		}

		int categroyFirstIndex = 0;

		for (Record category : mList) {
			int size = category.getItemCount();
			// 在当前分类中的索引值
			int categoryIndex = position - categroyFirstIndex;
			if (categoryIndex == 0) {
				return TYPE_CATEGORY_ITEM;
			}

			categroyFirstIndex += size;
		}

		return TYPE_ITEM;
	}

}
