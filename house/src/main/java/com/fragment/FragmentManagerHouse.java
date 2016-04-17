package com.fragment;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.beans.House;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ma.activity.ActivityHouseInfo;
import com.ma.interfac.HouseInterface;
import com.ma.service.ServiceHouse;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.special.ResideMenuDemo.R;
import com.utl.ImageLoadHouseFactory;
import com.utl.Url;

public class FragmentManagerHouse extends Fragment {
	public static ImageLoader imageLoader;// 图片加载类
	
	private DisplayImageOptions options;// 图片加载设置类
	
	private HouseInterface getAllHouse;// 该接口获取所有房源
	
	private ImageSwitcher switcher;
	
	private List<House> listHouse=new ArrayList<House>();
	
	private View parentView;
	
	private PullToRefreshListView  mPullRefreshListView;
	
	private SeHouseAdapter adapter;
	
	private ListView listView;

	private LayoutInflater inflater;
	
	private ImageView switch_imaImageView;

	
	private int[] image_id = new int[] { R.drawable.swtich1,
			R.drawable.swtich2, R.drawable.swtich3, R.drawable.switch4,
			R.drawable.swtich5 };// image自动显示图片
	private int index = 0;// 当前图片索引
	private GestureDetector gestureDetector;
	private GestureDetector.OnGestureListener onGestureListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		parentView = inflater
				.inflate(R.layout.sefragmenthome, container, false);
		this.inflater = inflater;
		// Log.v("测试哪个地方没有数据出现", "这里");//
		init();
		SetImageSiwtch();
		switcher.setImageResource(image_id[index]);
		// // 进来就刷新
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				mPullRefreshListView.setRefreshing();
			}
		}, 500);

		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						if (listHouse != null && listHouse.size() != 0) {
							listHouse.clear();// 清除数据

							new Get_data().execute(1);
						} else {
							new Get_data().execute(1);
						}

					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT)
						// .show();
						// new Get_data().execute(0);
					}
				});
		mPullRefreshListView// 设置最后一个的监听器
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						Toast.makeText(parentView.getContext(), "已经加载了所有数据",
								Toast.LENGTH_SHORT).show();
					}
				});

		return parentView;
	}

	private void SetImageSiwtch() {// 设置图像切换器

		switcher.setInAnimation(AnimationUtils.loadAnimation(getActivity(),
				android.R.anim.fade_in));
		switcher.setOutAnimation(AnimationUtils.loadAnimation(getActivity(),
				android.R.anim.fade_out));
		switcher.setFactory(new ViewFactory() {// 该借口用于创建显示的imageview

			@SuppressWarnings("deprecation")
			@Override
			public View makeView() {
				switch_imaImageView = new ImageView(getActivity());
				switch_imaImageView.setScaleType(ImageView.ScaleType.FIT_XY);// 保持横纵比例缩放
				switch_imaImageView
						.setLayoutParams(new ImageSwitcher.LayoutParams(
								LayoutParams.FILL_PARENT,
								LayoutParams.WRAP_CONTENT));
				return switch_imaImageView;
			}
		});

	}


	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}

	private void init() {// 初始化

		onGestureListener = new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				float x = e2.getX() - e1.getX();
				// float y = e2.getY() - e1.getY();

				if (x > 0) {
					switcher.setImageResource(image_id[4]);
					Log.v("左边", "");
				} else if (x < 0) {
					switcher.setImageResource(image_id[4]);

				}
				return true;
			}
		};
		
		new ImageLoadHouseFactory(getActivity(), 3);
		imageLoader = ImageLoadHouseFactory.getImageLoader();
		options = new ImageLoadHouseFactory(getActivity(), 3)
				.getOptions();
		
		
		

		gestureDetector = new GestureDetector(getActivity(), onGestureListener);// 注册监听手势

		mPullRefreshListView = (PullToRefreshListView) parentView
				.findViewById(R.id.lv_se_house);
		ILoadingLayout attr = mPullRefreshListView.getLoadingLayoutProxy();// 获取加载布局代理对象
		attr.setPullLabel("下拉可以刷新");
		attr.setRefreshingLabel("数据加载中，请等待");
		attr.setReleaseLabel("放开将会刷新加载");
		switcher = (ImageSwitcher) parentView.findViewById(R.id.imageSwitcher1);// 初始化
		adapter = new SeHouseAdapter();
		// list_house.add(new House("时光小屋", "重庆市南岸区北滨路", "环境优美，气质动人", 90, 20,
		// 1350000));

		listView = mPullRefreshListView.getRefreshableView();// 获取刷新视图
		// Log.v("资料测试", "添加成功" + list_house.get(1).getLocation());
		listHouse = new ArrayList<House>();// 显示的list数据
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("house",
						(Serializable) listHouse.get(position - 1));
				Intent intent = new Intent(getActivity(),
						ActivityHouseInfo.class);
				intent.putExtras(bundle);
				startActivity(new Intent(intent));
			}
		});
	}

	private class Get_data extends AsyncTask<Integer, String, Integer> {

		@Override
		protected void onPostExecute(Integer result) {
			if (result == 1) {
				try {
					listHouse.size();

				} catch (NullPointerException e) {
					Toast.makeText(getActivity(), "连接超时，数据获取失败",
							Toast.LENGTH_SHORT).show();
				}
				adapter.notifyDataSetChanged();// 通知适配器资料改变
				mPullRefreshListView.onRefreshComplete();

			} else {
				String[] aStrings = new String[] { "a", "b" };
				listHouse.add(new House(aStrings, 27, "kobe", "kobe2", "kobe3",
						34, 44, 43));
				listHouse.add(new House(aStrings, 26, "kobe", "kobe2", "kobe3",
						34, 44, 43));
				listHouse.add(new House(aStrings, 25, "kobe", "kobe2", "kobe3",
						34, 44, 43));
				listHouse.add(new House(aStrings, 24, "kobe", "kobe2", "kobe3",
						34, 44, 43));
				listHouse.add(new House(aStrings, 23, "kobe", "kobe2", "kobe3",
						34, 44, 43));
				listHouse.add(new House(aStrings, 22, "kobe", "kobe2", "kobe3",
						34, 44, 43));
				listHouse.add(new House(aStrings, 28, "kobe", "kobe2", "kobe3",
						34, 44, 43));
				Log.v("listHouse size", String.valueOf(listHouse.size()));
				Toast.makeText(getActivity(), "连接超时，数据获取失败", Toast.LENGTH_SHORT)
						.show();
				adapter.notifyDataSetChanged();// 通知适配器资料改变
				mPullRefreshListView.onRefreshComplete();
			}

			super.onPostExecute(result);
		}

		@Override
		protected Integer doInBackground(Integer... arg0) {
			if (arg0[0] == 1) {
				try {
					Thread.sleep(2000);
					getAllHouse = new ServiceHouse();
					try {
						listHouse = getAllHouse.getAllHouses();// 获取所有房源
						Log.v("获取房源信息", "连接服务器成功");
						return 1;
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						Log.v("获取房源信息，json转换异常", "");
						e.printStackTrace();
					} catch (IOException e) {
						Log.v("获取房源信息时，流异常", "");
						e.printStackTrace();
					}

				} catch (InterruptedException e) {
				}

			}

			return 0;
		}
	}

	//

	public class Asyn_Switch extends AsyncTask<Void, Void, Integer> {// SWITCH转换的线程
		@Override
		protected void onPostExecute(Integer result) {

			super.onPostExecute(result);
		}

		@Override
		protected Integer doInBackground(Void... arg0) {
			try {
				Thread.sleep(2000);//
				Log.v("线程休息2秒", String.valueOf(index));
				if (index > 0) {
					index = index - 1;
				} else {
					index = image_id.length - 1;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Log.v("swtich 转换的index 值", String.valueOf(index));

			return index;
		}
	}

	private class SeHouseAdapter extends BaseAdapter {

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
			class ViewHold {
				ImageView image;
				TextView tv_house_name, tv_house_size, tv_house_price;

			}
			ViewHold viewHold;
			if (arg1 == null) {
				viewHold = new ViewHold();
				arg1 = inflater.inflate(R.layout.listitemhouseinfo, null);// inflater
																			// convert
				viewHold.image = (ImageView) arg1.findViewById(R.id.img_house);
				viewHold.tv_house_name = (TextView) arg1
						.findViewById(R.id.tv_house_info_location);
				viewHold.tv_house_size = (TextView) arg1
						.findViewById(R.id.tv_house_info_size);
				viewHold.tv_house_price = (TextView) arg1
						.findViewById(R.id.tv_house_info_price);
				arg1.setTag(viewHold);

			}
			viewHold = (ViewHold) arg1.getTag();
			imageLoader.displayImage("http://"+Url.url+"/"+listHouse.get(arg0).getPicture_url()[0],
					viewHold.image, options);// 加载图片
			viewHold.tv_house_name.setText(listHouse.get(arg0).getLocation());
			viewHold.tv_house_price.setText(String.valueOf(listHouse.get(arg0)
					.getPrice()));
			viewHold.tv_house_size.setText(String.valueOf(listHouse.get(arg0)
					.getSize()));
			return arg1;
		}
	}

	public Drawable BitmapDrawable(Bitmap btp) {
		// TODO Auto-generated method stub
		return null;
	}
}
