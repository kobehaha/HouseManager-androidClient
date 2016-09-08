package com.se.house;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ma.activity.ActivityShowPicture;
import com.ma.photo.activity.ActivityAlbumAddHouse2;
import com.ma.photo.utl.Bimp2;
import com.ma.photo.utl.FileUtils;
import com.ma.photo.utl.Image_item;
import com.ma.service.FormInfoService;
import com.special.ResideMenuDemo.R;
import com.utl.FormFile;
import com.utl.ServletPath;

public class ActivityModifySellerInfo extends Activity {

	private static final int TAKE_PICTURE = 0x000001;
	private SharedPreferences sharedPreferences;
	private ArrayAdapter<CharSequence> adapter_age, adapter_degree;
	private RadioGroup radioGroup;
	private Spinner spinner_age, spinner_degree;
	private TextView tv_time, tv_age, tv_degree;
	private EditText ed_name, ed_phone, ed_weichart;
	private ImageView img_ico, img_choose;
	private String str_name, str_sex = "男", str_time, str_degree = "小学",
			str_weichart;
	private int phone, age = 16;
	private Button btn_modify, btn_get_time;
	private int year, month, day;
	private PopupWindow popupWindow;// 弹出式菜单
	private View parentview;
	private String mpath = null;// formdata
	private Map<String, String> map = new HashMap<String, String>();
	private Bitmap bitmap;
	@SuppressLint("InlinedApi")
	@TargetApi(19)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		parentview = getLayoutInflater().inflate(
				R.layout.seactivitysellerinfomodify, null);
		setContentView(R.layout.seactivitysellerinfomodify);

