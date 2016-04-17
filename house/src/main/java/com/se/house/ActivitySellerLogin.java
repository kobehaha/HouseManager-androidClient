package com.se.house;

import java.lang.ref.WeakReference;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.exception.Login_exception;
import com.se.inter.use.Inter_seller_login;
import com.se.service.Service_login;
import com.special.ResideMenuDemo.R;
import com.utl.Static_value;
import com.utl.Url;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class ActivitySellerLogin extends Activity {
	private Button btn_login;
	private EditText seller_id, seller_password;
	private ProgressDialog dialog;
	int save_id;

	int result2 = 1;
	private Inter_seller_login login;
	private String mUsername;
	private String mPassword;
	public static final String CONFIG = "login_config";// sharap定义变量

	String url = "http://" + Url.url + "/HouseService/loginServlet";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		setContentView(R.layout.seactivitylogin);
		init();

		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (checkUser()) {
					Seller_account.seller_id = mUsername;
					// mUsername = seller_id.getText().toString();
					//
					// mPassword = seller_password.getText().toString();
					final String url2 = url + "?seller_id=" + mUsername
							+ "&password=" + mPassword;// 定义实际操作的url

					if (dialog == null) {
						dialog = new ProgressDialog(ActivitySellerLogin.this);

					}
					dialog.setTitle("请等待");
					dialog.setMessage("登录中....");
					dialog.setCancelable(false);
					dialog.show();// 获取dialog
					Thread thread = new Thread(new Runnable() {
						public void run() {
							try {
								result2 = login.seller_account(url2, mUsername,
										mPassword);
								hander.sendEmptyMessage(Static_value.FLAG_LOGIN_SUCCESS);

							} catch (Login_exception e) {
								Message message = new Message();
								Bundle bundle = new Bundle();
								bundle.putSerializable("errormes",
										e.getMessage());// 登录密码异常
								message.setData(bundle);
								hander.sendMessage(message);
							} catch (Exception e) {
								Message message = new Message();
								Bundle bundle = new Bundle();
								bundle.putSerializable("errormes",
										Static_value.FLAG_LOGIN_FAIL);// 登录出错
								message.setData(bundle);
								hander.sendMessage(message);

							}

							if (result2 == 1) {
								Intent intent = new Intent();
								Log.v("test kobe", "ok");

								// int id = Integer.parseInt(idString);
								//
								// Bundle bundle = new Bundle();
								// bundle.putInt("save_id", id);// 传递特定的ID
								// intent.putExtras(bundle);

								intent.setClass(ActivitySellerLogin.this,
										MenuActivity.class);
								saveUser(mUsername, mPassword);// 保存数据

								startActivity(intent);

							} else {
								// Toast.makeText(Ac_se_login.this, "密码错误",
								// Toast.LENGTH_SHORT).show();

							}
						}
					});
					thread.start();
				}
			}

		});

	}

	private void init() {
		btn_login = (Button) findViewById(R.id.btn_se_start);
		seller_id = (EditText) findViewById(R.id.ed_se_username);
		seller_password = (EditText) findViewById(R.id.ed_se_userpsd);
		String user[] = getUser(ActivitySellerLogin.this);
		String hasUserName = user[0];
		String hasPassWord = user[1];
		mUsername = hasUserName;

		mPassword = hasPassWord;
		if (mUsername != null) {
			seller_id.setText(hasUserName);
			seller_id.setSelection(seller_id.getText().length());
			seller_password.setText(hasPassWord);
		}

		login = new Service_login();

	}

	public void saveUser(String name, String password) {// 保存seller_id,password
		if (TextUtils.isEmpty(name)) {
			return;
		}
		SharedPreferences sp = ActivitySellerLogin.this.getSharedPreferences(CONFIG,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = sp.edit();
		edit.putString("username", name);
		System.out.println(name);
		if (TextUtils.isEmpty(password)) {
			edit.putString("password", null);

		} else {
			edit.putString("password", password.trim());
		}

		edit.commit();
	}

	private String[] getUser(Context context) {// 从存储的shared中获取出来如果存在
		SharedPreferences sp = context.getSharedPreferences(CONFIG,
				Context.MODE_PRIVATE);
		String name = sp.getString("username", null);
		String password = sp.getString("password", null);
		String[] user = new String[2];
		user[0] = name;
		user[1] = password;

		return user;
	}

	private boolean checkUser() {// 判断是否取出数据
		mUsername = seller_id.getText().toString();
		mPassword = seller_password.getText().toString();
		boolean isValid = true;
		if (mUsername == null || mUsername.length() == 0) {
			Toast.makeText(ActivitySellerLogin.this, "请输入用户名", Toast.LENGTH_SHORT)
					.show();
			isValid = false;
		}
		return isValid;
	}

	private void show(String error) {
		Toast.makeText(ActivitySellerLogin.this, error, Toast.LENGTH_SHORT).show();

	}

	private static class Ihandle extends Handler {
		private final WeakReference<ActivitySellerLogin> mActivity;

		public Ihandle(ActivitySellerLogin activity) {
			this.mActivity = new WeakReference<ActivitySellerLogin>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			if (mActivity.get().dialog != null) {
				mActivity.get().dialog.dismiss();

			}
			int flag = msg.what;// 获取flag
			switch (flag) {
			case 0:
				String error = (String) msg.getData().getSerializable(
						"errormes");
				mActivity.get().show(error);

				break;
			case Static_value.FLAG_LOGIN_SUCCESS:

				mActivity.get().show(Static_value.FlAG_LOGIN_SUCESS_OK);
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	}

	private Ihandle hander = new Ihandle(ActivitySellerLogin.this);

}
