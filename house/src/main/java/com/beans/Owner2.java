package com.beans;

import java.io.Serializable;

import android.annotation.SuppressLint;

@SuppressWarnings("serial")
public class Owner2 implements Serializable {

	int age, phone;
	String name, sex, weichart;

	@SuppressLint("ParcelCreator")
	public Owner2(String name, int age, String sex, int phone, String weichart) {
		this.age = age;
		this.phone = phone;
		this.name = name;
		this.sex = sex;
		this.weichart = weichart;
	}

	public int getAge() {
		return age;
	}

	public int getPhone() {
		return phone;
	}

	public String getName() {
		return name;
	}

	public String getSex() {
		return sex;
	}

	public String getWeichart() {
		return weichart;
	}

}
