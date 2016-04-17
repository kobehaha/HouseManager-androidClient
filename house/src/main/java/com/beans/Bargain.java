package com.beans;

public class Bargain {

	private String pciture;
	private String sellerAccount;
	private String buyerId;
	private String houseId;
	private String time;
	private String timeOk;
	private int status;
	private int price;
	private String sellerName;
	private String buyerName;

	public String getTimeOk() {
		return timeOk;
	}

	public void setTimeOk(String timeOk) {
		this.timeOk = timeOk;
	}

	public String getPciture() {
		return pciture;
	}

	public void setPciture(String pciture) {
		this.pciture = pciture;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getSellerAccount() {
		return sellerAccount;
	}

	public void setSellerAccount(String sellerAccount) {
		this.sellerAccount = sellerAccount;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getHouseId() {
		return houseId;
	}

	public void setHouseId(String houseId) {
		this.houseId = houseId;
	}

	public String getTime() {
		return time.substring(0, 9);
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "bargain信息＝" + "交易时间=" + timeOk + "交易id＝" + buyerId;

	}

}
