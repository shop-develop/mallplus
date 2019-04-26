package com.zscat.mallplus.pay;

import com.zscat.mallplus.pay.vo.PayNotify;
import com.zscat.mallplus.pay.vo.PayOrder;
import com.zscat.mallplus.pay.vo.PayOrder.PayOrderType;
import com.zscat.mallplus.pay.vo.Refund;
import com.zscat.mallplus.pay.vo.RefundResult;

import javax.servlet.http.HttpServletRequest;

public interface PayPlatform {

	/**
	 * 生成支付链接或者是支付参数
	 * 可能会有网络调用，所以不能在事务中调用
	 * 
	 * @param order
	 * @return
	 */
	public String generatePayUrl(PayOrder order, PayMethod paymethod);

	/**
	 * 解析支付通知参数
	 *
	 * @param request
	 * @return
	 */
	public PayNotify parsePayNotify(HttpServletRequest request, PayMethod paymethod, PayOrderType payOrderType);

	/**
	 * 生成支付通知的响应
	 *
	 * @param success
	 * @return
	 */
	public String payNotifyResponse(boolean success);

	/**
	 * 调用支付平台的退款接口退款
	 *
	 * @param refund
	 * @return
	 */
	public RefundResult refund(Refund refund, PayMethod paymethod);

	/**
	 * 解析退款通知参数
	 *
	 * @param request
	 * @return
	 */
	public RefundResult parseRefundNotify(HttpServletRequest request, PayMethod paymethod, PayOrderType payOrderType);

	/**
	 * 查询退款状态
	 * @param refund
	 * @return
	 */
	public RefundResult queryRefund(Refund refund);
	
}
