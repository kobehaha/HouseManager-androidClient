package com.beans;

public class Owner {
	private int id, phone, age;
	private String name, sex, weichart;

	public Owner(int id, int phone, int age, String name, String sex,
			String weichart) {
		this.id = id;
		this.phone = phone;
		this.age = age;
		this.name = name;
		this.sex = sex;
		this.weichart = weichart;

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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getWeichart() {
		return weichart;
	}

	public void setWeichart(String weichart) {
		this.weichart = weichart;
	}

}
