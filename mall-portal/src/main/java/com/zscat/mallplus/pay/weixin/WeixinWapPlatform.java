package com.zscat.mallplus.pay.weixin;


import com.zscat.mallplus.common.CommonConstant;
import com.zscat.mallplus.pay.PayMethod;
import com.zscat.mallplus.pay.vo.PayOrder;
import com.zscat.mallplus.pay.weixin.constant.WeixinPayConstant;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

@Service("weixin_wapPlatform")
public class WeixinWapPlatform extends AbstraceWeixinPlatform{

	@Override
	protected String getTradeType() {
		return WeixinPayConstant.TRADE_TYPE_WAP;
	}

	@Override
	public String generatePayUrl(PayOrder order, PayMethod paymethod) {
		Map<String, String> preOrderRet = prepay(order, paymethod);
		
		if (preOrderRet == null) {
			return null;
		}
		String returl = "";
		try {
			returl = URLEncoder.encode(CommonConstant.DOMAIN + "/pay/success.html?orderId=" + order.getOrderId(), "utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		return preOrderRet.get("mweb_url") + "&redirect_url=" + returl;
	}
}
