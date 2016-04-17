package com.beans;

public class Notice {
	String time, content;
	int id;

	public Notice(int id, String time, String content) {
		this.id = id;
		this.time = time;
		this.content = content;

	}

	public String getTime() {
		return time;
	}

	public String getContent() {
		return content;
	}

	public int getId() {
		return id;
	}

}
