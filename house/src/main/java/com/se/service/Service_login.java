package com.se.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.exception.Login_exception;
import com.se.inter.use.Inter_seller_login;
import com.utl.Static_value;

public class Service_login implements Inter_seller_login {

	@Override
	public int seller_account(String url, String login_name,
			String login_password) throws Exception {
		int a = 0;
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response;
		response = httpClient.execute(get);
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Login_exception(Static_value.FlAG_LOGIN_SERVER_ERROR);

		} else {
			String result = EntityUtils.toString(response.getEntity(),
					HTTP.UTF_8);// 工具类获取
			int b = Integer.parseInt(result);

			if (b == 1) {
				a = b;

			} else {
				throw new Login_exception(Static_value.MSG_LOGIN_FAILED);

			}
			return a;

		}
	}
}
