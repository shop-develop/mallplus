package com.zscat.mallplus.pay.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Refund implements Serializable {

	private static final long serialVersionUID = 8148843465555401098L;

	private long refundId;
	private long orderId;
	private String paymethod; // 支付方式，冗余
	private String paySeireId; // 支付流水号，支付宝退款的时候需要
	private BigDecimal refundAmount;
	private BigDecimal payAmount; // 订单支付金额，微信退款的时候需要
	private String refundReason;
	private int refundStatus;
	private String note;
	private Timestamp createTime;
	private Timestamp updateTime;
	private String payOrderType;
	
	public Refund() {}
	
	public Refund(long orderId, String paymethod, String paySeireId, BigDecimal refundAmount,
                  int refundStatus, String refundReason, String note, BigDecimal payAmount, String payOrderType) {
		super();
		this.orderId = orderId;
		this.paymethod = paymethod;
		this.paySeireId = paySeireId;
		this.refundAmount = refundAmount;
		this.refundStatus = refundStatus;
		this.refundReason = refundReason;
		this.note = note;
		this.payAmount = payAmount;
		this.payOrderType = payOrderType;
	}

	public long getRefundId() {
		return refundId;
	}

	public void setRefundId(long refundId) {
		this.refundId = refundId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getPaymethod() {
		return paymethod;
	}

	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getRefundReason() {
		return refundReason;
	}

	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}

	public int getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(int refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getPaySeireId() {
		return paySeireId;
	}

	public void setPaySeireId(String paySeireId) {
		this.paySeireId = paySeireId;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public String getPayOrderType() {
		return payOrderType;
	}

	public void setPayOrderType(String payOrderType) {
		this.payOrderType = payOrderType;
	}

}
