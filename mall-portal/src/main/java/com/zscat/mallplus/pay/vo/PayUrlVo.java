package com.zscat.mallplus.pay.vo;

public class PayUrlVo {

	private String type;
	private String payUrl;
	
	public PayUrlVo(){}
	
	public PayUrlVo(String type, String payUrl) {
		this.type = type;
		this.payUrl = payUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
}
