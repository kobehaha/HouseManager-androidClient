package com.ma.interfac;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.beans.Manager;

public interface ManagerInterface {
	public Manager get_manager() throws ClientProtocolException, IOException,
			JSONException;

	public int ma_info_upload(String name, String sex, int age, int phone,
			String time) throws Exception;

}
