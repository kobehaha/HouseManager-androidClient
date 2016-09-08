package com.ma.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.beans.Manager;
import com.se.house.BasicActivity;
import com.se.house.MenuActivity;
import com.se.house.Seller_account;
import com.special.ResideMenuDemo.R;

@SuppressLint("InlinedApi")
public class login extends BasicActivity {
	private EditText ed_account, ed_password;
	private Button btn_login;

	@TargetApi(19)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maactivitylogin);
		init();

		// 设置点击事件
		btn_login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					int id = Integer.valueOf(ed_account.getText().toString());
					int password = Integer.valueOf(ed_password.getText()
							.toString());
					if (id == 1 && password == 1) {
						Manager.manager_id = "1";
						Toast.makeText(login.this, "登录成功", Toast.LENGTH_SHORT)
								.show();
						startActivity(new Intent(login.this, ActivityMain.class));
					} else if (id == 2 && password == 2) {
						Seller_account.seller_id = "2";
						Intent intent = new Intent();
						Log.v("test kobe", "ok");

						intent.setClass(login.this, MenuActivity.class);

						startActivity(intent);
						Toast.makeText(login.this, "登录成功", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(login.this, "密码错误", Toast.LENGTH_SHORT)
								.show();
					}

				} catch (NumberFormatException e) {
					Toast.makeText(login.this, "请输入符合格式的帐号", Toast.LENGTH_SHORT)
							.show();
				}
			}

		});

	}

	private void init() {
		ed_account = (EditText) findViewById(R.id.ed_account);
		ed_password = (EditText) findViewById(R.id.ed_password);
		btn_login = (Button) findViewById(R.id.btn_login);

	}

}
