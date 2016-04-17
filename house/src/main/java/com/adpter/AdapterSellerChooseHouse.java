package com.adpter;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.beans.House;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.special.ResideMenuDemo.R;
import com.utl.Url;

@SuppressLint("UseSparseArrays") public class AdapterSellerChooseHouse extends BaseAdapter {
	public static HashMap<Integer, Boolean> is_select;
	private List<House> listHouse;
	private ImageLoader imgImageLoader;
	private DisplayImageOptions options;
	private LayoutInflater inflater;

	public AdapterSellerChooseHouse(Context context, List<House> list_show,
			ImageLoader imageLoader, DisplayImageOptions options) {
		this.listHouse = list_show;
		this.options = options;
		this.imgImageLoader = imageLoader;
		inflater = LayoutInflater.from(context);
		is_select = new HashMap<Integer, Boolean>();
		init2(is_select);

	}

	public void init2(HashMap<Integer, Boolean> isHashMap) {
		for (int i = 0; i < listHouse.size(); i++) {
			getIsSelected().put(i, false);

		}

	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		return is_select;
	}

	public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		AdapterSellerChooseHouse.is_select = isSelected;
	}

	@Override
	public int getCount() {
		return listHouse.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listHouse.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {

		ViewHold viewHold;
		if (arg1 == null) {
			viewHold = new ViewHold();
			arg1 = inflater.inflate(R.layout.listitemhousechoose, null);
			viewHold.image = (ImageView) arg1
					.findViewById(R.id.img_choose_house);
			viewHold.tv_house_location = (TextView) arg1
					.findViewById(R.id.tv_choose_house_info_location);
			viewHold.tv_house_price = (TextView) arg1
					.findViewById(R.id.tv_choose_house_info_price);
			viewHold.tv_house_size = (TextView) arg1
					.findViewById(R.id.tv_choose_house_info_size);
			viewHold.checkBox = (CheckBox) arg1
					.findViewById(R.id.check_choose_house);
			arg1.setTag(viewHold);

		}
		viewHold = (ViewHold) arg1.getTag();
		Log.v("view", viewHold.toString());
		viewHold.tv_house_location.setText(listHouse.get(arg0).getLocation());
		viewHold.tv_house_price.setText(String.valueOf(listHouse.get(arg0)
				.getPrice()));
		viewHold.tv_house_size.setText(String.valueOf(listHouse.get(arg0)
				.getSize()));
		imgImageLoader.displayImage("http://"+Url.url+"/"+listHouse.get(arg0).getPicture_url()[0],
				viewHold.image, options);
		viewHold.checkBox.setChecked(getIsSelected().get(arg0));// 设置选中

		return arg1;
	}

	public class ViewHold {// 一定要加上public 不然无法访问
		public ImageView image;
		public TextView tv_house_location, tv_house_size, tv_house_price;
		public CheckBox checkBox;

	}
}
