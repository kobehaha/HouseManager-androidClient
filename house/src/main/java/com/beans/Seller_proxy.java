package com.beans;

public class Seller_proxy {
	public Seller seller;
	public String firstchar;

	public Seller_proxy(Seller seller) {
		this.seller = seller;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof Seller) {
			Seller user = (Seller) o;
			return user.getName().equals(seller.getName());
		} else {
			return false;
		}

	}

}
