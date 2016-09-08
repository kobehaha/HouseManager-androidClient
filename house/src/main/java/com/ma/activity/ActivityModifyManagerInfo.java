package com.ma.activity;

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
import android.view.ViewGroup.LayoutParams;
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

import com.beans.Manager;
import com.ma.photo.activity.ActivityAlbumAddHouse2;
import com.ma.photo.utl.Bimp2;
import com.ma.photo.utl.FileUtils;
import com.ma.photo.utl.Image_item;
import com.ma.service.FormInfoService;
import com.special.ResideMenuDemo.R;
import com.utl.FormFile;
import com.utl.ServletPath;

@SuppressLint("WorldReadableFiles")
public class ActivityModifyManagerInfo extends Activity {
	private RadioGroup radioGroup;
	private ArrayAdapter<CharSequence> adapter;
	private Spinner spinner;
	private ImageView imgIco, imgBack, imgShow;
	private Button btnOk, btnGetTime;
	private EditText edName, edPhone;
	private String name, sex, time;
	private int age;
	private int year = 2013, month = 5, day = 14, phone;
	private PopupWindow pop = null;
	private TextView tvAge, tvTime;
	private View parentView;
	private Map<String, String> map;
	private Bitmap bitmap;
	private static final int TAKE_PICTURE = 0x000001;

	@TargetApi(19)
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
		parentView = getLayoutInflater().inflate(
				R.layout.maactivityaddhouseinfo, null);

		setContentView(R.layout.maactivityinfomodify);

