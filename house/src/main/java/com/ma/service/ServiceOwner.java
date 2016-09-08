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

import com.beans.Owner;
import com.ma.interfac.OwnerInterface;
import com.utl.ServletPath;

public class ServiceOwner implements OwnerInterface {

	private InputStream is;
	private JSONObject jsonObject;
	private String url = ServletPath.MANAGERGETOWNER;

	@Override
	public Owner get_owner(int house_id) throws ClientProtocolException,
			IOException, IllegalStateException, JSONException {
		Owner onwer;
		String str_name, str_weichart, str_sex;
		int phone, age, id;
		String url2 = url + "&houseId=" + String.valueOf(house_id);
		System.out.println("获取Owner数据是url=" + url2 + "获取Owner数据的房源id是="
				+ house_id);

		HttpClient client = new DefaultHttpClient();

		HttpGet get = new HttpGet(url2);
		get.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1000);
		get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				1000);
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		is = entity.getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
				"utf-8"));
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);

		}
		is.close();
		String kobe = builder.toString();
		jsonObject = new JSONObject(kobe);
		id = jsonObject.getInt("houseID");
		str_name = jsonObject.getString("name");
		str_sex = jsonObject.getString("sex");
		str_weichart = jsonObject.getString("weiChat");
		phone = jsonObject.getInt("phone");
		age = jsonObject.getInt("age");
		onwer = new Owner(id, phone, age, str_name, str_sex, str_weichart);

		return onwer;
	}
}
