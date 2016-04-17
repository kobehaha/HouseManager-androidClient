package com.se.inter.use;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.beans.Bargain;
import com.beans.Client;
import com.beans.Seller;

public interface SellerInterface {

	public int seller_account(String login_name, String login_password)
			throws Exception;

	public Seller getSellerInfo() throws ClientProtocolException, IOException,
			JSONException;

	public ArrayList<Object> getSuccessBargains() throws Exception;

	public ArrayList<Client> getAllBuyers() throws Exception;

	public Map<String, Object> getBuyerRecord(int buyerId) throws Exception;

	public boolean deleteBuyerById(int buyerId) throws Exception;

	public boolean updateBuyerHouseIds(Client buyer, String houseIds)
			throws Exception;

	public boolean dealBargain(Bargain bargain) throws Exception;//交易中

}
