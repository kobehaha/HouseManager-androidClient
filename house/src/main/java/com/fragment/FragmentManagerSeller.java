package com.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.adpter.AdapterManagerSeller;
import com.beans.CharacterParser;
import com.beans.Seller;
import com.beans.Seller_proxy;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ma.activity.ActivitySellerInfo;
import com.ma.interfac.SellerInterface;
import com.ma.service.ServiceSeller;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utl.View_side_bar;
import com.utl.View_side_bar.OnTouchingLetterChangedListener;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
@SuppressLint("DefaultLocale")
public class FragmentManagerSeller extends Fragment {
	private View_side_bar side_bar;
	private ImageLoader imageLoader = FragmentManagerHouse.imageLoader;// 图片加载类
	private DisplayImageOptions options;// 图片加载设置类
	private PinyinComparator_seller comparator = new PinyinComparator_seller();
	private CharacterParser parser = CharacterParser.getInstance();// 字符转换类
	private static int count = 0;
	private PullToRefreshListView mListView;
	private ListView listView;
	private SellerInterface get_seller;
	private List<Seller> list_all = new ArrayList<Seller>();
	private List<Seller> list_show = new ArrayList<Seller>();
	private List<Seller> list_least;
	private List<Seller_proxy> list_proxy = new ArrayList<Seller_proxy>();// seller_代理类
	private AdapterManagerSeller adapter;
	private View parentView;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		list_show = new ArrayList<Seller>();
		list_show.add(new Seller("", "张志远", "", "", "", 5, 6, 7, "weixin"));
		list_show.add(new Seller("", "龙飞", "", "", "", 5, 6, 7, "weixin"));
		list_show.add(new Seller("", "德莱文", "", "", "", 5, 6, 7, "weixin"));
		list_show.add(new Seller("", "德玛", "", "", "", 5, 6, 7, "weixin"));
		list_show.add(new Seller("", "天妃", "", "", "", 5, 6, 7, "weixin"));
		list_show.add(new Seller("", "张龙", "", "", "", 5, 6, 7, "weixin"));
		list_show.add(new Seller("", "杜司棋美女", "", "", "", 5, 6, 7, "weixin"));
		list_show.add(new Seller("", "梁一怀", "", "", "", 7, 8, 9, "weixin"));
		list_show.add(new Seller("", "科比", "", "", "", 7, 8, 9, "weixin"));

		init();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				new Asny_get_seller().execute(1);
				loadSeller(list_show);
			}
		}, 500);
		Log.v("执行到这一步", "background");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.mafragmentseller, container, false);
		mListView = (PullToRefreshListView) parentView
				.findViewById(R.id.lv_lv_ma_seller);
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				Toast.makeText(getActivity(), "刷新", Toast.LENGTH_SHORT).show();

			}
		});

		return parentView;
	}

	private void init() {

		ILoadingLayout attr = mListView.getLoadingLayoutProxy();// 获取加载布局代理对象
		attr.setPullLabel("下拉可以刷新");
		attr.setRefreshingLabel("数据加载中，请等待");
		attr.setReleaseLabel("放开将会刷新加载");
		adapter = new AdapterManagerSeller(getActivity(), list_proxy
		// imageLoader,
		// options
		);// 获取适配器

		listView = mListView.getRefreshableView();// 获取刷新视图
		list_show = new ArrayList<Seller>();
		list_show.add(new Seller("", "张志远", "", "", "", 5, 6, 7, "weixin"));
		list_show.add(new Seller("", "龙飞", "", "", "", 5, 6, 7, "weixin"));
		list_show.add(new Seller("", "德莱文", "", "", "", 5, 6, 7, "weixin"));
		list_show.add(new Seller("", "德玛", "", "", "", 5, 6, 7, "weixin"));
		list_show.add(new Seller("", "天妃", "", "", "", 5, 6, 7, "weixin"));
		list_show.add(new Seller("", "张龙", "", "", "", 5, 6, 7, "weixin"));
		list_show.add(new Seller("", "杜司棋美女", "", "", "", 5, 6, 7, "weixin"));
		list_show.add(new Seller("", "梁一怀", "", "", "", 7, 8, 9, "weixin"));
		list_show.add(new Seller("", "科比", "", "", "", 7, 8, 9, "weixin"));
		listView.setAdapter(adapter);
		side_bar = (View_side_bar) parentView.findViewById(R.id.sidrbar);
		get_seller = new ServiceSeller();

		listView = mListView.getRefreshableView();// 获取刷新视图

		mListView.setAdapter(adapter);// 设置适配器
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				startActivity(new Intent(getActivity(),
						ActivitySellerInfo.class));

			}
		});

		side_bar.setOnTouchingLetterChangedListner(new OnTouchingLetterChangedListener() {
			//
			@Override
			public void onTouchingLetterChanged(String s) {
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					listView.setSelection(position);
				}
				//
			}
		});

		mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				new Asny_get_seller().execute(1);
				Toast.makeText(getActivity(), "dd", Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT)
				// .show();

			}
		});

		mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// new Asny_get_seller().execute(1);// 加载数据

				// loadSeller(list_show);
				// mListView.clearAnimation();

			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				new Asny_get_seller().execute(0);
				count = count + 1;
				loadSeller(list_show);

			}
		});
		mListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				Toast.makeText(getActivity(), "所有数据加载完毕", Toast.LENGTH_SHORT)
						.show();

			}
		});

	}

	@SuppressLint("DefaultLocale")
	protected void loadSeller(List<Seller> seller) {
		list_proxy.clear();

		if (seller != null) {
			for (Seller user : seller) {
				Log.v("loadselelr", "1");

				String pinyin = parser.getSelling(user.getName());
				String sortString = pinyin.substring(0, 1).toUpperCase();
				Seller_proxy userProxy = new Seller_proxy(user);
				if (sortString.matches("[A-Z]")) {
					userProxy.firstchar = sortString.toUpperCase();
				} else {
					userProxy.firstchar = "#";
				}
				list_proxy.add(userProxy);
				Log.v("loadselelr", "2");

			}
			Collections.sort(list_proxy, comparator);
		}
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
		Log.v("loadselelr", "3");

	}

	private class Asny_get_seller extends AsyncTask<Integer, Void, Integer> {
		@Override
		protected void onPostExecute(Integer result) {
			Log.v("结果result", String.valueOf(result));

			super.onPostExecute(result);// 放在前面，不然不起作用
			if (result == 0) {
				Toast.makeText(getActivity(), "网络连接失败..", Toast.LENGTH_SHORT)
						.show();
				//
			} else {
				loadSeller(list_all);
				Toast.makeText(getActivity(), "加载数据成功", Toast.LENGTH_SHORT)
						.show();
			}
		}

		@Override
		protected Integer doInBackground(Integer... arg0) {
			int result = 0;
			Log.v("arg0", String.valueOf(arg0[0]));
			if (arg0[0] == 1) {
				try {
					Thread.sleep(2000);
					List<Seller> kobeList = get_seller.get_all_seller();
					list_all.addAll(kobeList);

					list_show.addAll(list_all);// 添加所有显示
					result = 1;
					Log.v("list_all_所有数据", list_all.toString());
				} catch (ClientProtocolException e) {

					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				try {
					list_least = get_seller.get_least7_seller(count);
					list_show.addAll(list_least);// 每次添加7个
					result = 1;
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
			Log.v("result", String.valueOf(result));

			return result;
		}
	}
}
