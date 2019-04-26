package com.zscat.mallplus.pay.vo;


import com.zscat.mallplus.pay.PayMethod;
import com.zscat.mallplus.pay.constant.NotifyType;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PayNotify {

	private String payOrderId;
	private Long orderId; // 本地订单ID
	private PayMethod paymethod; // 支付方式
	private String paySeireId; // 支付流水号
	private BigDecimal tradeAmount; // 支付金额
	private Timestamp partnerPaysuccessTime; // 支付成功时间
	private NotifyType notifyType;
	private String params; // 支付时传递的参数，原样回传

	public String getPayOrderId() {
		return payOrderId;
	}

	public void setPayOrderId(String payOrderId) {
		this.payOrderId = payOrderId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public PayMethod getPaymethod() {
		return paymethod;
	}

	public void setPaymethod(PayMethod paymethod) {
		this.paymethod = paymethod;
	}

	public String getPaySeireId() {
		return paySeireId;
	}

	public void setPaySeireId(String paySeireId) {
		this.paySeireId = paySeireId;
	}

	public BigDecimal getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(BigDecimal tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	public Timestamp getPartnerPaysuccessTime() {
		return partnerPaysuccessTime;
	}

	public void setPartnerPaysuccessTime(Timestamp partnerPaysuccessTime) {
		this.partnerPaysuccessTime = partnerPaysuccessTime;
	}

	public NotifyType getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(NotifyType notifyType) {
		this.notifyType = notifyType;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

}
