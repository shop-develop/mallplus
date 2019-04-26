package com.zscat.mallplus.pay.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PaySelectVo {

	private List<PayUrlVo> payUrlList = new ArrayList<>();
	private BigDecimal payAmount;
	private long closeTime;
	private long orderId;

	public List<PayUrlVo> getPayUrlList() {
		return payUrlList;
	}

	public void setPayUrlList(List<PayUrlVo> payUrlList) {
		this.payUrlList = payUrlList;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public long getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(long closeTime) {
		this.closeTime = closeTime;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

}
