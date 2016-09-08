package com.adpter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beans.Client_proxy;
import com.special.ResideMenuDemo.R;
import com.utl.ImageLoadClientFactory;

public class AdapterSellerClient extends BaseAdapter {
	private Context context;
	private List<Client_proxy> client;
	private ImageLoadClientFactory imageLoad;

	public AdapterSellerClient(Context context, List<Client_proxy> clientProxy) {
		this.context = context;
		this.client = clientProxy;
		imageLoad = new ImageLoadClientFactory((Activity) context,2);//获取图片辅助类

	}

	@Override
	public int getCount() {
		return client.size();
	}

	@Override
	public Object getItem(int position) {
		return client.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.listimteseclien, null);
			viewHolder = new ViewHolder();
			viewHolder.linearLayout = (LinearLayout) convertView
					.findViewById(R.id.lin_show);
			viewHolder.status = (TextView) convertView
					.findViewById(R.id.tv_client_complete);
			viewHolder.icon = (ImageView) convertView
					.findViewById(R.id.img_client_item_picture);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.tv_client_item_name);
			viewHolder.firstChar = (TextView) convertView
					.findViewById(R.id.tv_client_item_first_char);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (viewHolder == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.listimteseclien, null);
			viewHolder = new ViewHolder();
			viewHolder.linearLayout = (LinearLayout) convertView
					.findViewById(R.id.lin_show);
			viewHolder.icon = (ImageView) convertView
					.findViewById(R.id.img_client_item_picture);

			viewHolder.name = (TextView) convertView
					.findViewById(R.id.tv_client_item_name);
			viewHolder.firstChar = (TextView) convertView
					.findViewById(R.id.tv_client_item_first_char);
			convertView.setTag(viewHolder);

		}

		viewHolder.firstChar.setVisibility(View.GONE);
		viewHolder.linearLayout.setVisibility(View.GONE);
		Client_proxy client_proxy = (Client_proxy) getItem(position);
		if (client_proxy.getClient().getStatus() == 1) {
			viewHolder.status.setVisibility(View.VISIBLE);

		}

		String name = client_proxy.client.getName();
		viewHolder.name.setText(name);
		int section = getSectionForPosition(position);// ///////////
		if (position == getPositionForSection(section)) {
			viewHolder.firstChar.setText(client_proxy.firstchar);
			viewHolder.firstChar.setVisibility(View.VISIBLE);
			viewHolder.linearLayout.setVisibility(View.VISIBLE);

		} else {
			viewHolder.firstChar.setVisibility(View.GONE);
			viewHolder.linearLayout.setVisibility(View.GONE);

		}

		// viewHolder.icon.setBackgroundResource(R.drawable.display);

		ImageLoadClientFactory.imageLoader.displayImage(client.get(position).getClient()
				.getUrl(), viewHolder.icon, imageLoad.getOptions());// 加载图片

		return convertView;
	}

	@SuppressLint("DefaultLocale")
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = client.get(i).firstchar;
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	private int getSectionForPosition(int arg0) {
		return client.get(arg0).firstchar.charAt(0);// 返回的0下的char对应的int
	}

	static class ViewHolder {
		LinearLayout linearLayout;
		ImageView icon;
		TextView firstChar, name, status;
	}

}
