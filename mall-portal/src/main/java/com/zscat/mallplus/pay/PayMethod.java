package com.zscat.mallplus.pay;


import com.zscat.mallplus.pay.vo.PayOrder;

/**
 * Created by lanxingcan on 2017/6/22.
 */
public interface PayMethod {
	String name();

	PayPlatform getPayPlatform();

	String getPartnerId();

	String getAppId();

	String getApiKey();

	String getPublicKey();

	String getPayNotifyUrl(PayOrder payOrder);

	String getRefundNotifyUrl(PayOrder.PayOrderType payOrderType);

	String generatePayUrl(PayOrder payOrder);
}
