package com.utl;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Common_util {
	// 判断网络适用情况

	public static boolean is_net_work_connected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);// 系统级管理服务
			NetworkInfo mneNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mneNetworkInfo != null) {
				return mneNetworkInfo.isAvailable();// 返回网络的可用性
			}
		}

		return false;

	}

	public static boolean is_exits_sdcard() {//android会模拟一块外部存/storage/emlate/0
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {// 已经挂载并且拥有可读可写权限
			return true;

		} else {
			return false;
		}

	}
}
