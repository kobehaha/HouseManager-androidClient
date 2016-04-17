package com.adpter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beans.Seller_proxy;
import com.special.ResideMenuDemo.R;

public class AdapterManagerSeller extends BaseAdapter {
	private Context context;
	private List<Seller_proxy> seller_proxies;

	public AdapterManagerSeller(Context context, List<Seller_proxy> seller_proxies

	) {
		this.context = context;
		this.seller_proxies = seller_proxies;

	}

	@Override
	public int getCount() {
		return seller_proxies.size();
	}

	@Override
	public Object getItem(int arg0) {
		return seller_proxies.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder viewHolder;
		if (arg1 == null) {// 无视图时
			arg1 = LayoutInflater.from(context).inflate(
					R.layout.listitemmaseller, null);
			viewHolder = new ViewHolder();
			viewHolder.linearLayout = (LinearLayout) arg1
					.findViewById(R.id.lin_show_seller);
			viewHolder.firstChar = (TextView) arg1
					.findViewById(R.id.tv_seller_item_first_char);
			viewHolder.name = (TextView) arg1
					.findViewById(R.id.tv_seller_item_name);
			viewHolder.icon = (ImageView) arg1
					.findViewById(R.id.img_seller_item_picture);
			arg1.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) arg1.getTag();
		}
		if (viewHolder == null) {
			arg1 = LayoutInflater.from(context).inflate(
					R.layout.listitemmaseller, null);
			viewHolder = new ViewHolder();
			viewHolder.linearLayout = (LinearLayout) arg1
					.findViewById(R.id.lin_show_seller);
			viewHolder.icon = (ImageView) arg1
					.findViewById(R.id.img_seller_item_picture);
			viewHolder.firstChar = (TextView) arg1
					.findViewById(R.id.tv_seller_item_first_char);
			viewHolder.name = (TextView) arg1.findViewById(R.id.name);
			arg1.setTag(viewHolder);
		}
		viewHolder.firstChar.setVisibility(View.GONE);// 开始没有这个视图
		viewHolder.linearLayout.setVisibility(View.GONE);
		Seller_proxy mSeller = (Seller_proxy) getItem(arg0);// 获取这个代理seller
		String name = mSeller.seller.getName();
		
		viewHolder.name.setText(name);
		int section = getSectionForPosition(arg0);// ///////////
		if (arg0 == getPositionForSection(section)) {
			viewHolder.firstChar.setText(mSeller.firstchar);
			viewHolder.firstChar.setVisibility(View.VISIBLE);
			viewHolder.linearLayout.setVisibility(View.VISIBLE);

		} else {
			viewHolder.firstChar.setVisibility(View.GONE);
			viewHolder.linearLayout.setVisibility(View.GONE);

		}

		viewHolder.icon.setBackgroundResource(R.drawable.ic_empty);

		return arg1;
	}

	@SuppressLint("DefaultLocale")
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = seller_proxies.get(i).firstchar;
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	private int getSectionForPosition(int arg0) {
		return seller_proxies.get(arg0).firstchar.charAt(0);// 返回的0下的char对应的int
	}

	static class ViewHolder {
		LinearLayout linearLayout;
		ImageView icon;
		TextView firstChar, name;
	}

}
