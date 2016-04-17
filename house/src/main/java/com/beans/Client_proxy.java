package com.beans;

public class Client_proxy {
	public Client client;

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public String firstchar;

	public Client_proxy(Client client) {
		this.client = client;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof Client) {
			Client user = (Client) o;
			return user.getName().equals(client.getName());
		} else {
			return false;
		}

	}

}
