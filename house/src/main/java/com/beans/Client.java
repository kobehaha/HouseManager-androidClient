package com.beans;

public class Client {

	private int id, phone, age, status;

	private String name, weichart, sex, url;

	public Client(int id, int phone, int age, String name, String weichart,
			String sex) {
		this.id = id;
		this.phone = age;
		this.name = name;
		this.weichart = weichart;
		this.phone = phone;
		this.sex = sex;

	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Client() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWeichart() {
		return weichart;
	}

	public void setWeichart(String weichart) {
		this.weichart = weichart;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

}
