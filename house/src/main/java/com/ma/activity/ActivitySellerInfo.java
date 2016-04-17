package com.ma.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ma.activity.DialogSellerSalary.Builder;
import com.se.house.ActivitySellerRecord;
import com.se.house.BasicActivity;
import com.se.house.DialogDeleteBuyer;
import com.special.ResideMenuDemo.R;

public class ActivitySellerInfo extends BasicActivity {
	private PopupWindow popupWindow;
	private ListView listView;
	private Button popButton;
	private RelativeLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maactivitysellerinfo);
		init();
	}

	private void init() {
		popButton = (Button) findViewById(R.id.btn_show_buyer_choose_option);
		layout = (RelativeLayout) findViewById(R.id.icon_layout);
		layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(ActivitySellerInfo.this,ActivitySellerRecord.class));
				
			}
		});
		popButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showPopWindow(arg0);

			}
		});
	}

	private void showPopWindow(View view2) {
		View view = LayoutInflater.from(this).inflate(R.layout.popwindowseller,
				null);
		view.findViewById(R.id.modifySalary).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						showModifyDialog();

					}
				});
		view.findViewById(R.id.deletePerson).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						showDeleteDialog();

					}
				});
		popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);
		popupWindow.setOutsideTouchable(false);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 设置背景
		popupWindow.showAsDropDown(view2, 0, 20);// 设置位置
		popupWindow.update();

	}

	private void showModifyDialog() {
		final com.ma.activity.DialogSellerSalary.Builder builder = new DialogSellerSalary.Builder(
				ActivitySellerInfo.this);
		// builder.setTitle("提示");
		// builder.setMessage("是否删除改买家");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(
						ActivitySellerInfo.this,
						Builder.edBasicSalary.getText().toString() + " " + "  "
								+ Builder.edPercent.getText().toString(),
						Toast.LENGTH_SHORT).show();

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

	private void showDeleteDialog() {
		final com.se.house.DialogDeleteBuyer.Builder builder = new DialogDeleteBuyer.Builder(
				ActivitySellerInfo.this);
		builder.setTitle("提示");
		builder.setMessage("是否删除该销售员");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				Toast.makeText(ActivitySellerInfo.this, "确定",
						Toast.LENGTH_SHORT).show();

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

}
