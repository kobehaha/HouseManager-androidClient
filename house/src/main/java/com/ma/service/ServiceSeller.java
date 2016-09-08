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
import com.beans.Seller;
import com.ma.interfac.SellerInterface;
import com.utl.ServletPath;

public class ServiceSeller implements SellerInterface {
	private final int wid = 7;
	
	// private static int count = 0;
	// private static int last = 0;
	private JSONObject jsonObject;
	private String jsonString;

	@Override
	public String add_seller(String account, String password)
			throws ClientProtocolException, IOException {
		HttpResponse response;
		HttpClient httpClient = new DefaultHttpClient();

		String url2 = ServletPath.MANAGERADDSELLER + "&seller_account="
				+ account + "&seller_password=" + password + "&managerAccount="
				+ Manager.manager_id;
		Log.v("新建账号传递数据是", "管理员账号是" + Manager.manager_id + "账号和密码是" + account
				+ "和" + password);
		Log.v("新建帐号url", url2);
		// 操作的url
		HttpGet get = new HttpGet(url2);
		get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				1000);
		get.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);

		response = httpClient.execute(get);
		if (response.getStatusLine().getStatusCode() != 200) {
			return ServletPath.FAILURE;

		} else {
			return ServletPath.SUCCESS;
		}

	}

	@Override
	public List<Seller> get_all_seller() throws ClientProtocolException,
			IOException, JSONException {
		String name, sex, degeree, time, url, weichart;

		int phone, age, id;
		List<Seller> list = new ArrayList<Seller>();
		HttpClient client = new DefaultHttpClient();
		String url3 = ServletPath.MANAGERGETSELLER + "&manager_id=" + Manager.manager_id;
		HttpGet get = new HttpGet(url3);

		get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				2000);
		get.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);

		HttpResponse response;

		response = client.execute(get);
		HttpEntity entity = response.getEntity();
		InputStream isInputStream = entity.getContent();
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(isInputStream, "utf-8"));
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			builder.append(line + "\n");

		}
		isInputStream.close();//
		jsonString = builder.toString();
		jsonObject = new JSONObject(jsonString);
		JSONArray array = jsonObject.getJSONArray("seller_info");
		Log.v("seller json数据", jsonString);
		for (int i = 0; i < array.length(); i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			url = jsonObject.getString("url");
			id = jsonObject.getInt("sellerID");
			name = jsonObject.getString("name");
			sex = jsonObject.getString("sex");
			degeree = jsonObject.getString("degree");
			phone = jsonObject.getInt("phone");
			age = jsonObject.getInt("age");
			time = jsonObject.getString("time");
			weichart = "weixin";
			list.add(new Seller(url, name, sex, time, degeree, phone, id, age,
					weichart));

		}

		return list;
	}

	@Override
	public List<Seller> get_least7_seller(int count2)
			throws ClientProtocolException, IOException, IllegalStateException,
			JSONException {
		String name, sex, degeree, time, url, weichart = null;
		int phone, age, id;
		List<Seller> list = new ArrayList<Seller>();
		HttpClient client = new DefaultHttpClient();
		String url3 = ServletPath.MANAGERGETSELLER + "&manager_id=" + Manager.manager_id;
		HttpGet get = new HttpGet(url3);
		get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				2000);
		get.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		InputStream isInputStream = entity.getContent();
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(isInputStream, "utf-8"));
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			builder.append(line + "\n");

		}
		isInputStream.close();

		jsonString = builder.toString();

		jsonObject = new JSONObject(jsonString);
		JSONArray array = jsonObject.getJSONArray("seller_info");
		// count = array.length() / wid;// 可以被执行的次数
		// last = array.length() % wid;// 最后一次的次数
		if (array.length() < wid) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				url = jsonObject.getString("seller_url");
				id = jsonObject.getInt("id");
				name = jsonObject.getString("name");
				sex = jsonObject.getString("sex");
				degeree = jsonObject.getString("degree");
				phone = jsonObject.getInt("phone");
				age = jsonObject.getInt("age");
				time = jsonObject.getString("time");
				weichart = "weixing";
				list.add(new Seller(url, name, sex, time, degeree, phone, id,
						age, weichart));

			}

		} else {

			for (int i = 0; i < wid; i++) {
				JSONObject jsonObject = array.getJSONObject(count2 * 7 + i);
				url = jsonObject.getString("seller_url");
				id = jsonObject.getInt("id");
				name = jsonObject.getString("name");
				sex = jsonObject.getString("sex");
				degeree = jsonObject.getString("degree");
				phone = jsonObject.getInt("phone");
				age = jsonObject.getInt("age");
				time = jsonObject.getString("time");
				list.add(new Seller(url, name, sex, time, degeree, phone, id,
						age, weichart));

			}
		}

		return null;
	
	}

}
