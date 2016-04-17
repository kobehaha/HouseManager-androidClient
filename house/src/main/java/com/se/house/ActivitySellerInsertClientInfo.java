package com.se.house;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import com.ma.photo.activity.ActivityAlbumAddHouse2;
import com.ma.photo.utl.Bimp2;
import com.ma.photo.utl.Image_item;
import com.ma.service.FormInfoService;
import com.special.ResideMenuDemo.R;
import com.utl.FormFile;
import com.utl.ServletPath;

public class ActivitySellerInsertClientInfo extends BasicActivity {
	private ImageView imageBack, imgChoosePicture, imgShow;
	private Button btnInsertSure;
	private RadioGroup radioGroup;
	private EditText editPhone, editWeichart, edName;
	private Spinner spinner;
	private ArrayAdapter<CharSequence> adapter;
	private TextView tvAge;
	private int age;
	private String phone;
	private String strName, strWeichart, strSex;
	private int[] arryHouse;
	private View parentView;
	private PopupWindow pop;
	private static final int TAKE_PICTURE = 0x000002;
	private static final String flag = "buyerIco";
	private Map<String, String> map = new HashMap<String, String>();
	private String houseIds = "";
	private static int count = 0;// 提交次数
	private AlertDialog.Builder builder;
	private AlertDialog.Builder builderInfo;// 提示不能重复提交

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parentView = getLayoutInflater().inflate(
				R.layout.maactivityaddhouseinfo, null);
		setContentView(R.layout.seactivityinsertclientinfo);
		init();
		get_intent();
	}

	private void get_intent() {
		Bundle bundle = getIntent().getExtras();
		arryHouse = bundle.getIntArray("all_house_id");
		System.out.println("arryhouse " + String.valueOf(arryHouse[0]));

	}

	private void init() {
		builder = new AlertDialog.Builder(ActivitySellerInsertClientInfo.this);
		builderInfo = new AlertDialog.Builder(
				ActivitySellerInsertClientInfo.this);
		imgShow = (ImageView) findViewById(R.id.img_se_info_inco_show);
		imgChoosePicture = (ImageView) findViewById(R.id.img_se_modify_icon);
		imageBack = (ImageView) findViewById(R.id.back7);
		btnInsertSure = (Button) findViewById(R.id.btn_insert_client_info_sure);
		radioGroup = (RadioGroup) findViewById(R.id.ra_client_info_sex);
		edName = (EditText) findViewById(R.id.ed_client_name);
		spinner = (Spinner) findViewById(R.id.spinner_client_info_modify_age);
		editWeichart = (EditText) findViewById(R.id.ed_client_weichart);
		editPhone = (EditText) findViewById(R.id.ed_client_phone);
		tvAge = (TextView) findViewById(R.id.tv_client_info_now_age);
		adapter = ArrayAdapter.createFromResource(this, R.array.age,
				android.R.layout.simple_dropdown_item_1line);
		spinner.setAdapter(adapter);

		if (Bimp2.mArrayList.size() > 0) {// 设置图片
			imgShow.setImageBitmap(Bimp2.mArrayList.get(0).getBitmap());

		}

		pop = new PopupWindow(ActivitySellerInsertClientInfo.this);

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

		imgChoosePicture.setOnClickListener(new OnClickListener() {

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
				Intent intent = new Intent(ActivitySellerInsertClientInfo.this,
						ActivityAlbumAddHouse2.class);
				intent.putExtra("flag", flag);
				Bundle bundle = new Bundle();
				bundle.putIntArray("all_house_id", arryHouse);
				intent.putExtras(bundle);
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

		btnInsertSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (count == 0) {
					builder.setTitle("你确定提交吗？");
					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// 这里添加点击确定后的逻辑
									phone = editPhone.getText().toString();
									strName = edName.getText().toString();
									strWeichart = editWeichart.getText()
											.toString();
									if (strName.equals("")
											|| strWeichart.equals("")) {
										Toast.makeText(
												ActivitySellerInsertClientInfo.this,
												"请完善信息", Toast.LENGTH_SHORT)
												.show();

									} else {
										map.put("seller_id",
												Seller_account.seller_id);

										for (int i = 0; i < arryHouse.length; i++) {
											houseIds = houseIds
													+ String.valueOf(arryHouse[i])
													+ "A";

										}
										Log.v("houseIds==", houseIds);
										map.put("house_id", houseIds);
										System.out.println(" seller sure house Id "
												+ String.valueOf(arryHouse));
										map.put("name", strName);
										map.put("age", String.valueOf(age));
										map.put("phone", phone);
										map.put("weichart", strWeichart);
										map.put("sex", strSex);

										new AsynUploadBuyerInfo(
												ActivitySellerInsertClientInfo.this)
												.execute();
										count++;// 次数加一

									}

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

				} else {
					builderInfo.setTitle("您已经提交过了一次了，请修改房源信息重新提交");
					builderInfo.create().show();


				}

			}
		});
		imageBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				age = Integer.valueOf(parent.getItemAtPosition(position)
						.toString());

				tvAge.setText("您当前选择的年龄是" + String.valueOf(age) + "岁");

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton radioButton = (RadioButton) group
						.findViewById(checkedId);
				strSex = radioButton.getText().toString();

			}
		});

	}

	private void photo() {// 拍照获取信息
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	public class AsynUploadBuyerInfo extends
			AsyncTask<Image_item, Integer, String> {

		private ProgressDialog pdialog;
		private Activity activity;
		private ArrayList<Image_item> mArrayList = Bimp2.mArrayList;

		public AsynUploadBuyerInfo(Context context) {
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
				Bimp2.mArrayList.clear();// 记录选中的图片删除
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

			return FormInfoService.post(ServletPath.SELLERINSERTBUYER, map,
					fiFormFiles);

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			pdialog.setProgress(values[0]);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			startActivity(new Intent(ActivitySellerInsertClientInfo.this,
					MenuActivity.class));// 打开主界面的同时，关闭次activity
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
