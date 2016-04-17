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

import com.beans.House;
import com.beans.Manager;
import com.ma.interfac.HouseInterface;
import com.utl.ServletPath;
import com.utl.Url;

public class ServiceHouse implements HouseInterface {
	private JSONObject json_object = null;
	private String json;
	String picture_url, name, brief, location;// 初始化
	int size, year, price, id;

	@Override
	public String add_house_info(int manager_id, String name, String location,
			int price, int year, String brief, String owner_name,
			String owner_sex, int owner_phone, String owner_weichart)
			throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		String url2 = ServletPath.MANAGERADDHOUSE + "?operate="
				+ "uploadOwnerAndHouseInfo" + "&managerID=" + manager_id
				+ "&houseName=" + name + "&house_locaiton=" + location
				+ "&house_price=" + price + "&house_year=" + year
				+ "&house_brief=" + brief + "&owner_name" + owner_name
				+ "&owner_sex" + owner_sex + "&owner_phone" + owner_phone
				+ "&owner_weichart=" + owner_weichart;

		HttpGet get = new HttpGet(url2);
		get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				1000);
		get.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);

		HttpResponse response;
		response = httpClient.execute(get);
		if (response.getStatusLine().getStatusCode() != 200) {
			return ServletPath.SUCCESS;

		} else {
			return ServletPath.FAILURE;
		}

	}

	@Override
	public List<House> getAllHouses() throws JSONException,
			ClientProtocolException, IOException {

		ArrayList<House> houses = new ArrayList<House>();

		HttpClient httpClient = new DefaultHttpClient();
		String url2 = ServletPath.GETALLHOUSE + "&managerAccount="
				+ Manager.manager_id;
		HttpGet get = new HttpGet(url2);
		get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				1000);
		get.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);

		HttpResponse response;
		response = httpClient.execute(get);
		HttpEntity entity = response.getEntity();// get entity
		InputStream is = entity.getContent();// 获取内容
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
				"UTF-8"));// change stream to
							// reader

		StringBuilder sbBuilder = new StringBuilder();// a build cach for
		String line = null;
		while ((line = reader.readLine()) != null) {// read start
			sbBuilder.append(line);// add to sbBuilder

		}
		is.close();
		json = sbBuilder.toString();
		json_object = new JSONObject(json);
		Log.v("管理员接受房源json数据", json_object.toString());
		JSONArray Jsonarray = json_object.getJSONArray("house_info");
		for (int i = 0; i < Jsonarray.length(); i++) {

			JSONObject jsonObject = Jsonarray.getJSONObject(i);
			picture_url = jsonObject.getString("picture");
			String[] url3 = picture_url.split("#");
			for (int j = 0; j < url3.length; j++) {
				url3[j] = url3[j];// 添加每一个url

			}
			id = jsonObject.getInt("houseID");
			name = jsonObject.getString("name");
			size = jsonObject.getInt("size");
			year = jsonObject.getInt("years");
			brief = jsonObject.getString("brief");
			price = jsonObject.getInt("price");
			location = jsonObject.getString("location");
			houses.add(new House(url3, id, name, location, brief, size, year,
					price));

		}

		return houses;
	}

	@Override
	public House getHouseById(int id) throws Exception {
		House house;
		String url = ServletPath.GETHOUSEINFOBYID + "&houseId=" + id;
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				1000);
		get.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);

		HttpResponse response;
		response = httpClient.execute(get);
		HttpEntity entity = response.getEntity();// get entity
		InputStream is = entity.getContent();// 获取内容
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
				"UTF-8"));// change stream to
							// reader

		StringBuilder sbBuilder = new StringBuilder();// a build cach for
		String line = null;
		while ((line = reader.readLine()) != null) {// read start
			sbBuilder.append(line);// add to sbBuilder

		}
		is.close();
		json = sbBuilder.toString();
		json_object = new JSONObject(json);
		Log.v("房源json数据", json_object.toString());

		String picture_url = json_object.getString("picture");
		String[] url3 = picture_url.split("#");
		for (int j = 0; j < url3.length; j++) {
			url3[j] = "http://" + Url.url + "/" + url3[j];// 添加每一个url

		}
		id = json_object.getInt("houseID");
		name = json_object.getString("name");
		size = json_object.getInt("size");
		year = json_object.getInt("years");
		brief = json_object.getString("brief");
		price = json_object.getInt("price");
		location = json_object.getString("location");
		house = new House(url3, id, name, location, brief, size, year, price);

		return house;
	}

}
