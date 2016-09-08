package com.se.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.beans.Bargain;
import com.beans.Client;
import com.beans.House;
import com.beans.Seller;
import com.se.house.Seller_account;
import com.se.inter.use.SellerInterface;
import com.utl.ServletPath;
import com.utl.Url;

public class ServiceSeller implements SellerInterface {
	private String json;
	private JSONObject jsonObject2;

	@Override
	public int seller_account(String login_name, String login_password)
			throws Exception {
		return 0;
	}

	@Override
	public Seller getSellerInfo() throws ClientProtocolException, IOException,
			JSONException {

		String uri, name, sex, time, degree, weichart;
		int age, phone, id;
		HttpClient httpClient = new DefaultHttpClient();
		String url = ServletPath.SELLERGETINFO + "&sellerAccount="
				+ Seller_account.seller_id;
		HttpGet get = new HttpGet(url);

		get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				1000);
		get.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);
		Log.v("获取销售人员信息", url);
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
		Log.v("seInfoJson", json);
		jsonObject2 = new JSONObject(json);
		JSONArray array = jsonObject2.getJSONArray("jsonArray");
		JSONObject jsonObject = array.getJSONObject(0);
		int status = jsonObject.getInt("status");
		Log.v("status=    ", String.valueOf(status));
		if (status == 1) {
			JSONObject jsonObject3 = array.getJSONObject(1);
			degree = jsonObject3.getString("degree");
			uri = jsonObject3.getString("url");
			name = jsonObject3.getString("name");
			sex = jsonObject3.getString("sex");
			age = jsonObject3.getInt("age");
			time = jsonObject3.getString("time");
			phone = jsonObject3.getInt("phone");
			id = jsonObject3.getInt("account");
			weichart = jsonObject3.getString("weichart");
			Seller seller = new Seller(uri, name, sex, time, degree, phone, id,
					age, weichart);
			Log.v("seller info", seller.toString());
			return seller;

		} else {
			return null;
		}

	}

	@Override
	public ArrayList<Object> getSuccessBargains() throws Exception {
		String houseId, buyerId, time, price, timeok, sellerName;
		HttpClient httpClient = new DefaultHttpClient();
		String url = ServletPath.SELLERGETSUCCESSBARGAIN + "&selleraccount="
				+ Seller_account.seller_id;
		HttpGet get = new HttpGet(url);

		get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				1000);
		get.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);
		Log.v("获取销售人员信息", url);
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
		ArrayList<Object> objects = new ArrayList<Object>();
		Log.v("sellerGetSuccessBargain", json);
		jsonObject2 = new JSONObject(json);
		Seller seller = new Seller();
		String name = jsonObject2.getString("sellerName");
		String picture = jsonObject2.getString("sellerPicture");
		seller.setUrl(picture);
		seller.setName(name);

		objects.add(seller);
		ArrayList<List<Bargain>> bargains = new ArrayList<List<Bargain>>();
		JSONArray array = jsonObject2.getJSONArray("getBargains");
		for (int i = 0; i < array.length(); i++) {
			JSONArray array2 = array.getJSONArray(i);
			List<Bargain> bargain1 = new ArrayList<Bargain>();
			for (int j = 0; j < array2.length(); j++) {
				JSONObject jsonObject = array2.getJSONObject(j);
				time = jsonObject.getString("houseId");
				houseId = jsonObject.getString("houseId");
				buyerId = jsonObject.getString("buyerId");
				time = jsonObject.getString("buyerId");
				timeok = jsonObject.getString("timeok");
				price = jsonObject.getString("price");
				Bargain bargain = new Bargain();
				bargain.setHouseId(houseId);
				bargain.setTime(time);
				bargain.setBuyerId(buyerId);
				bargain.setPrice(Integer.valueOf(price));
				bargain.setTimeOk(timeok);
				bargain1.add(bargain);
				Log.v("加载bargain数据", bargain.toString());

			}
			bargains.add(bargain1);
			Log.v("加载数据次数", String.valueOf(bargains.size()));

		}
		objects.add(bargains);

		return objects;

	}

	@Override
	public ArrayList<Client> getAllBuyers() throws Exception {

		ArrayList<Client> clients = new ArrayList<Client>();

		HttpClient httpClient = new DefaultHttpClient();
		String url2 = ServletPath.SELLERGETALLBUYER + Seller_account.seller_id;
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
		jsonObject2 = new JSONObject(json);
		Log.v("buyer json数据", jsonObject2.toString());
		JSONArray Jsonarray = jsonObject2.getJSONArray("buyerInfo");
		String picture_url, name;
		int buyerID, status;

		for (int i = 0; i < Jsonarray.length(); i++) {

			JSONObject jsonObject = Jsonarray.getJSONObject(i);
			picture_url = "http://" + Url.url + "/"
					+ jsonObject.getString("url");

			buyerID = jsonObject.getInt("buyerID");
			name = jsonObject.getString("name");
			status = jsonObject.getInt("status");
			Client client = new Client();
			client.setName(name);
			client.setStatus(status);
			client.setUrl(picture_url);
			client.setId(buyerID);
			clients.add(client);

		}
		return clients;

	}

	@Override
	public Map<String, Object> getBuyerRecord(int buyerId) throws Exception {// 获取卖家的记录

		Map<String, Object> map = new HashMap<String, Object>();
		// Map<String , Object> map2=new HashMap<String, Object>();

		HttpClient httpClient = new DefaultHttpClient();

		String url2 = ServletPath.SELLERGETBUYERINFO + buyerId;// url
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
		jsonObject2 = new JSONObject(json);
		Log.v("buyer json数据", jsonObject2.toString());

		Bargain bargain = new Bargain();// 获取的每个数据
		Client client = new Client();
		ArrayList<House> houses = new ArrayList<House>();

		JSONObject object = jsonObject2.getJSONObject("buyerInfo");// 获取buyerinfo
		// buyerInfo":{"age":16,"buyerID":50,"createTime":"2016-13-02:04:13:46","houseId":"39A38A","name":"寰疯幈鏂�","phone":"2","sex":"","status":"","url":"HouseService02/buyerIcoFilePlace/201603031632300.jpg#","weiChat":"2"}
		client.setId(object.getInt("buyerID"));
		client.setPhone(object.getInt("phone"));
		client.setName(object.getString("name"));
		client.setWeichart(object.getString("weiChat"));
		client.setSex(object.getString("sex"));
		client.setUrl(object.getString("url").substring(
				object.getString("url").length() - 1));// 获取显示图片
		client.setAge(object.getInt("age"));

		// bargainInfo":{"account":"2","buyerId":"","houseId":"39A38A","price":"0","status":"0","time":"2016-03-02
		// 16:13:47.0","timeOkMonth":0,"timeOkYear":0,"timeok":"0000-00-00"}

		JSONObject bargainObject = jsonObject2.getJSONObject("bargainInfo");// 获取bagain
		bargain.setTimeOk(bargainObject.getString("timeok"));
		bargain.setTime(bargainObject.getString("time"));
		bargain.setPrice(bargainObject.getInt("price"));

		System.out.println(bargain.toString());

		JSONArray Jsonarray = jsonObject2.getJSONArray("houseInfo");// 获取house
																	// arry

		for (int i = 0; i < Jsonarray.length(); i++) {

			JSONObject jsonObject = null;

			try {

				jsonObject = Jsonarray.getJSONObject(i);// 获取每个对象

			} catch (Exception e) {
				System.out.println("移除第一个null");
			} finally {
				if (jsonObject == null) {
				} else {
					House house1 = new House();
					int houseId = jsonObject.getInt("houseID");
					int size = jsonObject.getInt("size");
					int price = jsonObject.getInt("price");
					String location = jsonObject.getString("location");
					System.out.println("pciture"
							+ jsonObject.getString("picture"));
					String[] pictures = jsonObject.getString("picture").split(
							"#");
					house1.setPictureUrl(pictures);
					house1.setPrice(price);
					house1.setSize(size);
					house1.setId(houseId);
					house1.setLocation(location);
					houses.add(house1);
					map.put("houseinfo", houses);
					map.put("bargaininfo", bargain);
					map.put("buyerinfo", client);

				}

			}

		}

		return map;// 返回map
	}

	@Override
	public boolean deleteBuyerById(int buyerId) throws Exception {

		HttpClient httpClient = new DefaultHttpClient();
		String url2 = ServletPath.SELLERDELETEBUYER + buyerId;
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
		jsonObject2 = new JSONObject(json);
		Log.v("buyer json数据", jsonObject2.toString());

		int flags = jsonObject2.getInt("status");

		if (flags == 1) {
			return true;

		} else {
			return false;
		}

	}

	@Override
	public boolean updateBuyerHouseIds(Client buyer, String houseIds)
			throws Exception {// 更新houseids

		HttpClient httpClient = new DefaultHttpClient();
		String url2 = ServletPath.SELLERUPDATEBUYERHOUSEID + "&buyerId="
				+ buyer.getId() + "&houseIds=" + houseIds;
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
		jsonObject2 = new JSONObject(json);
		Log.v("buyer json数据", jsonObject2.toString());

		int flags = jsonObject2.getInt("status");

		if (flags == 1) {
			return true;

		} else {
			return false;
		}

	}

	@Override
	public boolean dealBargain(Bargain bargain) throws Exception {

		HttpClient httpClient = new DefaultHttpClient();

		// buyerId=17&selleraccount=2&houseId=34&price=23
		String url2 = ServletPath.SELLERDEALHOUSE + "&buyerId="
				+ bargain.getBuyerId() + "&houseId=" + bargain.getHouseId()
				+ "&price=" + bargain.getPrice()+"&selleraccount="+bargain.getSellerAccount();
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
		jsonObject2 = new JSONObject(json);
		Log.v("buyer json数据", jsonObject2.toString());

		int flags = jsonObject2.getInt("status");

		if (flags == 1) {
			return true;

		} else {
			return false;
		}

	}
}
