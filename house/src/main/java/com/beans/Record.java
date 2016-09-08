package com.beans;

import java.util.ArrayList;

public class Record {
	private String time;
	private ArrayList<Bargain> bargains = new ArrayList<Bargain>();

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public ArrayList<Bargain> getBargains() {
		return bargains;
	}

	public void setBargains(ArrayList<Bargain> bargains) {
		this.bargains = bargains;
	}

	public Record(String time, ArrayList<Bargain> bargains) {
		this.bargains = bargains;
		this.time = time;
	}

	public int getItemCount() {// 当前item的数量，record需要占用一个item
		return bargains.size() + 1;
	}

	public Object getItem(int position) {
		if (position == 0) {
			return time;

		} else {
			return bargains.get(position - 1);
		}
	}
}
