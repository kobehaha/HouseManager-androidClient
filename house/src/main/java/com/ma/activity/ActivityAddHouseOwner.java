package com.ma.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beans.Owner2;
import com.se.house.BasicActivity;
import com.special.ResideMenuDemo.R;

@SuppressLint("InlinedApi")
public class ActivityAddHouseOwner extends BasicActivity {
	// 增加房源和拥有者的信息
	private ArrayAdapter<CharSequence> adapter;
	private Button btn_owner_add;
	private EditText ed_name, ed_phone, ed_weichart;
	private Spinner spinner;
	private RadioGroup radioGroup;
	private Owner2 owner;
	private String sex;
	private int age;
	private TextView tv_age;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.maactivityaddhouseowner);
		init();
		btn_owner_add.setOnClickListener(new OnClickListener() {// 设置监听器

					@Override
					public void onClick(View arg0) {// 获取输入的变量
						String name = ed_name.getText().toString();

						// String age = ed_age.getText().toString();

						// String sex = ed_sex.getText().toString();
						String weichart = ed_weichart.getText().toString();
						String phone = ed_phone.getText().toString();
						if (name.equals("") && sex.equals("")// 设置判断条件
								&& weichart.equals("") && phone.equals("")) {
							Toast.makeText(ActivityAddHouseOwner.this,
									"请完整输入条件", Toast.LENGTH_SHORT).show();

						} else {
							owner = new Owner2(name, age, sex, Integer
									.parseInt(phone), weichart);// 实例化变量
							Intent intent = new Intent(
									ActivityAddHouseOwner.this,
									ActivityAddHouseInfo.class);// 设置intent传递对象
							Bundle bundle = new Bundle();
							bundle.putInt("sure", 2);
							bundle.putSerializable("owner", owner);
							intent.putExtras(bundle);
							startActivity(intent);

						}

					}
				});

	}

	private void init() {
		tv_age = (TextView) findViewById(R.id.tv_owner_info_now_age);
		radioGroup = (RadioGroup) findViewById(R.id.ra_owner_info_sex);
		spinner = (Spinner) findViewById(R.id.spinner_owner_info__age);
		btn_owner_add = (Button) findViewById(R.id.btn_owner_add);
		// ed_age = (EditText) findViewById(R.id.ed_owner_age);
		ed_name = (EditText) findViewById(R.id.ed_owner_name);
		ed_phone = (EditText) findViewById(R.id.ed_owner_phone);
		// ed_sex = (EditText) findViewById(R.id.ed_owner_sex);
		ed_weichart = (EditText) findViewById(R.id.ed_owner_weichart);
		adapter = ArrayAdapter.createFromResource(this, R.array.age,
				android.R.layout.simple_dropdown_item_1line);
		spinner.setAdapter(adapter);
		
		
		//设置单选按钮监听器
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				RadioButton r = (RadioButton) findViewById(arg1);
				sex = r.getText().toString();
			}
		});
		
		//springer监听器
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				age = Integer.valueOf(arg0.getItemAtPosition(arg2).toString());// 获取年龄
				tv_age.setText("当前选择" + String.valueOf(age) + "岁");

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				Toast.makeText(ActivityAddHouseOwner.this, "对不起，您什么都没有选中",
						Toast.LENGTH_SHORT).show();

			}
		});

	}

}
