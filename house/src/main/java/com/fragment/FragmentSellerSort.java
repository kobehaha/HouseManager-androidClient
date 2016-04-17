package com.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.beans.Seller;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.special.ResideMenuDemo.R;

public class FragmentSellerSort extends Fragment {
	public static ImageLoader imageLoader = ImageLoader.getInstance();// 图片加载类
	private DisplayImageOptions options;// 图片加载设置类
	private View view;
	private PullToRefreshListView listView;
	private ListView mlistView;
	private List<Seller> mList = new ArrayList<Seller>();
	private AdapterSort adapterSort;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.sefragmentsort, container, false);
		init();
		// listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
		//
		// @Override
		// public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		//
		// }
		// });

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.v("data", String.valueOf(mList.size()));
	}

	private void init() {
		listView = (PullToRefreshListView) view.findViewById(R.id.lv_se_sort);
		mlistView = listView.getRefreshableView();
		config();
		mlistView.setAdapter(new AdapterSort());
		adapterSort = new AdapterSort();
		mlistView.setAdapter(adapterSort);

		new AsynGetDate().execute();

	}

	@SuppressWarnings("deprecation")
	private void config() {// 配置文件

		File cacheDir = StorageUtils.getOwnCacheDirectory(// 设置缓存
				getActivity(), "imageloader/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(// 配置文件
				getActivity())
				.threadPoolSize(4)
				.// 线程池大小
				threadPriority(Thread.NORM_PRIORITY - 2)
				.// 线程优先级
				tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCache(new UnlimitedDiskCache(cacheDir))
				.denyCacheImageMultipleSizesInMemory().build();

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageOnFail(R.drawable.ic_error)
				.showImageForEmptyUri(R.drawable.ic_empty).cacheOnDisc(true)
				// 设置缓存到sd，内存中
				.displayer(new RoundedBitmapDisplayer(100)).cacheInMemory(true)
				.considerExifParams(true).build();// 设置不同形态的图片
		imageLoader.init(config);

	}

	private class AsynGetDate extends AsyncTask<Void, Void, Void> {
		Seller seller;

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			adapterSort.notifyDataSetChanged();
			Log.v("更新完成", String.valueOf(mList.size()));
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			String picture = "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=788553091,1475641975&fm=58";
			for (int i = 0; i < 5; i++) {
				seller = new Seller();
				seller.setName("张志远" + String.valueOf(i));
				seller.setPhone(i);
				seller.setMothCount(i);

				seller.setUrl(picture);
				mList.add(seller);

			}

			return null;
		}
	}

	private class AdapterSort extends BaseAdapter {

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ViewHold viewHold = null;
			if (arg1 == null) {
				arg1 = LayoutInflater.from(getActivity()).inflate(
						R.layout.listsellersortitem, null);
				viewHold = new ViewHold();
				viewHold.imgView = (ImageView) arg1
						.findViewById(R.id.img_seller);
				viewHold.tvCount = (TextView) arg1
						.findViewById(R.id.seller_count1);
				viewHold.tvName = (TextView) arg1
						.findViewById(R.id.seller_name);
				viewHold.tvPhone = (TextView) arg1
						.findViewById(R.id.seller_phone);
				arg1.setTag(viewHold);
				Log.v("view 不为空", arg1.toString());

			}
			if (arg1 != null) {
				viewHold = (ViewHold) arg1.getTag();
				viewHold.tvCount.setText(String.valueOf(mList.get(arg0)
						.getMothCount()));// 把一个int型业务数据的 设置setText（）或者类似的方法中，
											// 这样Android系统就会主动去资源文件当中寻找
				viewHold.tvPhone.setText(String.valueOf(mList.get(arg0)
						.getPhone()));
				imageLoader.displayImage(mList.get(arg0).getUrl(),
						viewHold.imgView, options);// 加载图片
				viewHold.tvName.setText(mList.get(arg0).getName());
				Log.v("设置界面item", arg1.toString());

			}

			return arg1;
		}

		private class ViewHold {
			ImageView imgView;
			TextView tvCount, tvName, tvPhone;

		}

	}

}
