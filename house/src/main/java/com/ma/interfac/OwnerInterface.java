package com.ma.interfac;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.beans.Owner;

public interface OwnerInterface {
	public Owner get_owner(int house_id) throws ClientProtocolException,
			IOException, IllegalStateException, JSONException;

}
