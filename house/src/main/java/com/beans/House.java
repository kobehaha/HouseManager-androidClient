package com.beans;

import java.io.Serializable;

import android.annotation.SuppressLint;

public class House implements Serializable {// 房源类


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name, location, brief;
	private int size, year, price, id;
	private String[] picture;

	@SuppressLint("ParcelCreator")
	public House(String[] url3, int id, String name, String location,
			String brief, int size, int year, int price) {
		this.id = id;
		this.picture = url3;
		this.name = name;
		this.location = location;
		this.brief = brief;
		this.size = size;
		this.year = year;
		this.price = price;

	}

	public House() {
	}
	
	public void setId(int id) {
		this.id = id;
	}


	public String[] getPicture_url() {
		return picture;
	}

	public String[] setPictureUrl(String[] picture) {
		return this.picture=picture;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getId() {
		return id;
	}

}
