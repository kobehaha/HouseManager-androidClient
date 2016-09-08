package com.se.house;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import android.app.ActionBar.LayoutParams;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.beans.Bargain;
import com.beans.Client;
import com.beans.House;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.se.house.DialogBargain.Builder;
import com.se.inter.use.SellerInterface;
import com.se.service.ServiceSeller;
import com.special.ResideMenuDemo.R;
import com.utl.ImageLoadClientFactory;
import com.utl.ImageLoadHouseFactory;
import com.utl.Url;

/***
 * 
 * @author kobe 该页面是获取所有的交易信息，和买家个人信息 ，能进入交易界面
 * 
 */
public class ActivitySellerBuyerRecord extends BasicActivity {

	private PopupWindow popupWindow;

	private Button btnPop, btnSure, btnBack;

	private ImageView mImageView;

	private TextView bargainTime, bargainTimeOk, bargainPrice, buyerName,
			buyerSex, buyerAge, buyerPhone, buyerWeichartTextView;

	private int age;
	private List<House> arrayListHouses;
	private PullToRefreshListView mPullRefreshListView;// 下拉菜单的listview

	private int buyerId;

	private SellerInterface getSellerInterface = new ServiceSeller();// seller接口

	private ListView listView;

	private ImageLoader imageLoader, imageLoader2;// 图片加载起

	private DisplayImageOptions options, options2;// 属性器

	private Map<String, Object> object;// 数据加载

	private Bargain bargain;// 交易信息

