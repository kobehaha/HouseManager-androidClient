package com.se.house;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adpter.AdapterSellerChooseHouse;
import com.adpter.AdapterSellerChooseHouse.ViewHold;
import com.beans.House;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ma.interfac.HouseInterface;
import com.ma.service.ServiceHouse;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.special.ResideMenuDemo.R;
import com.utl.ImageLoadHouseFactory;

public class ActivitySellerModifyChooseHouse extends BasicActivity {
	private Button btn_choose_all, btn_choose_no, btn_choose_clear, btn_ok;

	private ImageView img_back;

	private PullToRefreshListView listView;

	private List<House> listHouse = new ArrayList<House>();

	private HouseInterface getAllHouse;

	private AdapterSellerChooseHouse adapter;// 适配器

	private TextView tv_choose_number;

	private int choose_number;

	private ImageLoader imageLoader;

	private DisplayImageOptions options;

	private List<House> list_client = new ArrayList<House>();

	private ArrayList<CharSequence> mArrayList;// 保存上次界面传过来的id;

	private int[] choosed_id;

	@TargetApi(19)
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seactivitychoosehouse);
		get_intent();// 获取上次传递的数据
		new Get_data().execute(1);// 初始化完就加载
		init();

	}

	private void get_intent() {
		// Bundle
		Bundle bundle = getIntent().getExtras();
		if (bundle.getBoolean("save_sure")) {// 添加客户房源
			mArrayList = bundle.getCharSequenceArrayList("save_id");
			Log.v("marraylist的长度", String.valueOf(mArrayList.size()));
			choosed_id = new int[mArrayList.size()];
			for (int i = 0; i < choosed_id.length; i++) {
				choosed_id[i] = Integer.valueOf((String) mArrayList.get(i));

			}

		} else {
			Log.v("?????", String.valueOf(11));
		}

	}

	private void init() {

		new ImageLoadHouseFactory(ActivitySellerModifyChooseHouse.this, 3);
		imageLoader = ImageLoadHouseFactory.getImageLoader();
		options = new ImageLoadHouseFactory(
				ActivitySellerModifyChooseHouse.this, 3).getOptions();

		img_back = (ImageView) findViewById(R.id.back8);
		btn_ok = (Button) findViewById(R.id.btn_choose_house_ok);
		tv_choose_number = (TextView) findViewById(R.id.btn_choose_number);
		listView = (PullToRefreshListView) findViewById(R.id.lv_se_choose_house);
		btn_choose_all = (Button) findViewById(R.id.btn_choose_all);
		btn_choose_clear = (Button) findViewById(R.id.btn_choose_clear);
		btn_choose_no = (Button) findViewById(R.id.btn_choose_no);

		ILoadingLayout layout = listView.getLoadingLayoutProxy();
		layout.setRefreshingLabel("");
		layout.setReleaseLabel("");
		layout.setPullLabel("");
		// Drawable drawable = new BitmapDrawable(R.drawable.load);
		// layout.setLoadingDrawable(R.drawable.load);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				for (int i = 0; i < list_client.size(); i++) {
					bundle.putSerializable(String.valueOf(i),
							(Serializable) list_client.get(i));

				}
				bundle.putInt("size", list_client.size());
				Log.v("ac_se_insert_choose", String.valueOf(list_client.size()));
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
			}
		});

		img_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});
		btn_choose_no.setOnClickListener(new OnClickListener() {// 反选择

					@Override
					public void onClick(View arg0) {
						for (int i = 0; i < listHouse.size(); i++) {
							if (AdapterSellerChooseHouse.getIsSelected().get(i)) {
								AdapterSellerChooseHouse.getIsSelected().put(i,
										false);
								choose_number--;
								list_client.remove(listHouse.get(i));

							} else {
								AdapterSellerChooseHouse.getIsSelected().put(i,
										true);
								choose_number++;
								list_client.add(listHouse.get(i));
							}

						}
						refresh_view();

					}

				});
		btn_choose_clear.setOnClickListener(new OnClickListener() {// 全取消

					@Override
					public void onClick(View arg0) {
						for (int i = 0; i < listHouse.size(); i++) {
							if (AdapterSellerChooseHouse.getIsSelected().get(i)) {
								AdapterSellerChooseHouse.getIsSelected().put(i,
										false);

							}

						}
						choose_number = 0;
						list_client.clear();
						refresh_view();

					}
				});
		btn_choose_all.setOnClickListener(new OnClickListener() {// 全选

					@Override
					public void onClick(View arg0) {
						for (int i = 0; i < listHouse.size(); i++) {

							AdapterSellerChooseHouse.getIsSelected().put(i,
									true);

						}
						choose_number = listHouse.size();
						list_client.addAll(listHouse);
						refresh_view();

					}
				});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤

				ViewHold holder = (ViewHold) arg1.getTag();
				holder.checkBox.toggle();
				AdapterSellerChooseHouse.getIsSelected().put(arg2,
						holder.checkBox.isChecked());// 设置选中

				if (AdapterSellerChooseHouse.getIsSelected().get(arg2) == true) {
					choose_number++;
					list_client.add(listHouse.get(arg2 - 1));// 增加客户偏好房源
				} else {
					choose_number--;
					list_client.remove(listHouse.get(arg2 - 1));// 移除偏好房源
				}
				tv_choose_number.setText("您选中了" + String.valueOf(choose_number)
						+ "个房源");

			}
		});

	}

	protected void refresh_view() {
		tv_choose_number
				.setText("您选中了" + String.valueOf(choose_number) + "个房源");
		adapter.notifyDataSetChanged();

	}

	public class Get_data extends AsyncTask<Integer, String, Integer> {
		ProgressDialog dialog = ProgressDialog.show(
				ActivitySellerModifyChooseHouse.this, "提示", "加载数据中，请稍等", false,
				true);

		@Override
		protected void onPostExecute(Integer result) {
			dialog.dismiss();

			if (result == 1) {
				adapter = new AdapterSellerChooseHouse(
						// 绑定
						ActivitySellerModifyChooseHouse.this, listHouse,
						imageLoader, options);

				listView.setAdapter(adapter);// 设定设备

				adapter.notifyDataSetChanged();// 通知适配器资料改变
				listView.onRefreshComplete();
				Log.v("house.size", String.valueOf(listHouse.size()));

			} else {
				Toast.makeText(ActivitySellerModifyChooseHouse.this,
						"连接超时，数据获取失败", Toast.LENGTH_SHORT).show();

			}

			super.onPostExecute(result);
		}

		@Override
		protected Integer doInBackground(Integer... arg0) {
			int result = 0;
			if (arg0[0] == 1) {
				try {
					getAllHouse = new ServiceHouse();// 获取服务
					listHouse = getAllHouse.getAllHouses();
					if (choosed_id != null) {
						for (int i = 0; i < choosed_id.length; i++) {
							Log.v("choose_id", String.valueOf(i));
							for (int j = 0; j < listHouse.size(); j++) {
								if (choosed_id[i] == listHouse.get(j).getId()) {

									listHouse.remove(j);
									Log.v("移除成功", String.valueOf(i));

								}
							}
						}

					}
					result = 1;

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			return result;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

}
