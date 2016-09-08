package com.beans;

public class Manager {
	public static String manager_id = "1";
	String name, time, sex, url;
	int age, phone;

	public String getName() {
		return name;
	}

	public String getTime() {
		return time;
	}

	public String getSex() {
		return sex;
	}

	public int getAge() {
		return age;
	}

	public int getPhone() {
		return phone;
	}

	public String getUrl() {
		return url;
	}

	public Manager(String url, String name, String sex, int age, int phone,
			String time) {
		this.url = url;
		this.name = name;
		this.sex = sex;
		this.age = age;
		this.phone = phone;
		this.time = time;

	}
}
