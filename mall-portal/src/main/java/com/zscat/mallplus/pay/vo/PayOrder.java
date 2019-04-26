package com.zscat.mallplus.pay.vo;

import java.math.BigDecimal;

public class PayOrder {

	private long orderId;
	private String goodsName;
	private BigDecimal payAmount;
	private String userIp;
	private String weixinOpenId;
	private PayOrderType payOrderType;
	private String params; // 自定义参数
	
	public enum PayOrderType {
		shop, diet
	}

	public String getPayOrderId() {
		return payOrderType.name() + orderId;
	}
	
	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getWeixinOpenId() {
		return weixinOpenId;
	}

	public void setWeixinOpenId(String weixinOpenId) {
		this.weixinOpenId = weixinOpenId;
	}

	public PayOrderType getPayOrderType() {
		return payOrderType;
	}

	public void setPayOrderType(PayOrderType payOrderType) {
		this.payOrderType = payOrderType;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
	
}
