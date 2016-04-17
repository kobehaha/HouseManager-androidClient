package com.utl;

import java.util.Comparator;

import com.beans.Seller_proxy;

/*
 */
public class PinyinComparator_seller implements Comparator<Seller_proxy> {

	public int compare(Seller_proxy o1, Seller_proxy o2) {
		if (o1.firstchar.equals("@") || o2.firstchar.equals("#")) {
			return -1;
		} else if (o1.firstchar.equals("#") || o2.firstchar.equals("@")) {
			return 1;
		} else {
			return o1.firstchar.compareTo(o2.firstchar);
		}
	}

}
