package com.ma.activity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beans.Notice;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ma.interfac.NoticeInterface;
import com.ma.service.ServiceNotice;
import com.se.house.BasicActivity;
import com.special.ResideMenuDemo.R;
import com.utl.ServletPath;

public class ActivitySendNotice extends BasicActivity {
	private NoticeInterface noticeInterface;
	private ImageView img_back;
	private EditText ed_notice;
	private Button btn_send;
	private String str_notice, str_time;
	private PullToRefreshListView lv_notice;// 下拉刷新
	private LayoutInflater inflater;
	// private List<Notice> list_show;
	private List<Notice> list_save;
	private Adapter_notice adapter;

	// private static int i = 0;

	@TargetApi(19)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.maactivitysendnotice);
		init();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				lv_notice.setRefreshing();
			}
		}, 500);

		btn_send.setOnClickListener(new OnClickListener() {

			@SuppressLint("SimpleDateFormat")
			@Override
			public void onClick(View arg0) {
				SimpleDateFormat format = new SimpleDateFormat(// 获取时间
						"yyyy年MM月dd日HH:mm:ss");
				Date str_data = new Date(System.currentTimeMillis());
				str_time = format.format(str_data);
				Log.v("测试当前时间", "当前时间是" + str_time);
				str_notice = ed_notice.getText().toString();
				if (str_notice.equals("")) {
					Toast.makeText(ActivitySendNotice.this, "对不起，您输入的内容为空",
							Toast.LENGTH_SHORT).show();

				} else {
					new AsynSendNotice().execute();
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							lv_notice.setRefreshing();
						}
					}, 500);

					// new AsynGetNotice().execute(1);

				}

			}
		});
	}

	private void init() {
		list_save = new ArrayList<Notice>();
		img_back = (ImageView) findViewById(R.id.back2);
		ed_notice = (EditText) findViewById(R.id.ed_ma_send_notice);
		btn_send = (Button) findViewById(R.id.btn_ma_send_sure);
		noticeInterface = new ServiceNotice();
		inflater = getLayoutInflater();
		adapter = new Adapter_notice();
		// list_show = new ArrayList<Notice>();
		lv_notice = (PullToRefreshListView) findViewById(R.id.lv_ma_notice);// 下拉刷新
		ILoadingLayout attr = lv_notice.getLoadingLayoutProxy();// 获取加载布局代理对象
		attr.setPullLabel("下拉可以刷新");
		attr.setRefreshingLabel("数据加载中，请等待");
		attr.setReleaseLabel("放开将会刷新加载");
		lv_notice.setAdapter(adapter);// 设置适配器
		img_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// i = 0;
				finish();
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						lv_notice.setRefreshing();
						new AsynGetNotice().execute(1);
						list_save.clear();
					}
				}, 1500);

			}
		});

		lv_notice.setOnRefreshListener(new OnRefreshListener2<ListView>() {// 增加监听事件

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						list_save.clear();
						new AsynGetNotice().execute(1);

					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						list_save.clear();
						new AsynGetNotice().execute(1);

					}
				});
		lv_notice.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				Toast.makeText(ActivitySendNotice.this, "已经加载了所有数据",
						Toast.LENGTH_SHORT).show();

			}
		});

	}

	public class AsynGetNotice extends AsyncTask<Integer, Void, Integer> {// 异步任务，加载数据
		@Override
		protected void onPostExecute(Integer result) {
			// if (result == 2) {
			// Toast.makeText(ActivitySendNotice.this, "加载成功",
			// Toast.LENGTH_SHORT).show();
			// list_show.addAll(list_save);
			// adapter.notifyDataSetChanged();// 通知适配器资料改变
			// // Call onRefreshComplete when the list has been refreshed.
			// lv_notice.onRefreshComplete();
			//
			// }
			if (result == 1) {
				// list_show.addAll(list_save);
				adapter.notifyDataSetChanged();// 通知适配器资料改变
				// Call onRefreshComplete when the list has been refreshed.
				lv_notice.onRefreshComplete();
				// Toast.makeText(ActivitySendNotice.this, "加载网络数据失败",
				// Toast.LENGTH_SHORT).show();

			}
			// else {
			// if (i < list_save.size()) {
			// list_show.add(list_save.get(i));// 每次假设加载三条数据
			// i = i + 1;
			// if (i < list_save.size() - 1) {
			// list_show.add(list_save.get(i + 1));
			// i = i + 1;
			// if (i < list_save.size() - 2) {
			// list_show.add(list_save.get(i + 2));
			// i = i + 1;
			// }
			//
			// }
			// Toast.makeText(ActivitySendNotice.this, "加载网络数据失败",
			// Toast.LENGTH_SHORT).show();
			//
			// }
			//
			// adapter.notifyDataSetChanged();// 通知适配器资料改变
			// // Call onRefreshComplete when the list has been refreshed.
			// lv_notice.onRefreshComplete();
			//
			// super.onPostExecute(result);
			// }
		}

		@Override
		protected Integer doInBackground(Integer... arg0) {

			Notice notice;
			list_save.add(new Notice(1000000, "2015-2-14", "这个月的业绩必须达到20套"));
			list_save.add(new Notice(9999999, "2015-10-14", "批评小刘这个月业绩为1"));
			list_save.add(new Notice(11111111, "2015-9-14", "默认数据"));

			try {
				for (int i = 0; i < ((NoticeInterface) noticeInterface)
						.get_notice_info().size(); i++) {
					notice = ((NoticeInterface) noticeInterface)
							.get_notice_info().get(i);// 获取资料
					// Log.v("从网络上和自己数据的总和", )

					list_save.add(notice);

				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

			// if (arg0[0] == 1) {
			// try {
			// Thread.sleep(2000);
			// if (list_save.size() > 9) {
			// return 2;
			//
			// } else {
			// return 1;
			// }
			//
			// } catch (InterruptedException e) {
			// }
			//
			// }

			return 1;
		}
	}

	private class AsynSendNotice extends AsyncTask<Void, Void, String> {// 异步任务，发送通知
		@Override
		protected void onPostExecute(String result) {
			if (ServletPath.SUCCESS.equalsIgnoreCase((String) result)) {
				Toast.makeText(ActivitySendNotice.this, "发送成功",
						Toast.LENGTH_SHORT).show();

			} else {

				Toast.makeText(ActivitySendNotice.this, "发送失败,请检查你的网络",
						Toast.LENGTH_SHORT).show();

			}

			super.onPostExecute(result);
		}

		@Override
		protected String doInBackground(Void... arg0) {
			String str_sure = "0";
			try {
				str_sure = noticeInterface.send_notice(str_notice, str_time);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return str_sure;
		}

	}

	private class Adapter_notice extends BaseAdapter {

		@Override
		public int getCount() {
			return list_save.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list_save.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int arg0, View arg1, ViewGroup arg2) {

			class ViewHold {
				// ImageView image;
				TextView tv_notice_time, tv_notice_content;

			}
			ViewHold viewHold;
			if (arg1 == null) {
				viewHold = new ViewHold();
				arg1 = inflater.inflate(R.layout.listnotice, null);// inflater
																	// convert
				viewHold.tv_notice_content = (TextView) arg1
						.findViewById(R.id.tv_ma_notice);

				viewHold.tv_notice_time = (TextView) arg1
						.findViewById(R.id.tv_ma_notice_time);
				arg1.setTag(viewHold);

			}
			viewHold = (ViewHold) arg1.getTag();
			viewHold.tv_notice_content
					.setText(list_save.get(arg0).getContent());
			viewHold.tv_notice_time.setText(String.valueOf(list_save.get(arg0)
					.getTime()));

			arg1.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View arg5) {
					// Toast.makeText(ActivitySendNotice.this,
					// String.valueOf(list_save.get(arg0).getId()),
					// Toast.LENGTH_SHORT).show();
					dialog1(list_save.get(arg0).getId());

					return true;

				}

			});
			return arg1;
		}

		public void dialog1(final int id) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					ActivitySendNotice.this); // 先得到构造器
			builder.setTitle("提示"); // 设置标题
			builder.setMessage("是否删除改条纪录?"); // 设置内容
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() { // 设置确定按钮
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss(); // 关闭dialog
							// Toast.makeText(ActivitySendNotice.this,
							// "确认" + which, Toast.LENGTH_SHORT).show();

							new Handler().postDelayed(new Runnable() {

								@Override
								public void run() {
									list_save.clear();
									lv_notice.setRefreshing();
									new AsynDeleteNotice(
											ActivitySendNotice.this)
											.execute(id);

								}
							}, 1500);
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() { // 设置取消按钮
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

						}
					});

			builder.create().show();
		}
	}

	public class AsynDeleteNotice extends AsyncTask<Integer, Void, String> {
		private ProgressDialog dialog;
		private Activity context;

		public AsynDeleteNotice(Context context) {
			this.context = (Activity) context;
			dialog = ProgressDialog.show(context, "正在删除", "正在删除2");

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result.equals(ServletPath.SUCCESS)) {
				Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();

			} else {
				Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();

			}
			dialog.dismiss();
		}

		@Override
		protected String doInBackground(Integer... arg0) {
			String result = ServletPath.FAILURE;
			try {
				result = noticeInterface.deleteNoticeById(arg0[0]);
				Log.v("删除的noticeId是＝", String.valueOf(arg0));
				return ServletPath.SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

	}

	@Override
	public void onBackPressed() {
		// i = 0;
		finish();
		super.onBackPressed();
	}
}
