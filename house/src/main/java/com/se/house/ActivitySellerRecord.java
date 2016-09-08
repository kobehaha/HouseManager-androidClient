package com.se.house;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adpter.RecordAdapter;
import com.beans.Bargain;
import com.beans.Record;
import com.beans.Seller;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.se.inter.use.SellerInterface;
import com.se.service.ServiceSeller;
import com.special.ResideMenuDemo.R;

public class ActivitySellerRecord extends BasicActivity {
	private RecordAdapter mAdapter;
	
	private PullToRefreshListView listView;
	
	private SellerInterface seller = new ServiceSeller();
	
	private String url = "";
	
	private ArrayList<Record> records;
	
	private Seller sellerInfo;
	
	
	private View view1, view2;
	
	private List<View> listViewPage = new ArrayList<View>();
	
	private ViewPager viewPager;
	
	private Button btnMonth, btnYear;
	
	private PullToRefreshListView pullToRefreshListView;
	
	private List<Bargain> bargains;
    
	@TargetApi(19)
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void onStart() {
		setContentView(R.layout.seactivitysellrecord);
		// init();
		// new AsynGetSellerSuccessRecord().execute();
		btnMonth = (Button) findViewById(R.id.btn_month);
		btnMonth.setTextColor(Color.BLUE);
		btnYear = (Button) findViewById(R.id.btn_year);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		LayoutInflater inflater = getLayoutInflater();
		view1 = inflater.inflate(R.layout.viewpagermonth, null);
		view2 = inflater.inflate(R.layout.viewpageyear, null);
		listViewPage.add(view1);
		listViewPage.add(view2);
		init1();
		init2();
		PageAdapter pageAdapter = new PageAdapter();
		viewPager.setAdapter(pageAdapter);
		btnMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				viewPager.setCurrentItem(0);

			}
		});
		btnYear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				viewPager.setCurrentItem(1);

			}
		});
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (arg0 == 0) {
					new AsynGetSellerSuccessRecord().execute();
					btnYear.setTextColor(Color.GRAY);
					btnMonth.setTextColor(Color.BLUE);
				} else {
					btnMonth.setTextColor(Color.GRAY);
					btnYear.setTextColor(Color.BLUE);

				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		super.onStart();
	}

	private void init2() {
		pullToRefreshListView = (PullToRefreshListView) view2
				.findViewById(R.id.list_record);

	}

	private void init1() {

		listView = (PullToRefreshListView) view1.findViewById(R.id.list_record);
		ILoadingLayout startLabels = listView
				.getLoadingLayoutProxy(true, false);
		startLabels.setPullLabel("");// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel("");// 刷新时
		startLabels.setReleaseLabel("");// 下来达到一定距离时，显示的提示

		ILoadingLayout endLabels = listView.getLoadingLayoutProxy(false, true);
		endLabels.setPullLabel("");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("");// 刷新时
		endLabels.setReleaseLabel("");// 下来达到一定距离时，显示的提示
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startActivity(new Intent(ActivitySellerRecord.this,
						ActivitySellerRecordInfo.class));

			}
		});

	}

	private class AsynGetSellerSuccessRecord extends
			AsyncTask<Void, Void, Integer> {
		@Override
		protected void onPostExecute(Integer result) {
			if (result == 1) {
				mAdapter = new RecordAdapter(ActivitySellerRecord.this,
						records, sellerInfo);
				listView.setAdapter(mAdapter);
				Toast.makeText(ActivitySellerRecord.this, "加载数据成功",
						Toast.LENGTH_SHORT).show();

			} else {
				records = new ArrayList<Record>();
				ArrayList<Bargain> bargains = new ArrayList<Bargain>();
				Bargain bargain = new Bargain();
				bargain.setHouseId("1");
				bargain.setTime("2015-2-14");
				bargain.setBuyerId("15");
				bargain.setPrice(Integer.valueOf(30));
				bargain.setTimeOk("2015-2-14");
				bargains.add(bargain);
				Record record = new Record("2015-2-1", bargains);
				Bargain bargain2 = new Bargain();
				ArrayList<Bargain> bargains2 = new ArrayList<Bargain>();
				bargain.setHouseId("1");
				bargain.setTime("2014-2-14");
				bargain.setBuyerId("15");
				bargain.setPrice(Integer.valueOf(30));
				bargain.setTimeOk("2014-2-14");
				bargains2.add(bargain2);
				Bargain bargain3 = new Bargain();
				bargain.setHouseId("1");
				bargain.setTime("2014-2-14");
				bargain.setBuyerId("15");
				bargain.setPrice(Integer.valueOf(30));
				bargain.setTimeOk("2014-2-14");
				bargains2.add(bargain3);
				Record record2 = new Record("2014-2-1", bargains2);
				ArrayList<Record> record3 = new ArrayList<Record>();
				record3.add(record);
				record3.add(record2);

				Seller seller = new Seller();
				seller.setUrl("");
				Log.v("加载bargain数据", bargain.toString());
				mAdapter = new RecordAdapter(ActivitySellerRecord.this,
						record3, seller);
				listView.setAdapter(mAdapter);
				Toast.makeText(ActivitySellerRecord.this, "加载数据失败",
						Toast.LENGTH_SHORT).show();

			}
			super.onPostExecute(result);
		}

		@Override
		protected Integer doInBackground(Void... arg0) {
			try {
				ArrayList<Object> bargains = seller.getSuccessBargains();
				sellerInfo = (Seller) bargains.get(0);
				url = sellerInfo.getUrl().replace("#", "");
				sellerInfo.setUrl(url);
				Log.v("url=", url);

				@SuppressWarnings("unchecked")
				ArrayList<List<Bargain>> arrayList = (ArrayList<List<Bargain>>) bargains
						.get(1);
				records = new ArrayList<Record>();
				for (int i = 0; i < arrayList.size(); i++) {
					ArrayList<Bargain> bargain = (ArrayList<Bargain>) arrayList
							.get(i);

					Record record = new Record(bargain.get(0).getTimeOk(),
							(ArrayList<Bargain>) bargain);

					records.add(record);

				}
				System.out.println("size的大小＝" + records.size());
				return 1;

			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
		}
	}

	private class PageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return listViewPage.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		// destroyItem（）：从当前container中删除指定位置（position）的View;
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
			// container.removeView(listViewPage.get(position));

		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(listViewPage.get(position));

			return listViewPage.get(position);
		}
		// instantiateItem()：做了两件事，第一：将当前视图添加到container中，第二：返回当前View

	}

	private class AdapterBuyer extends BaseAdapter {

		@Override
		public int getCount() {
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			return null;
		}
		private class ViewHold{
			ImageView imageView;
			
		}

	}

}
