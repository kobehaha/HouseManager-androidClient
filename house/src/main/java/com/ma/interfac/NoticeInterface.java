package com.ma.interfac;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.beans.Notice;

public interface NoticeInterface {
	public String send_notice(String notice, String time)
			throws ClientProtocolException, IOException;

	public List<Notice> get_notice_info() throws ClientProtocolException,
			IOException, JSONException;

	public String deleteNoticeById(int id) throws Exception;
}
