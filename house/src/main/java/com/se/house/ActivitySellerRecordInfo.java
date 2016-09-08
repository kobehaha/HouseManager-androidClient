package com.se.house;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.special.ResideMenuDemo.R;

public class ActivitySellerRecordInfo extends BasicActivity {
	private ImageView mImageView;
	private TextView bargainName, bargainTime, bargainTimeOk, bargainPrice,
			buyerName, buyerSex, buyerAge, buyerPhone, buyerWeichartTextView;
	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seactivityrecordinfo);
		init();
	}

	private void init() {
		bargainName = (TextView) findViewById(R.id.tv_bargainName);
		bargainPrice = (TextView) findViewById(R.id.tv_bargainPrice);
		bargainTime = (TextView) findViewById(R.id.tv_bargainTime);
		bargainTimeOk = (TextView) findViewById(R.id.tv_bargainTimeOk);
		buyerAge = (TextView) findViewById(R.id.buyer_age);
		buyerName = (TextView) findViewById(R.id.buyer_name);
		buyerPhone = (TextView) findViewById(R.id.buyer_phone);
		buyerWeichartTextView = (TextView) findViewById(R.id.buyer_weichart);
		buyerSex = (TextView) findViewById(R.id.buyer_sex);
		mListView = (ListView) findViewById(R.id.lv_houseinfo);
		mImageView = (ImageView) findViewById(R.id.iv_seller_ico);
		

	}
}
