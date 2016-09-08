package com.utl;

import java.util.Comparator;

import com.beans.Client_proxy;

public class PinyinComparator_client implements Comparator<Client_proxy> {

	@Override
	public int compare(Client_proxy o1, Client_proxy o2) {
		if (o1.firstchar.equals("@") || o2.firstchar.equals("#")) {
			return -1;
		} else if (o1.firstchar.equals("#") || o2.firstchar.equals("@")) {
			return 1;
		} else {
			return o1.firstchar.compareTo(o2.firstchar);
		}
	}

}
