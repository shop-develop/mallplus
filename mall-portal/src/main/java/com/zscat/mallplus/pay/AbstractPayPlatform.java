package com.zscat.mallplus.pay;


import com.zscat.mallplus.oms.entity.OmsOrder;
import com.zscat.mallplus.pay.exeception.BusinessException;
import com.zscat.mallplus.pay.vo.PayOrder;
import com.zscat.mallplus.pay.vo.Refund;
import com.zscat.mallplus.pay.vo.RefundResult;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractPayPlatform implements PayPlatform {

	@Override
	public RefundResult parseRefundNotify(HttpServletRequest request, PayMethod paymethod, PayOrder.PayOrderType payOrderType) {
		throw new BusinessException(paymethod.name() + " not support parseRefundNotify. ");
	}
	

	@Override
	public RefundResult queryRefund(Refund refund) {
		throw new BusinessException("not supported method");
	}
	
	protected String getGoodsName(OmsOrder order) {
		return "Mall商城-" + order.getId();
	}
}
