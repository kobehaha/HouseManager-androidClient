package com.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.adpter.AdapterSellerClient;
import com.beans.CharacterParser;
import com.beans.Client;
import com.beans.Client_proxy;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.se.house.ActivitySellerBuyerRecord;
import com.se.inter.use.SellerInterface;
import com.se.service.ServiceSeller;
import com.special.ResideMenuDemo.R;
import com.utl.PinyinComparator_client;
import com.utl.View_side_bar;
import com.utl.View_side_bar.OnTouchingLetterChangedListener;

/**
 */
public class FragmentSellerClient extends Fragment {
	private View_side_bar side_bar;
	private PinyinComparator_client comparator = new PinyinComparator_client();
	private CharacterParser parser = CharacterParser.getInstance();// 字符转换类
	private View parentView;
	private PullToRefreshListView mlListView;
	private ListView listView;
	private List<Client_proxy> list_proxy = new ArrayList<Client_proxy>();
	private List<Client> list_show = new ArrayList<Client>();
	private AdapterSellerClient adapterSeller;
	private SellerInterface sellerInterface = new ServiceSeller();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.sefragmentclient, container,
				false);


		return parentView;






	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				new AsnyLoad().execute();

			}
		}, 500);

		init();

	}

	private void init() {

		mlListView = (PullToRefreshListView) parentView
				.findViewById(R.id.lv_client);
		side_bar = (View_side_bar) parentView.findViewById(R.id.sidrbar_client);

		side_bar.setOnTouchingLetterChangedListner(new OnTouchingLetterChangedListener() {
			//
			@Override
			public void onTouchingLetterChanged(String s) {
				int position = adapterSeller.getPositionForSection(s.charAt(0));
				if (position != -1) {
					listView.setSelection(position);
				}
				//
			}
		});

		mlListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				new AsnyLoad().execute();
				refreshView.clearFocus();

			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
			}
		});

	}

	private class AsnyLoad extends AsyncTask<Void, Void, Integer> {
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (result == 1) {
				adapterSeller = new AdapterSellerClient(getActivity(),
						list_proxy);
				listView = mlListView.getRefreshableView();

				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						
						Intent intent = new Intent(getActivity(),
								ActivitySellerBuyerRecord.class);
						Bundle bundle = new Bundle();
						bundle.putInt("buyerId", list_proxy.get((int) arg3)
								.getClient().getId());// 传递buyerID
						intent.putExtras(bundle);
						startActivity(intent);
						
					}
				});

				listView.setAdapter(adapterSeller);
				Toast.makeText(getActivity(), "刷新完成", Toast.LENGTH_SHORT)
						.show();

			} else {
				Toast.makeText(getActivity(), "刷新失败", Toast.LENGTH_SHORT)
						.show();

			}
		}

		@Override
		protected Integer doInBackground(Void... params) {// 加载出数据马上获取
			try {
				Thread.sleep(1000);

				list_show = sellerInterface.getAllBuyers();
				loadSeller(list_show);
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
			return 1;
		}

	}

	@SuppressLint("DefaultLocale")
	protected void loadSeller(List<Client> list_show2) {
		list_proxy.clear();

		if (list_show2 != null) {
			for (Client user : list_show2) {

				String pinyin = parser.getSelling(user.getName());
				String sortString = pinyin.substring(0, 1).toUpperCase();
				Client_proxy userProxy = new Client_proxy(user);
				if (sortString.matches("[A-Z]")) {
					userProxy.firstchar = sortString.toUpperCase();
				} else {
					userProxy.firstchar = "#";
				}
				list_proxy.add(userProxy);

			}
			Collections.sort(list_proxy, comparator);
		}
		if (adapterSeller != null) {
			adapterSeller.notifyDataSetChanged();
		}

	}

}