	private Client client;// 客户信息

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seactivitysellerrecordmain);
		getBuyerId();
		init();
		new AsnyLoads().execute(buyerId);// 加载数据
	}

	private void getBuyerId() {// 获取上个activity的数据
		buyerId = getIntent().getExtras().getInt("buyerId");

	}

	private void init() {// 初始化

		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_houseinfo);
		ILoadingLayout attr = mPullRefreshListView.getLoadingLayoutProxy();// 获取加载布局代理对象
		attr.setPullLabel("下拉可以刷新");
		attr.setRefreshingLabel("数据加载中，请等待");
		attr.setReleaseLabel("放开将会刷新加载");
		listView = mPullRefreshListView.getRefreshableView();// 获取刷新视图

		btnPop = (Button) findViewById(R.id.btn_show_buyer_choose_option);

		new ImageLoadHouseFactory(ActivitySellerBuyerRecord.this, 3);
		imageLoader = ImageLoadHouseFactory.getImageLoader();
		options = new ImageLoadHouseFactory(ActivitySellerBuyerRecord.this, 3)
				.getOptions();

		new ImageLoadClientFactory(ActivitySellerBuyerRecord.this, 3);
		imageLoader2 = ImageLoadClientFactory.getImageLoader();
		options2 = new ImageLoadClientFactory(ActivitySellerBuyerRecord.this, 3)
				.getOptions();

		bargainPrice = (TextView) findViewById(R.id.tv_bargainPrice);
		bargainTime = (TextView) findViewById(R.id.tv_bargainTime);
		bargainTimeOk = (TextView) findViewById(R.id.tv_bargainTimeOk);
		buyerAge = (TextView) findViewById(R.id.buyer_age);
		buyerName = (TextView) findViewById(R.id.buyer_name);
		buyerPhone = (TextView) findViewById(R.id.buyer_phone);
		buyerWeichartTextView = (TextView) findViewById(R.id.buyer_weichart);
		buyerSex = (TextView) findViewById(R.id.buyer_sex);
		mImageView = (ImageView) findViewById(R.id.iv_seller_ico);

		// 设置listview 监听器

		btnPop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				show_pop(v);

			}
		});

		// btnSure.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// ShowDialog();
		// }
		// }
		//
		// );
	}

	private void show_pop(View view2) {// 显示popwindow
		View view = LayoutInflater.from(this).inflate(
				R.layout.popupwindowsellerbuyer, null);
		view.findViewById(R.id.deal).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ShowDialog();
				Toast.makeText(ActivitySellerBuyerRecord.this, "交易",
						Toast.LENGTH_SHORT).show();

			}
		});
		view.findViewById(R.id.delete).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						ShowDeleteDialog();
						Toast.makeText(ActivitySellerBuyerRecord.this, "删除",
								Toast.LENGTH_SHORT).show();

					}
				});
		view.findViewById(R.id.modify).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Toast.makeText(ActivitySellerBuyerRecord.this, "修改",
								Toast.LENGTH_SHORT).show();

						ShowDialogModify();// 显示dislog

					}
				});
		popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);
		popupWindow.setOutsideTouchable(false);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 设置背景
		popupWindow.showAsDropDown(view2, 0, 20);// 设置位置
		popupWindow.update();

	}

	public void ShowDialogModify() {// 显示修改信息dialog

		final com.se.house.DialogModify.Builder builder = new DialogModify.Builder(
				ActivitySellerBuyerRecord.this);
		builder.setPositiveButton("修改房源信息",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						Toast.makeText(ActivitySellerBuyerRecord.this,
								"修改房源信息", Toast.LENGTH_SHORT).show();

						Intent intent = new Intent();
						Bundle bundle = new Bundle(); // 不能用intent.getextras获取bundle
														// nullpol....

						Log.v("测试数据serializable",
								String.valueOf(arrayListHouses.size()));
						bundle.putInt("buyerId", client.getId());
						bundle.putSerializable("houseList",
								(Serializable) arrayListHouses);// 传递house信息
						intent.putExtras(bundle);
						intent.setClass(ActivitySellerBuyerRecord.this,
								ActivitySellerClientModify.class);
						startActivity(intent);// 启动

					}
				});

		// 不用修改个人信息
		// builder.setNegativeButton("修改个人信息",
		// new android.content.DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int which) {
		// Toast.makeText(ActivitySellerBuyerRecord.this,
		// "修改个人信息", Toast.LENGTH_SHORT).show();
		//
		// Intent intent = new Intent(
		// ActivitySellerBuyerRecord.this,
		// ActivitySellerInsertClientInfo.class);
		// // Bundle bundle = new Bundle();
		// // bundle.putIntArray("all_house_id", arry);
		// // intent.putExtras(bundle);
		// startActivity(intent);
		//
		// }
		// });

		builder.create().show();

	}

	public void ShowDialog() {// 确定dialog

		final Builder builder = new DialogBargain.Builder(
				ActivitySellerBuyerRecord.this);
		builder.setTitle("提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				// String a = Builder.editText.getText().toString();
				Toast.makeText(ActivitySellerBuyerRecord.this, "选择",
						Toast.LENGTH_SHORT).show();

				dialog.dismiss();

			}
		});

		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builder.create().show();

	}

	public void ShowDeleteDialog() {// 删除dialog
		final com.se.house.DialogDeleteBuyer.Builder builder = new DialogDeleteBuyer.Builder(
				ActivitySellerBuyerRecord.this);
		builder.setTitle("提示");
		builder.setMessage("是否删除改买家");

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				new AsynDelete().execute(buyerId);
				finish();

			}
		});

		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builder.create().show();

	}

	private class SeHouseAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public SeHouseAdapter() {

			inflater = LayoutInflater.from(ActivitySellerBuyerRecord.this);

		}

		@Override
		public int getCount() {
			return arrayListHouses.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arrayListHouses.get(arg0);
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

			imageLoader.displayImage("http://" + Url.url + "/"
					+ arrayListHouses.get(arg0).getPicture_url()[0],
					viewHold.image, options);// 加载图片

			viewHold.tv_house_name.setText(arrayListHouses.get(arg0)
					.getLocation());
			viewHold.tv_house_price.setText(String.valueOf(arrayListHouses.get(
					arg0).getPrice()));
			viewHold.tv_house_size.setText(String.valueOf(arrayListHouses.get(
					arg0).getSize()));
			return arg1;
		}
	}

	private class AsnyLoads extends AsyncTask<Integer, Integer, Integer> {
		private ProgressDialog dialog;

		public AsnyLoads() {
			dialog = ProgressDialog.show(ActivitySellerBuyerRecord.this,
					"正在获取信息", "稍等");

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			dialog.setProgress(values[0]);
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if (result == 1) {

				SeHouseAdapter adapter = new SeHouseAdapter();

				arrayListHouses = (List<House>) object.get("houseinfo");// 初始化信息
				bargain = (Bargain) object.get("bargaininfo");
				client = (Client) object.get("buyerinfo");

				if (bargain.getPrice() == 0) {
					bargainPrice.setText("交易中");
				}

				bargainPrice.setText(String.valueOf(bargain.getPrice()));// 设置信息

				bargainTime.setText(bargain.getTime());
				if (bargain.getTimeOk().equals("0000-00-00")) {
					bargainTimeOk.setText("交易中");
				} else {
					bargainTimeOk.setText("交易完成");
				}

				buyerAge.setText(String.valueOf(client.getAge())); // 设置age
				buyerName.setText(client.getName());
				buyerSex.setText(client.getSex());
				buyerWeichartTextView.setText(client.getWeichart());
				buyerPhone.setText(String.valueOf(client.getPhone()));

				imageLoader2.displayImage(
						"http://" + Url.url + "/" + client.getUrl(),
						mImageView, options2);

				listView.setOnItemClickListener(new OnItemClickListener() {// 设置监听器

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent intent = new Intent(
								ActivitySellerBuyerRecord.this,
								ActivitySellerDeal.class);
						Bundle bundle = new Bundle();
						bundle.putInt("houseId", arrayListHouses.get(arg2 - 1)
								.getId());
						bundle.putInt("buyerId", client.getId());
						intent.putExtras(bundle);
						startActivity(intent);

					}

				});

				// listView.setOnItemClickListener(new OnItemClickListener() {
				//
				// @Override
				// public void onItemClick(AdapterView<?> arg0, View arg1,
				// int arg2, long arg3) {
				// startActivity(new Intent(getActivity(),
				// ActivitySellerBuyerRecord.class));
				// }
				// });

				listView.setAdapter(adapter);
				Toast.makeText(ActivitySellerBuyerRecord.this, "数据加载完成",
						Toast.LENGTH_SHORT).show();

			} else {
				Toast.makeText(ActivitySellerBuyerRecord.this, "数据加载失败",
						Toast.LENGTH_SHORT).show();

			}
		}

		@Override
		protected Integer doInBackground(Integer... params) {// 加载出数据马上获取
			int result;
			try {
				object = getSellerInterface.getBuyerRecord(params[0]);
				result = 1;
			} catch (Exception e) {
				result = 0;
				e.printStackTrace();
			}

			return result;
		}

	}

	public class AsynDelete extends AsyncTask<Integer, Integer, Boolean> {
		private ProgressDialog pdialog;

		public AsynDelete() {
			pdialog = ProgressDialog.show(ActivitySellerBuyerRecord.this,
					"正在加载", "正在删除");
		}

		@Override
		protected void onPostExecute(Boolean result) {

			pdialog.dismiss();

			super.onPostExecute(result);
			if (result) {

				Toast.makeText(ActivitySellerBuyerRecord.this, "删除成功",
						Toast.LENGTH_SHORT).show();

			} else {

				Toast.makeText(ActivitySellerBuyerRecord.this, "删除失败",
						Toast.LENGTH_SHORT).show();

			}
		}

		@Override
		protected Boolean doInBackground(Integer... arg0) {

			boolean flag = false;
			try {
				flag = getSellerInterface.deleteBuyerById(arg0[0]);

			} catch (Exception e) {
				e.printStackTrace();

			}
			return flag;

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			pdialog.setProgress(values[0]);
		}

	}

}
