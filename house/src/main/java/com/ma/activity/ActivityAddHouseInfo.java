package com.ma.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.beans.Manager;
import com.beans.Owner2;
import com.ma.photo.activity.ActivityAlbumAddHouse;
import com.ma.photo.activity.GalleryActivity;
import com.ma.photo.utl.Bimp;
import com.ma.photo.utl.FileUtils;
import com.ma.photo.utl.Image_item;
import com.ma.photo.utl.Public_way;
import com.ma.service.FormInfoService;
import com.se.house.BasicActivity;
import com.special.ResideMenuDemo.R;
import com.utl.FormFile;
import com.utl.ServletPath;

@SuppressLint("InlinedApi")
public class ActivityAddHouseInfo extends BasicActivity {

	private GridView noScrollgridview;
	private GridAdapter adapter;
	private EditText ed_house_name, ed_house_location, ed_house_price,
			ed_house_size, ed_house_year, ed_house_brief;// 初始化
	private static final int TAKE_PICTURE = 0x000001;
	private Button img_sure;
	private static String owner_name, owner_sex, owner_weichart;
	private static String house_name, house_location, house_brief;
	private static int house_size, house_price, house_year;
	private static int age, phone;
	private View parentView;
	private PopupWindow pop = null;
	public static Bitmap bimap;
	private Map<String, String> map;

	private Owner2 owner;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		parentView = getLayoutInflater().inflate(
				R.layout.maactivityaddhouseinfo, null);
		
		setContentView(R.layout.maactivityaddhouseinfo);

