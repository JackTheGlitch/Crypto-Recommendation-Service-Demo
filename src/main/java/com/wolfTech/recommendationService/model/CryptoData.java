package com.wolfTech.recommendationService.model;

public class CryptoData {

	private long timeStamp;
	private String cryptoSymbol;
	private double price;
	
	public CryptoData(long timeStamp, String cryptoSymbol, double price) {
		this.timeStamp = timeStamp;
		this.cryptoSymbol = cryptoSymbol;
		this.price = price;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public String getCryptoSymbol() {
		return cryptoSymbol;
	}
	
	public void setCryptoSymbol(String cryptoSymbol) {
		this.cryptoSymbol = cryptoSymbol;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}

}