		init();
		get_manager_info();
	}

	private void get_manager_info() {
		// 从存储的shared中获取出来如果存在
		SharedPreferences sp = getSharedPreferences(ActivitySellerInfo.config,
				Context.MODE_PRIVATE);
		// mpath = sp.getString("path",
		// String.valueOf(R.drawable.head_icon_user));
		str_name = sp.getString("name", "未添加资料");
		str_time = sp.getString("time", "未添加资料");
		str_weichart = sp.getString("weichart", "未添加资料");
		str_degree = sp.getString("degree", "未添加资料");

		age = sp.getInt("age", 0);
		phone = sp.getInt("phone", 000);
		str_sex = sp.getString("sex", "未添加资料");
		tv_age.setText("年龄  " + String.valueOf(age) + "岁");
		tv_time.setText(str_time);
		ed_name.setText(str_name);
		ed_weichart.setText(str_weichart);
		ed_phone.setText(String.valueOf(phone));

	}

	@SuppressWarnings("deprecation")
	private void init() {
		tv_degree = (TextView) findViewById(R.id.tv_se_info_now_degree);
		spinner_degree = (Spinner) findViewById(R.id.spinner_se_info_modify_degree);
		ed_weichart = (EditText) findViewById(R.id.ed_se_info_modify_weichart);
		tv_time = (TextView) findViewById(R.id.tv_se_info_now_time);
		tv_age = (TextView) findViewById(R.id.tv_se_info_now_age);
		spinner_age = (Spinner) findViewById(R.id.spinner_se_info_modify_age);
		ed_name = (EditText) findViewById(R.id.ed_se_info_modify_name);
		ed_phone = (EditText) findViewById(R.id.ed_se_info_modify_phone);
		radioGroup = (RadioGroup) findViewById(R.id.ra_se_info_sex);
		btn_get_time = (Button) findViewById(R.id.btn_se_info_modify_time);
		img_choose = (ImageView) findViewById(R.id.img_se_modify_icon);
		img_ico = (ImageView) findViewById(R.id.img_se_info_inco_show);
		btn_modify = (Button) findViewById(R.id.btn_se_info_modify_sure);
		adapter_age = ArrayAdapter.createFromResource(this, R.array.age,
				android.R.layout.simple_dropdown_item_1line);
		adapter_degree = ArrayAdapter.createFromResource(this, R.array.degree,
				android.R.layout.simple_dropdown_item_1line);
		spinner_degree.setAdapter(adapter_degree);
		spinner_age.setAdapter(adapter_age);
		popupWindow = new PopupWindow(ActivityModifySellerInfo.this);
		View view = getLayoutInflater().inflate(
				R.layout.popupitemwindowsaddimge, null);// 获取填充视图
		popupWindow.setWidth(android.app.ActionBar.LayoutParams.MATCH_PARENT);
		popupWindow.setHeight(android.app.ActionBar.LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		btn_modify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				str_name = ed_name.getText().toString();
				str_weichart = ed_weichart.getText().toString();
				str_time = tv_time.getText().toString();
				try {
					phone = Integer.parseInt(ed_phone.getText().toString());
					if (str_name.equals("") || str_time.equals("")
							|| str_weichart.equals("")) {
						Toast.makeText(ActivityModifySellerInfo.this,
								"请输入完整信息", Toast.LENGTH_SHORT).show();

					} else {
						Log.v("销售人员上传的数据资料",
								"姓名" + str_name + "  电话"
										+ String.valueOf(phone) + " 性别是"
										+ "  年龄是" + String.valueOf(age)
										+ str_sex + "  工作时间是" + str_time
										+ "微信是" + str_weichart);
						map.put("account", Seller_account.seller_id);
						map.put("name", str_name);
						map.put("weichart", "weichart");
						map.put("time", str_time);
						map.put("phone", String.valueOf(phone));
						map.put("degree", str_degree);
						map.put("age", String.valueOf(age));
						map.put("sex", str_sex);
						if (Bimp2.mArrayList.size() == 0) {
							Toast.makeText(ActivityModifySellerInfo.this,
									"您还没有加入图片呢", Toast.LENGTH_SHORT).show();

						} else {
							AsynModifySellerInfo modify = new AsynModifySellerInfo(
									ActivityModifySellerInfo.this);
							Toast.makeText(ActivityModifySellerInfo.this,
									"开始上传", Toast.LENGTH_SHORT).show();
							modify.execute();

						}

					}

				} catch (NumberFormatException e) {
					Toast.makeText(ActivityModifySellerInfo.this,
							"数据未完整,或电话输入输入存在问题，请从新输入", Toast.LENGTH_SHORT)
							.show();

				}

			}
		});

		img_ico.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				if (mpath == null) {
					Toast.makeText(ActivityModifySellerInfo.this, "您还没有添加图片",
							Toast.LENGTH_SHORT).show();
				} else {
					bundle.putString("picture", mpath);
					intent.putExtras(bundle);
					intent.setClass(ActivityModifySellerInfo.this,
							ActivityShowPicture.class);
					startActivity(intent);

				}
			}
		});

		img_choose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popupWindow.showAtLocation(parentview, Gravity.BOTTOM, 0, 0);

			}
		});
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();// 拍照获取图片
				popupWindow.dismiss();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {// 从相册中获取图片
			public void onClick(View v) {
				Intent intent = new Intent(ActivityModifySellerInfo.this,
						ActivityAlbumAddHouse2.class);
				startActivity(intent);// 从文件中获取图片
				finish();
				popupWindow.dismiss();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				RadioButton r = (RadioButton) findViewById(arg1);
				str_sex = r.getText().toString();
			}
		});
		spinner_age.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				age = Integer.valueOf(arg0.getItemAtPosition(arg2).toString());// 获取年龄
				tv_age.setText("选择" + String.valueOf(age) + "岁");

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				Toast.makeText(ActivityModifySellerInfo.this, "对不起，您什么都没有选中",
						Toast.LENGTH_SHORT).show();

			}
		});
		spinner_degree.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				str_degree = arg0.getItemAtPosition(arg2).toString();// 获取年龄
				tv_degree.setText("学历  " + str_degree);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				Toast.makeText(ActivityModifySellerInfo.this, "对不起，您什么都没有选中",
						Toast.LENGTH_SHORT).show();

			}
		});
		btn_get_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
				Date mydate = new Date(); // 获取当前日期Date对象
				mycalendar.setTime(mydate);// //为Calendar对象设置时间为当前日期

				year = mycalendar.get(Calendar.YEAR); // 获取Calendar对象中的年
				month = mycalendar.get(Calendar.MONTH);// 获取Calendar对象中的月
				day = mycalendar.get(Calendar.DAY_OF_MONTH);// 获取这个月的第几天}
				/**
				 * 构造函数原型： public DatePickerDialog (Context context,
				 * DatePickerDialog.OnDateSetListener callBack, int year, int
				 * monthOfYear, int dayOfMonth) content组件运行Activity，
				 * DatePickerDialog.OnDateSetListener：选择日期事件
				 * year：当前组件上显示的年，monthOfYear：当前组件上显示的月，dayOfMonth：当前组件上显示的第几天
				 * 
				 */
				// 创建DatePickerDialog对象
				DatePickerDialog dpd = new DatePickerDialog(
						ActivityModifySellerInfo.this, Datelistener, year,
						month, day);
				dpd.show();// 显示DatePickerDialog组件
			}
		});
	}

	protected void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);

	}

	private DatePickerDialog.OnDateSetListener Datelistener = new DatePickerDialog.OnDateSetListener() {
		/**
		 * params：view：该事件关联的组件 params：myyear：当前选择的年 params：monthOfYear：当前选择的月
		 * params：dayOfMonth：当前选择的日
		 */
		@Override
		public void onDateSet(DatePicker view, int myyear, int monthOfYear,
				int dayOfMonth) {

			// 修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
			year = myyear;
			month = monthOfYear;
			day = dayOfMonth;
			// 更新日期
			updateDate();

		}

		// 当DatePickerDialog关闭时，更新日期显示
		private void updateDate() {
			// 在TextView上显示日期
			tv_time.setText("" + year + "-" + (month + 1) + "-" + day);
		}
	};

	private class AsynModifySellerInfo extends
			AsyncTask<Image_item, Integer, String> {

		private ProgressDialog pdialog;
		private Activity activity;
		private ArrayList<Image_item> mArrayList = Bimp2.mArrayList;

		public AsynModifySellerInfo(Context context) {
			activity = (Activity) context;
			pdialog = ProgressDialog.show(activity, "正在加载", "正在上传");

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			pdialog.dismiss();
			if (FormInfoService.SUCCESS.equalsIgnoreCase((String) result)) {
				Toast.makeText(activity, "上传成功", Toast.LENGTH_SHORT).show();
				sharedPreferences = getSharedPreferences(
						ActivitySellerInfo.config, MODE_PRIVATE);
				Editor editor = sharedPreferences.edit();

				editor.putString("sex", str_sex);
				editor.putString("time", str_time);
				editor.putString("name", str_name);
				editor.putInt("age", age);
				editor.putInt("phone", phone);
				editor.putString("weichart", str_weichart);
				editor.putString("degree", str_degree);
				editor.commit();

			} else {

				Toast.makeText(activity, "上传失败,请检查你的网络", Toast.LENGTH_SHORT)
						.show();

			}
			super.onPostExecute(result);
		}

		@Override
		protected String doInBackground(Image_item... arg0) {
			File file;
			FormFile[] fiFormFiles = new FormFile[1];

			file = new File(mArrayList.get(0).getImge_path());
			FormFile formfile = new FormFile(file.getName(), file, "image",
					"application/octet-stream");
			fiFormFiles[0] = formfile;
			Log.v("房源上传file图片个数", String.valueOf(fiFormFiles.length));
			return FormInfoService.post(ServletPath.SELLERMODIFYINFO, map,
					fiFormFiles);

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			pdialog.setProgress(values[0]);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp2.mArrayList.size() < 1 && resultCode == RESULT_OK) {
				String fileName = String.valueOf(System.currentTimeMillis());
				Uri uri = data.getData();
				if (uri != null) {
					bitmap = BitmapFactory.decodeFile(uri.getPath());
					System.out.println("uri不为空" + uri);
					FileUtils.saveBitmap(bitmap, fileName);
					String filePath = FileUtils.SDPATH + fileName + ".JPEG";
					Image_item takePhoto = new Image_item();
					takePhoto.setImge_path(filePath);
					takePhoto.setBitmap(bitmap);
					Bimp2.mArrayList.add(takePhoto);
					img_ico.setImageBitmap(bitmap);

				}

				if (bitmap == null) {
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						bitmap = (Bitmap) bundle.get("data");
						System.out.println("data不为空" + bundle.toString());
						FileUtils.saveBitmap(bitmap, fileName);
						String filePath = FileUtils.SDPATH + fileName + ".JPEG";
						Image_item takePhoto = new Image_item();
						takePhoto.setImge_path(filePath);
						takePhoto.setBitmap(bitmap);
						Bimp2.mArrayList.add(takePhoto);
						img_ico.setImageBitmap(bitmap);

					}
				}
			}
			break;
		}

	}
}
