package com.ma.interfac;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.beans.Seller;

public interface SellerInterface {
	public String add_seller(String account, String password)
			throws ClientProtocolException, IOException;

	public List<Seller> get_all_seller() throws ClientProtocolException,
			IOException, JSONException;

	public List<Seller> get_least7_seller(int count)
			throws ClientProtocolException, IOException, IllegalStateException,
			JSONException;

}