		init();
		get_ma_info(ActivityModifyManagerInfo.this);
	}

	@SuppressWarnings("deprecation")
	private void init() {
		map = new HashMap<String, String>();
		btnGetTime = (Button) findViewById(R.id.btn_ma_info_modify_time);
		radioGroup = (RadioGroup) findViewById(R.id.radio_ma_info_modify_sex);
		spinner = (Spinner) findViewById(R.id.spinner_ma_info_modify_age);
		tvAge = (TextView) findViewById(R.id.tv_ma_info_now_age);
		tvTime = (TextView) findViewById(R.id.tv_ma_info_now_time);
		edName = (EditText) findViewById(R.id.ed_ma_info_modify_name);
		edPhone = (EditText) findViewById(R.id.ed_ma_info_modify_phone);
		imgBack = (ImageView) findViewById(R.id.img_ma_info_modify_back3);
		imgIco = (ImageView) findViewById(R.id.img_ma_modify_icon);
		imgShow = (ImageView) findViewById(R.id.img_ma_info_inco_show);
		btnOk = (Button) findViewById(R.id.btn_ma_info_modify_sure);
		adapter = ArrayAdapter.createFromResource(this, R.array.age,
				android.R.layout.simple_dropdown_item_1line);
		spinner.setAdapter(adapter);
		if (Bimp2.mArrayList.size() > 0) {// 设置图片
			imgShow.setImageBitmap(Bimp2.mArrayList.get(0).getBitmap());

		}

		btnGetTime.setOnClickListener(new OnClickListener() {

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
						ActivityModifyManagerInfo.this, Datelistener, year,
						month, day);
				dpd.show();// 显示DatePickerDialog组件

			}
		});
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				RadioButton r = (RadioButton) findViewById(arg1);
				sex = r.getText().toString();
			}
		});
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				age = Integer.valueOf(arg0.getItemAtPosition(arg2).toString());// 获取年龄
				// tv_age.setText("当前选择" + String.valueOf(age) + "岁");

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				Toast.makeText(ActivityModifyManagerInfo.this, "对不起，您什么都没有选中",
						Toast.LENGTH_SHORT).show();

			}
		});

		// updata = new ServiceManager();// 获取服务
		pop = new PopupWindow(ActivityModifyManagerInfo.this);

		View view = getLayoutInflater().inflate(
				R.layout.popupitemwindowsaddimge, null);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);

		imgIco.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

			}
		});
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pop.dismiss();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();// 拍照获取图片
				pop.dismiss();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {// 从相册中获取图片
			public void onClick(View v) {
				Intent intent = new Intent(ActivityModifyManagerInfo.this,
						ActivityAlbumAddHouse2.class);
				startActivity(intent);// 从文件中获取图片
				finish();
				pop.dismiss();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
			}
		});
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				time = String.valueOf(year) + "-" + String.valueOf(month) + "-"
						+ String.valueOf(day);

				name = edName.getText().toString();
				try {
					phone = Integer.valueOf(edPhone.getText().toString());
					if (time.equals("") || name.equals("") || sex.equals("")) {
						Toast.makeText(ActivityModifyManagerInfo.this,
								"请输入完整信息", Toast.LENGTH_SHORT).show();

					} else {
						try {
							save_info(name, sex, Integer.valueOf(age),
									Integer.valueOf(phone), time);
							Log.v("管理员信息添加",
									time + name + sex + String.valueOf(age)
											+ String.valueOf(phone));

						} catch (NumberFormatException e) {
							Toast.makeText(ActivityModifyManagerInfo.this,
									"对不起，您输入的信息有误", Toast.LENGTH_SHORT).show();
						}

						map.put("manager_name", edName.getText().toString());
						map.put("manager_sex", sex);
						map.put("manager_age", String.valueOf(age));
						map.put("manager_phone", String.valueOf(phone));
						map.put("manager_time", time);
						map.put("manager_account", Manager.manager_id);
						Log.v("上传管理员信息", "姓名=" + edName.getText().toString()
								+ "   性别=" + sex + "age=" + String.valueOf(age)
								+ "   phone=" + String.valueOf(phone)
								+ "    time=" + time);
						if (Bimp2.mArrayList.size() == 0) {
							Toast.makeText(ActivityModifyManagerInfo.this,
									"您还没有加入图片呢", Toast.LENGTH_SHORT).show();

						} else {
							AsynModifyManagerInfo mUploadPictureTask = new AsynModifyManagerInfo(
									ActivityModifyManagerInfo.this);
							mUploadPictureTask.execute();

						}

					}

				}

				catch (Exception e) {
				}
			}
		});

		imgShow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (Bimp2.mArrayList.size() == 0) {
					Toast.makeText(ActivityModifyManagerInfo.this, "您还没有添加图片",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();// 返回

			}
		});

	}

	private void photo() {// 拍照获取信息
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	private void get_ma_info(Context context) {
		// 从存储的shared中获取出来如果存在
		SharedPreferences sp = context.getSharedPreferences(
				ActivityManagerInfo.CONFIG, Context.MODE_PRIVATE);
		// mpath = sp.getString("path",
		// String.valueOf(R.drawable.head_icon_user));
		name = sp.getString("name", "未添加资料");
		time = sp.getString("time", "未添加资料");
		age = sp.getInt("age", 0);
		phone = sp.getInt("phone", 000);
		sex = sp.getString("sex", "未添加资料");
		tvAge.setText("当前年龄 " + String.valueOf(age) + " 岁");
		tvTime.setText(time);
		edName.setText(name);
		edPhone.setText(String.valueOf(phone));

	}

	public void save_info(String name, String sex, int age, int phone,
			String time) {// 保存seller_id,password

		SharedPreferences sp = ActivityModifyManagerInfo.this
				.getSharedPreferences(ActivityManagerInfo.CONFIG, MODE_PRIVATE);
		SharedPreferences.Editor edit = sp.edit();

		// edit.putString("path", mpath);
		edit.putString("name", name);
		edit.putInt("age", age);
		edit.putInt("phone", phone);// 保存信息
		edit.putString("time", time);
		edit.putString("sex", sex);

		edit.commit();
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
					imgShow.setImageBitmap(bitmap);

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
						imgShow.setImageBitmap(bitmap);

					}
				}
			}
			break;
		}
	}

	public class AsynModifyManagerInfo extends
			AsyncTask<Image_item, Integer, String> {

		private ProgressDialog pdialog;
		private Activity activity;
		private ArrayList<Image_item> mArrayList = Bimp2.mArrayList;

		public AsynModifyManagerInfo(Context context) {
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
			return FormInfoService.post(ServletPath.MANAGERUPDATEINFO, map,
					fiFormFiles);

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			pdialog.setProgress(values[0]);
		}
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
			tvTime.setText("" + year + "-" + (month + 1) + "-" + day);
		}
	};

	public void onBackPressed() {
		Bimp2.mArrayList.clear();
		finish();
	};

}
