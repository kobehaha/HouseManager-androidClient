package com.ma.activity;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ma.interfac.SellerInterface;
import com.ma.service.ServiceSeller;
import com.se.house.BasicActivity;
import com.special.ResideMenuDemo.R;
import com.utl.ServletPath;

@TargetApi(19)
public class ActivityAddSeller extends BasicActivity {
	private SellerInterface sellerInterface;// 通过借口获取服务
	ImageView img_back;
	EditText ed_password, ed_password_sure, ed_account;
	Button btn_add;
	public String str_account, str_password, str_password_sure;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.maactivityandseller);
		init();// 初始化
		btn_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				str_account = ed_account.getText().toString();
				str_password = ed_password.getText().toString();
				str_password_sure = ed_password_sure.getText().toString();
				Log.v("管理员增加销售", "密码是" + str_password + "确认密码是"
						+ str_password_sure);
				if (str_password_sure.equals(str_password)) {
					new AsynAddSeller().execute();

				} else {
					Toast.makeText(ActivityAddSeller.this, "对不起，您两次输入的密码不一致",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		img_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});

	}

	private void init() {
		img_back = (ImageView) findViewById(R.id.back3);
		ed_account = (EditText) findViewById(R.id.ed_seller_account);
		ed_password = (EditText) findViewById(R.id.ed_seller_password);
		ed_password_sure = (EditText) findViewById(R.id.ed_seller_sure_password);
		btn_add = (Button) findViewById(R.id.btn_ma_seller_add_sure);
		sellerInterface = new ServiceSeller();

	}

	// 添加销售
	public class AsynAddSeller extends AsyncTask<Void, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			if (ServletPath.SUCCESS.equalsIgnoreCase((String) result)) {
				Toast.makeText(ActivityAddSeller.this, "上传成功",
						Toast.LENGTH_SHORT).show();

			} else {

				Toast.makeText(ActivityAddSeller.this, "上传失败,请检查你的网络",
						Toast.LENGTH_SHORT).show();

			}
			super.onPostExecute(result);

		}

		@Override
		protected String doInBackground(Void... arg0) {// 后台执行
			String str_sure = "0";
			try {
				str_sure = sellerInterface
						.add_seller(str_account, str_password);
			} catch (ClientProtocolException e) {

				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return str_sure;

		}
	}

}
