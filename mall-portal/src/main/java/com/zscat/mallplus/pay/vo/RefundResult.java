package com.zscat.mallplus.pay.vo;

import com.zscat.mallplus.pay.vo.PayOrder.PayOrderType;

import java.math.BigDecimal;

public class RefundResult {

	private long orderId;
	private long refundId;
	private String paySeireId; // 订单支付流水号
	private String refundSeireId;
	private int refundStatus;
	private BigDecimal refundAmount;
	private PayOrderType payOrderType;

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getId() {
		return refundId;
	}

	public void setRefundId(long refundId) {
		this.refundId = refundId;
	}

	public String getPaySeireId() {
		return paySeireId;
	}

	public void setPaySeireId(String paySeireId) {
		this.paySeireId = paySeireId;
	}

	public String getRefundSeireId() {
		return refundSeireId;
	}

	public void setRefundSeireId(String refundSeireId) {
		this.refundSeireId = refundSeireId;
	}

	public int getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(int refundStatus) {
		this.refundStatus = refundStatus;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public PayOrderType getPayOrderType() {
		return payOrderType;
	}

	public void setPayOrderType(PayOrderType payOrderType) {
		this.payOrderType = payOrderType;
	}

}
