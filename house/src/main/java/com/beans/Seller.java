package com.beans;

public class Seller {
	public void setTime(String time) {
		this.time = time;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public void setWeichart(String weichart) {
		this.weichart = weichart;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setAge(int age) {
		this.age = age;
	}

	private String url, name, sex, time, degree, weichart;
	private int phone, id, age;
	private int MothCount;
	private int YearCount;

	public Seller() {
	}

	public Seller(String url, String name, String sex, String time,
			String degree, int phone, int id, int age, String weichart) {
		this.url = url;
		this.name = name;
		this.sex = sex;
		this.time = time;
		this.degree = degree;
		this.phone = phone;
		this.age = age;
		this.weichart = weichart;
		this.id = id;
	}

	public void setMothCount(int mothCount) {
		MothCount = mothCount;
	}

	public void setYearCount(int yearCount) {
		YearCount = yearCount;
	}

	public int getMothCount() {
		return MothCount;
	}

	public int getYearCount() {
		return YearCount;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWeichart() {
		return weichart;
	}

	public int getAge() {
		return age;
	}

	public int getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public String getName() {
		return name;
	}

	public String getSex() {
		return sex;
	}

	public String getTime() {
		return time;
	}

	public String getDegree() {
		return degree;
	}

	public int getPhone() {
		return phone;
	}

	@Override
	public String toString() {
		return "seller account=" + id + "  url=" + url + "  degree=" + degree
				+ "   phone=" + phone + "  time=" + time + "   age=" + age;
	}

	public void setName(String name) {
		this.name = name;

	}

	public String setSex(String string) {
		return sex;
	}

}
