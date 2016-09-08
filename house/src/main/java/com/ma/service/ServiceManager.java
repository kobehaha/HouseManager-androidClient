package com.ma.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.beans.Manager;
import com.ma.interfac.ManagerInterface;
import com.utl.ServletPath;

public class ServiceManager implements ManagerInterface {
	private String json;
	private JSONObject jsonObject;

	@Override
	public Manager get_manager() throws ClientProtocolException, IOException,
			JSONException {

		String uri, name, sex, time;
		int age, phone;
		HttpClient httpClient = new DefaultHttpClient();
		String url = ServletPath.MANAGERINFOTOGET + "&managerAccount="
				+ Manager.manager_id;
		HttpGet get = new HttpGet(url);
		get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				1000);
		get.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);
		HttpResponse response = httpClient.execute(get);
		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(is, "utf-8"));
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			builder.append(line + "\n");

		}
		is.close();
		json = builder.toString();
		jsonObject = new JSONObject(json);
		System.out.println(json);

		name = jsonObject.getString("name");
		sex = jsonObject.getString("sex");
		age = jsonObject.getInt("age");
		uri = jsonObject.getString("picture");
		time = jsonObject.getString("dateString");
		phone = jsonObject.getInt("phone");
		Manager manager = new Manager(uri, name, sex, age, phone, time);

		return manager;
	}

	@Override
	public int ma_info_upload(String name, String sex, int age, int phone,
			String time) throws Exception {
		HttpClient httpClient = new DefaultHttpClient();

		String url2 = ServletPath.MANGERIFNOUPLOAD + "&manager_name=" + name
				+ "&manager_sex=" + sex + "&manager_age=" + String.valueOf(age)
				+ "&manager_phone=" + String.valueOf(phone) + "&manager_time="
				+ "1995-2-14" + "&manager_account=" + Manager.manager_id;// 操作的url
		Log.v("上传的数据", url2);

		HttpGet get = new HttpGet(url2);
		get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				1000);
		get.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);

		HttpResponse response;
		response = httpClient.execute(get);
		if (response.getStatusLine().getStatusCode() != 200) {
			return 0;

		} else {
			return 1;
		}
	}

}
