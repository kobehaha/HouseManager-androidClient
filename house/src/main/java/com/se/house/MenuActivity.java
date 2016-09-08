package com.se.house;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fragment.FragmentManagerHouse;
import com.fragment.FragmentSellerClient;
import com.fragment.FragmentSellerSort;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.special.ResideMenuDemo.R;
import com.utl.Common_util;

@SuppressLint({ "NewApi", "Recycle" })
public class MenuActivity extends FragmentActivity implements
		View.OnClickListener, android.widget.RadioGroup.OnCheckedChangeListener {
	private RadioGroup group;
	private LinearLayout error;
	private Boolean net;

	AlertDialog.Builder builder;
	private RadioButton main_home;
	private Fragment fragment1;
	private Fragment fragment2;
	private Fragment fragment3;

	private ResideMenu resideMenu;// 创建菜单对象
	private MenuActivity mContext;
	private ResideMenuItem itemHome;
	private ResideMenuItem itemInfo;
	private ResideMenuItem itemCalendar;
	private ResideMenuItem itemSettings;
	private android.support.v4.app.FragmentManager mfragmentManager;
	private PopupWindow tools;// 初始化popupwindow

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		setContentView(R.layout.seactivitymain);
		mContext = this;
		init();
		show_error();
		setUpMenu();
		mfragmentManager = getSupportFragmentManager();
		main_home.setChecked(true);// 设置最初的fragment
		// changeFragment(new Fra_se_house(), false);
		showFragment(1);
		group.setOnCheckedChangeListener((android.widget.RadioGroup.OnCheckedChangeListener) this);
	}

	private void show_error() {
		net = Common_util.is_net_work_connected(MenuActivity.this);
		if (net) {
			error.setVisibility(8);

		}

	}

	private void showFragment(int index) {
		FragmentTransaction ft = mfragmentManager.beginTransaction();

		// 想要显示一个fragment,先隐藏所有fragment，防止重叠
		hideFragments(ft);

		switch (index) {
		case 1:
			// 如果fragment1已经存在则将其显示出来
			if (fragment1 != null)
				ft.show(fragment1);
			// 否则是第一次切换则添加fragment1，注意添加后是会显示出来的，replace方法也是先remove后add
			else {
				fragment1 = new FragmentManagerHouse();
				ft.add(R.id.main_fragment, fragment1);
			}
			break;
		case 2:
			if (fragment2 != null)
				ft.show(fragment2);
			else {
				fragment2 = new FragmentSellerClient();
				ft.add(R.id.main_fragment, fragment2);
			}
			break;
		case 3:
			if (fragment3 != null)
				ft.show(fragment3);
			else {
				fragment3 = new FragmentSellerSort();
				ft.add(R.id.main_fragment, fragment3);
			}
			break;

		}
		ft.commit();

	}

	private void hideFragments(FragmentTransaction ft) {
		if (fragment1 != null)
			ft.hide(fragment1);
		if (fragment2 != null)
			ft.hide(fragment2);
		if (fragment3 != null)
			ft.hide(fragment3);
	}

	private void init() {
		builder = new AlertDialog.Builder(MenuActivity.this);
		group = (RadioGroup) findViewById(R.id.main_tabs_button);
		main_home = (RadioButton) findViewById(R.id.radio_house);
		error = (LinearLayout) findViewById(R.id.layout_network);// 错误信息
		error.setOnClickListener(new OnClickListener() {// 设置打开系统界面

			@Override
			public void onClick(View arg0) {

				System.out.println("点击成功");

				Intent intent = new Intent(Settings.ACTION_SETTINGS);
				startActivityForResult(intent, 0);

			}
		});

	}

	private void setUpMenu() {

		resideMenu = new ResideMenu(this);
		resideMenu.setBackground(R.drawable.mainback);
		resideMenu.attachToActivity(this);
		resideMenu.setMenuListener(menuListener);
		// valid scale factor is between 0.0f and 1.0f. leftmenu'width is
		// 150dip.
		resideMenu.setScaleValue(0.6f);

		// create menu items;
		itemHome = new ResideMenuItem(this, R.drawable.icon_home, "主界面");
		itemInfo = new ResideMenuItem(this, R.drawable.icon_profile, "个人信息");
		itemCalendar = new ResideMenuItem(this, R.drawable.icon_calendar,
				"销售纪录");
		itemSettings = new ResideMenuItem(this, R.drawable.icon_settings, "设置");

		itemHome.setOnClickListener(this);
		itemInfo.setOnClickListener(this);
		itemCalendar.setOnClickListener(this);
		itemSettings.setOnClickListener(this);

		resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemInfo, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemCalendar, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_LEFT);
		resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

		// You can disable a direction by setting ->
		// resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

		findViewById(R.id.title_bar_left_menu).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
					}
				});
		findViewById(R.id.title_bar_add).setOnClickListener(
				new View.OnClickListener() {
					// 现实popwind
					@Override
					public void onClick(View view) {
						showTools(view);
					}
				});

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return resideMenu.dispatchTouchEvent(ev);
	}

	@Override
	public void onClick(View view) {

		if (view == itemHome) {
			// changeFragment(new House_fragment());
		} else if (view == itemInfo) {
			startActivity(new Intent(MenuActivity.this,
					ActivitySellerInfo.class));
		} else if (view == itemCalendar) {
			startActivity(new Intent(MenuActivity.this,
					ActivitySellerRecord.class));
		} else if (view == itemSettings) {
			// changeFragment(new Sort_fragment());

			// } else if (view.getId() == R.id.add) {
			// showTools(view);// 现实popwindow
			startActivity(new Intent(MenuActivity.this,
					ActivitySellerSetting.class));

		} else if (view.getId() == R.id.tools_search) {
			startActivity(new Intent(MenuActivity.this,
					ActivitySellerSearch.class));

		} else if (view.getId() == R.id.tools_notice) {
			startActivity(new Intent(MenuActivity.this,
					ActivitySellerNotice.class));

		} else if (view.getId() == R.id.tools_add_single) {

			startActivity(new Intent(MenuActivity.this,
					ActivitySellerInsertClientList.class));

		}

		resideMenu.closeMenu();
	}

	private void showTools(View view) {// 现实pop
		View toolsLayout = LayoutInflater.from(MenuActivity.this).inflate(
				R.layout.popitemwindowsse, null);// 填充对象
		toolsLayout.findViewById(R.id.tools_add_single)
				.setOnClickListener(this);
		toolsLayout.findViewById(R.id.tools_notice).setOnClickListener(this);
		toolsLayout.findViewById(R.id.tools_search).setOnClickListener(this);
		tools = new PopupWindow(toolsLayout, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);
		tools.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		tools.setOutsideTouchable(false);
		tools.showAsDropDown(view, 0, 20);
		tools.update();

	}

	private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {// 设置菜单监听器
		@Override
		public void openMenu() {
			// Toast.makeText(mContext, "菜单打开!", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void closeMenu() {
			// Toast.makeText(mContext, "菜单关闭", Toast.LENGTH_SHORT).show();
		}
	};

	// @SuppressLint({ "NewApi", "CommitTransaction" })
	// private void changeFragment(Fragment targetFragment, Boolean is_init) {//
	// 转换frgament
	// resideMenu.clearIgnoredViewList();
	// FragmentTransaction transaction = mfragmentManager.beginTransaction();
	// // 开始事务，填充
	// // 设置动画
	// transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
	// transaction.replace(R.id.main_fragment, targetFragment);
	// if (is_init) {
	// transaction.addToBackStack(null);
	//
	// }
	// transaction.commit();
	//
	// }

	// What good method is to access resideMenu？

	public ResideMenu getResideMenu() {
		return resideMenu;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			builder.setTitle("你确定要离开吗？");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// 这里添加点击确定后的逻辑
							finish();
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// 这里添加点击确定后的逻辑

						}
					});
			builder.create().show();

		}

		return false;

	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {// 切换id
		// TODO Auto-generated method stub
		// switch (arg1) {
		// case R.id.radio_buyer:// 客户
		// // changeFragment(new Fra_ma_seller(), true);
		// showFragment(2);
		//
		// break;
		// case R.id.radio_house:// 房源
		//
		// // changeFragment(new Frag_ma_house(), true);／／
		// showFragment(1);
		// break;
		// case R.id.radio_sort:// 排名
		// // changeFragment(new Fra_se_sort(), true);
		// showFragment(3);
		//
		// break;
		//
		// default:
		// break;
		// }
		if (arg1 == R.id.radio_buyer) {
			showFragment(2);
		} else if (arg1 == R.id.radio_house) {
			showFragment(1);

		} else {
			showFragment(3);

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.v("code", String.valueOf(resultCode));
		show_error();
		if (requestCode == 0) {
			if (net) {
				error.setVisibility(View.INVISIBLE);

			}

		}

	}

}
