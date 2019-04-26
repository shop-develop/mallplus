package com.zscat.mallplus.pay.alipay;

import com.zscat.mallplus.pay.alipay.constant.AlipayConstant;
import org.springframework.stereotype.Service;

@Service("alipay_pcPlatform")
public class AlipayPcPlatform extends AlipayDirectPayPlatform {

	@Override
	public String getServiceName() {
		return AlipayConstant.DIRECT_PAY_SERVICE_NAME;
	}
}