		Public_way.activities.add(this);// 添加此activity
		init();// 初始化
		get_owner();
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));// 设置gird透明
		adapter = new GridAdapter(this);
		adapter.update();// 每次实力化更新
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if (arg2 == Bimp.mArrayList.size()) {
					Log.v("test", "增加图片");
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

				} else {
					Intent intent = new Intent(ActivityAddHouseInfo.this,
							GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}

			}
		});

	}

	private void get_owner() {
		Bundle bundle = getIntent().getExtras();
		int a = bundle.getInt("sure");
		Log.v("从相册返回的sure值", String.valueOf(a));
		if (a == 0) {
			Log.v("back to ma_add_house_info", "从相册返回");

		} else {
			owner = (Owner2) bundle.getSerializable("owner");
			owner_name = owner.getName();
			owner_sex = owner.getSex();
			owner_weichart = owner.getWeichart();
			age = owner.getAge();
			phone = owner.getPhone();
			Log.v("测试数据", owner_name + owner_sex + owner_weichart);

		}

	}

	@SuppressLint("NewApi")
	private void init() {
		ed_house_brief = (EditText) findViewById(R.id.ed_ma_house_brief);
		ed_house_location = (EditText) findViewById(R.id.ed_ma_house_location);
		ed_house_year = (EditText) findViewById(R.id.ed_ma_house_year);
		ed_house_price = (EditText) findViewById(R.id.ed_ma_house_price);
		ed_house_size = (EditText) findViewById(R.id.ed_ma_house_size);
		ed_house_name = (EditText) findViewById(R.id.ed_ma_house_name);
		img_sure = (Button) findViewById(R.id.btn_ma_add_house_sure);

		noScrollgridview = (GridView) findViewById(R.id.grid_choose_picture);// 家在girdview
		pop = new PopupWindow(ActivityAddHouseInfo.this);

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
				Intent intent = new Intent(ActivityAddHouseInfo.this,
						ActivityAlbumAddHouse.class);
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
		img_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {// 获取数据
				try {
					house_name = ed_house_name.getText().toString();
					house_location = ed_house_location.getText().toString();
					house_size = Integer.parseInt(ed_house_size.getText()
							.toString());
					house_price = Integer.parseInt(ed_house_price.getText()
							.toString());
					house_year = Integer.parseInt(ed_house_year.getText()
							.toString());
					house_brief = ed_house_brief.getText().toString();

					get_owner();

					map = new HashMap<String, String>();// 传递的文字参数
					map.put("house_name", house_name);
					map.put("managerID", String.valueOf(Manager.manager_id));
					Log.v("上传房源时候的mangerID＝", Manager.manager_id);
					map.put("house_location", house_location);
					map.put("house_price", String.valueOf(house_price));
					map.put("house_year", String.valueOf(house_year));
					map.put("house_brief", house_brief);
					map.put("house_size", String.valueOf(house_size));
					map.put("owner_name", owner_name);
					map.put("owner_weichart", owner_weichart);
					map.put("owner_sex", owner_sex);
					map.put("owner_phone", String.valueOf(phone));
					map.put("owner_age", String.valueOf(age));

					if (Bimp.mArrayList.size() == 0) {
						Toast.makeText(ActivityAddHouseInfo.this, "您还没有加入图片呢",
								Toast.LENGTH_SHORT).show();

					} else {
						UploadPictureTask mUploadPictureTask = new UploadPictureTask(
								ActivityAddHouseInfo.this);
						mUploadPictureTask.execute();

					}

				} catch (NumberFormatException e) {
					Toast.makeText(ActivityAddHouseInfo.this,
							"对不起您输入的数据有问题，请重新输入", Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	private void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.mArrayList.size() < 9 && resultCode == RESULT_OK) {
				String fileName = String.valueOf(System.currentTimeMillis());
				Uri uri = data.getData();
				if (uri != null) {
					bimap = BitmapFactory.decodeFile(uri.getPath());
					System.out.println("uri不为空" + uri);
					FileUtils.saveBitmap(bimap, fileName);
					String filePath = FileUtils.SDPATH + fileName + ".JPEG";
					Image_item takePhoto = new Image_item();
					takePhoto.setImge_path(filePath);
					takePhoto.setBitmap(bimap);
					Bimp.mArrayList.add(takePhoto);

				}

				if (bimap == null) {
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						bimap = (Bitmap) bundle.get("data");
						System.out.println("data不为空" + bundle.toString());
						FileUtils.saveBitmap(bimap, fileName);
						String filePath = FileUtils.SDPATH + fileName + ".JPEG";
						Image_item takePhoto = new Image_item();
						takePhoto.setImge_path(filePath);
						takePhoto.setBitmap(bimap);
						Bimp.mArrayList.add(takePhoto);

					}
				}

				// Image_item takePhoto = new Image_item();
				//
				// takePhoto.setImge_path(filePath);
				// takePhoto.setSelected(true);
				// takePhoto.setBitmap(bm);
				// Bimp.mArrayList.add(takePhoto);// 添加bitmap
			}
			break;
		}
	}

	public class UploadPictureTask extends
			AsyncTask<Image_item, Integer, String> {

		private ProgressDialog pdialog;
		private Activity activity;
		private ArrayList<Image_item> mArrayList = Bimp.mArrayList;

		public UploadPictureTask(Context context) {
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
			FormFile[] fiFormFiles = new FormFile[mArrayList.size()];

			for (int i = 0; i < mArrayList.size(); i++) {
				file = new File(mArrayList.get(i).getImge_path());
				FormFile formfile = new FormFile(file.getName(), file, "image",
						"application/octet-stream");
				fiFormFiles[i] = formfile;
			}
			Log.v("房源上传file图片个数", String.valueOf(fiFormFiles.length));
			return FormInfoService.post(ServletPath.MANAGERUPLOADOWNERANDHOUSE,
					map, fiFormFiles);

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			pdialog.setProgress(values[0]);
		}
	}

	class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);

		}

		public void update() {
			loading();
		}

		@Override
		public int getCount() {
			if (Bimp.mArrayList.size() == 9) {
				return 9;
			}
			return (Bimp.mArrayList.size() + 1);
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {// 设置选中position
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.itempublishedgrida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.mArrayList.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.mArrayList.get(position)
						.getBitmap());
				// Log.v("每次更新girdview的视图路径", Bimp.mArrayList.get(position)
				// .getImge_path());
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				adapter.notifyDataSetChanged();
				break;
			}
			super.handleMessage(msg);
		}
	};

	public void loading() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					if (Bimp.max == Bimp.mArrayList.size()) {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
						break;
					} else {
						Bimp.max += 1;
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}
				}
			}
		}).start();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {// 关闭打开一系列的activity
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			for (int i = 0; i < Public_way.activities.size(); i++) {
				if (null != Public_way.activities.get(i)) {
					Public_way.activities.get(i).finish();
				}
			}
			System.exit(0);
		}
		return true;
	}

}
