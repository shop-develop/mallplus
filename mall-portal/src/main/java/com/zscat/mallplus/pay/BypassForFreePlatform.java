package com.zscat.mallplus.pay;

import com.zscat.mallplus.pay.constant.NotifyType;
import com.zscat.mallplus.pay.constant.PayConstant;
import com.zscat.mallplus.pay.exeception.BusinessException;
import com.zscat.mallplus.pay.vo.PayNotify;
import com.zscat.mallplus.pay.vo.PayOrder;
import com.zscat.mallplus.pay.vo.Refund;
import com.zscat.mallplus.pay.vo.RefundResult;
import org.apache.commons.codec.digest.HmacUtils;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Service
public class BypassForFreePlatform extends AbstractPayPlatform {


	private final String domainHostname;
	private final String signKey;

	private String generateSignature(String orderId) {

		return Hex.toHexString(HmacUtils.hmacSha256(signKey.getBytes(), orderId.getBytes()));
	}

	@Autowired
	BypassForFreePlatform(@Value("${mall.domain}") String hostname,
	                      @Value("${pay.bypass.signkey}") String signKey) {
		this.domainHostname = hostname;
		this.signKey = signKey;

	}

	@Override
	public String generatePayUrl(PayOrder order, PayMethod paymethod) {
		if (order.getPayAmount().compareTo(new BigDecimal(0)) != 0)
			throw new BusinessException("pay amount is not zero");

		String orderId = Long.toString(order.getOrderId());
		String signature = generateSignature(orderId);


		return domainHostname + "pay/free/payNotify.html?orderId=" + orderId + "&signature=" + signature;
	}

	@Override
	public PayNotify parsePayNotify(HttpServletRequest request, PayMethod paymethod, PayOrder.PayOrderType payOrderType) {
		String orderId = request.getParameter("orderId");
		String signature = request.getParameter("signature");

		String desiredSignature = generateSignature(orderId);
		if (!signature.toLowerCase().equals(desiredSignature.toLowerCase())) {
			throw new BusinessException("无效签名");
		}


		PayNotify notify = new PayNotify();
		notify.setPayOrderId(orderId);
		String orderIdStr = payOrderType == null ? orderId : orderId.replace(payOrderType.name(), "");
		notify.setOrderId(Long.parseLong(orderIdStr));
		notify.setPaymethod(paymethod);
		notify.setPaySeireId("bypass-" + orderId);
		notify.setPartnerPaysuccessTime(new Timestamp(System.currentTimeMillis()));
		notify.setNotifyType(NotifyType.ONLINE);
		notify.setTradeAmount(new BigDecimal(0));
		return notify;
	}

	@Override
	public String payNotifyResponse(boolean success) {
		if (success) {
			return "success";
		} else {
			return "failed";
		}
	}

	@Override
	public RefundResult refund(Refund refund, PayMethod paymethod) {
		RefundResult result = new RefundResult();
		result.setOrderId(refund.getOrderId());
		result.setRefundId(refund.getRefundId());
		result.setRefundAmount(refund.getRefundAmount());
		result.setRefundStatus(PayConstant.REFUND_STATUS_FAILED);
		return result;
	}
}
