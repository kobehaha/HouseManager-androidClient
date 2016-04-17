package com.ma.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.beans.Manager;
import com.beans.Notice;
import com.ma.interfac.NoticeInterface;
import com.utl.ServletPath;

public class ServiceNotice implements NoticeInterface {
	private JSONObject json_object = null;
	private String json;

	@Override
	public String send_notice(String notice, String time)
			throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();

		String url2 = ServletPath.MANAGERSENDNOTICE + "&manager_id="
				+ Manager.manager_id

				+ "&content=" + notice;
		Log.v("传递数据是", "传递时间是" + time + "内容是" + notice + "URL是" + url2);

		HttpGet get = new HttpGet(url2);
		get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				1000);
		get.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);

		HttpResponse response = httpClient.execute(get);
		if (response.getStatusLine().getStatusCode() != 200) {
			return ServletPath.FAILURE;

		} else {
			return ServletPath.SUCCESS;
		}

	}

	@Override
	public List<Notice> get_notice_info() throws ClientProtocolException,
			IOException, JSONException {

		List<Notice> notices = new ArrayList<Notice>();// 定义一个保存notice的变量
		HttpClient httpClient = new DefaultHttpClient();
		String url2 = ServletPath.MANAGERGETNOTICE + "&manager_id="
				+ Manager.manager_id

		;
		HttpGet get = new HttpGet(url2);
		get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				4000);
		get.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);

		HttpResponse response;
		response = httpClient.execute(get);
		HttpEntity entity = response.getEntity();// get entity

		InputStream is = entity.getContent();// 获取内容
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
				"UTF-8"));// change stream to
							// reader
		StringBuilder sbBuilder = new StringBuilder();// a build cach for
		// string
		String line = null;
		while ((line = reader.readLine()) != null) {// read start
			sbBuilder.append(line + "/n");// add to sbBuilder
		}
		is.close();
		json = sbBuilder.toString();
		json_object = new JSONObject(json);
		Log.v("管理员发送通知json数据", json_object.toString());
		JSONArray Jsonarray = json_object.getJSONArray("notice_info");
		for (int i = 0; i < Jsonarray.length(); i++) {

			JSONObject jsonObject2 = Jsonarray.getJSONObject(i);
			Log.v("管理员发送通知json数据>>>", jsonObject2.toString());
			String content = jsonObject2.getString("content");
			String time = jsonObject2.getString("time");
			int id = jsonObject2.getInt("notice_id");
			notices.add(new Notice(id, time, content));

		}

		// }
		return (ArrayList<Notice>) notices;
	}

	@Override
	public String deleteNoticeById(int id) throws Exception {
		HttpClient httpClient = new DefaultHttpClient();

		String url2 = ServletPath.MANGERDELETENOTICEBYID + "&notice_id=" + id;
		Log.v("删除notice ID", "  id＝" + String.valueOf(id));

		HttpGet get = new HttpGet(url2);
		get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				1000);
		get.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);

		HttpResponse response = httpClient.execute(get);
		if (response.getStatusLine().getStatusCode() != 200) {
			return ServletPath.FAILURE;

		} else {
			return ServletPath.SUCCESS;
		}

	}

}
