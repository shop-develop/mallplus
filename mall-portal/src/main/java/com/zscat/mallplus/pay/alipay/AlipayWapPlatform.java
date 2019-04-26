package com.zscat.mallplus.pay.alipay;

import com.zscat.mallplus.pay.alipay.constant.AlipayConstant;
import org.springframework.stereotype.Service;

@Service("alipay_wapPlatform")
public class AlipayWapPlatform extends AlipayDirectPayPlatform {

	@Override
	public String getServiceName() {
		return AlipayConstant.WAP_CREATE_DIRECT_SERVICE_NAME;
	}
}
