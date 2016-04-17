package com.ma.activity;

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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fragment.FragmentManagerHouse;
import com.fragment.FragmentManagerSeller;
import com.fragment.FragmentSellerSort;
import com.se.house.ActivitySellerRecord;
import com.se.house.ActivitySellerSetting;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.special.ResideMenuDemo.R;
import com.utl.Common_util;

@SuppressLint({ "NewApi", "Recycle" })
public class ActivityMain extends FragmentActivity implements
		View.OnClickListener, android.widget.RadioGroup.OnCheckedChangeListener {
	private RadioGroup group;
	private RadioButton main_home;
	// private View parentView;
	private LinearLayout error;
	private Boolean net;

	private ResideMenu resideMenu;// 创建菜单对象
	// private Ac_ma_main mContext;
	AlertDialog.Builder builder;
	private ResideMenuItem itemHome;
	private ResideMenuItem itemInfo;
	private ResideMenuItem itemCalendar;
	private ResideMenuItem itemSettings;
	private FragmentManagerHouse fragment1;
	private FragmentManagerSeller fragment2;
	private FragmentSellerSort fragment3;
	private android.support.v4.app.FragmentManager mfragmentManager;
	private PopupWindow tools;// 初始化popupwindow

	@Override
	public void onCreate(Bundle savedInstanceState) {
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
		setContentView(R.layout.maactivitymain);
		// mContext = this;

		init();
		show_error();
		setUpMenu();
		mfragmentManager = getSupportFragmentManager();
		main_home.setChecked(true);// 设置最初的fragment
		showFragment(1);
		// changeFragment(new Frag_ma_house(), false);
		group.setOnCheckedChangeListener((android.widget.RadioGroup.OnCheckedChangeListener) this);
	}

	private void init() {
		builder = new AlertDialog.Builder(ActivityMain.this);
		group = (RadioGroup) findViewById(R.id.main_tabs_button);
		main_home = (RadioButton) findViewById(R.id.radio_ma_left);
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

		// You can disable a direction by setting ->
		resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

		findViewById(R.id.title_bar_left_menu_ma).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
					}
				});
		findViewById(R.id.title_bar_add_ma).setOnClickListener(
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
			startActivity(new Intent(ActivityMain.this,
					ActivityManagerInfo.class));
		} else if (view == itemCalendar) {
			startActivity(new Intent(ActivityMain.this,
					ActivitySellerRecord.class));

		} else if (view == itemSettings) {
			// changeFragment(new Sort_fragment());

			// } else if (view.getId() == R.id.add) {
			// showTools(view);// 现实popwindow
			startActivity(new Intent(ActivityMain.this,
					ActivitySellerSetting.class));

		} else if (view.getId() == R.id.tools_search_ma) {
			startActivity(new Intent(ActivityMain.this, ActivitySearch.class));

		} else if (view.getId() == R.id.tools_send_notice_ma) {
			startActivity(new Intent(ActivityMain.this,
					ActivitySendNotice.class));

		} else if (view.getId() == R.id.tools_add_seller_ma) {

			startActivity(new Intent(ActivityMain.this, ActivityAddSeller.class));

		} else if (view.getId() == R.id.tools_add_house_ma) {
			startActivity(new Intent(ActivityMain.this,
					ActivityAddHouseOwner.class));

		}

		resideMenu.closeMenu();
	}

	private void showTools(View view) {// 现实pop
		View toolsLayout = LayoutInflater.from(ActivityMain.this).inflate(
				R.layout.mapopadd, null);// 填充对象
		toolsLayout.findViewById(R.id.tools_add_house_ma).setOnClickListener(
				this);
		toolsLayout.findViewById(R.id.tools_add_seller_ma).setOnClickListener(
				this);
		toolsLayout.findViewById(R.id.tools_search_ma).setOnClickListener(this);
		toolsLayout.findViewById(R.id.tools_send_notice_ma).setOnClickListener(
				this);
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

	//
	// @SuppressLint({ "NewApi", "CommitTransaction" })
	// private void changeFragment(Fragment targetFragment, Boolean is_init) {//
	// 转换frgament
	// resideMenu.clearIgnoredViewList();
	// FragmentTransaction transaction = mfragmentManager.beginTransaction();
	// // 开始事务，填充
	// // 设置动画
	// transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
	// transaction.replace(R.id.main_fragment, targetFragment);
	// // transaction.show(targetFragment);
	// // if (is_init) {
	// // transaction.addToBackStack(null);
	// //
	// // }
	// transaction.commit();
	//
	// }

	// What good method is to access resideMenu？
	public ResideMenu getResideMenu() {
		return resideMenu;
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {// 切换id
		// TODO Auto-generated method stub
		// switch (arg1) {
		// case R.id.radio_ma_middle:// 客户
		// // changeFragment(new Fra_ma_seller(), true);
		// showFragment(2);
		//
		// break;
		// case R.id.radio_ma_left:// 房源
		//
		// // changeFragment(new Frag_ma_house(), true);／／
		// showFragment(1);
		// break;
		// case R.id.radio_ma_right:// 排名
		// // changeFragment(new Fra_se_sort(), true);
		// showFragment(3);
		//
		// break;
		//
		// default:
		// break;
		// }
		if (arg1 == R.id.radio_ma_middle) {
			showFragment(2);
		} else if (arg1 == R.id.radio_ma_left) {
			showFragment(1);
		} else {
			showFragment(3);
		}

	}

	public void showFragment(int index) {
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
				fragment2 = new FragmentManagerSeller();
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

	// 当fragment已被实例化，就隐藏起来
	public void hideFragments(FragmentTransaction ft) {
		if (fragment1 != null)
			ft.hide(fragment1);
		if (fragment2 != null)
			ft.hide(fragment2);
		if (fragment3 != null)
			ft.hide(fragment3);

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

	private void show_error() {
		net = Common_util.is_net_work_connected(ActivityMain.this);
		if (net) {
			error.setVisibility(8);

		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.v("code", String.valueOf(resultCode));
		if (requestCode == 0) {
			show_error();
			if (net) {
				error.setVisibility(View.INVISIBLE);

			}

		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		show_error();
		super.onPause();
	}

}
