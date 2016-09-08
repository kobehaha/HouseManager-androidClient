package com.ma.interfac;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.beans.House;

public interface HouseInterface {
	public String add_house_info(int manager_id, String name, String location,
			int price, int year, String brief, String owner_name,
			String owner_sex, int owner_phone, String owner_weichart)
			throws ClientProtocolException, IOException;

	public List<House> getAllHouses() throws JSONException,
			ClientProtocolException, IOException;

	public House getHouseById(int id) throws Exception;

}
