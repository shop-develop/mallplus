package com.zscat.mallplus.pay.weixin;


import com.zscat.mallplus.pay.PayMethod;
import com.zscat.mallplus.pay.vo.PayOrder;
import com.zscat.mallplus.pay.weixin.constant.WeixinPayConstant;
import com.zscat.mallplus.pay.weixin.util.WeixinPayUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Service("weixin_appPlatform")
public class WeixinAppPlatform extends AbstraceWeixinPlatform{

	@Override
	protected String getTradeType() {
		return WeixinPayConstant.TRADE_TYPE_APP;
	}

	@Override
	public String generatePayUrl(PayOrder order, PayMethod paymethod) {
		Map<String, String> preOrderRet = prepay(order, paymethod);
		
		if (preOrderRet == null) {
			return null;
		}
		String prepayId = preOrderRet.get("prepay_id");
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("appid", paymethod.getAppId());
		map.put("partnerid", paymethod.getPartnerId());
		map.put("noncestr", WeixinPayUtil.CreateNoncestr(WeixinPayConstant.NONCESTR_LENGTH));
		map.put("timestamp", String.valueOf((System.currentTimeMillis() / 1000)));
		map.put("prepayid", prepayId);
		map.put("package", "Sign=WXPay");
		
		String signStr = WeixinPayUtil.formatUnSignParaMap(map, false, false) + "&key=" + paymethod.getApiKey();
		String sign = DigestUtils.md5Hex(signStr).toUpperCase();
		map.put("sign", sign);
		
		String str1 = "";
		try {
			str1 = "appId=" + paymethod.getAppId() + "&partnerId=" + paymethod.getPartnerId() + "&nonceStr=" + map.get("noncestr") + "&timeStamp=" + map.get("timestamp") + "&prepayId=" + map.get("prepayid") + "&package=" + URLEncoder.encode(map.get("package"), "utf-8") + "&sign=" + sign + "&orderId=" + order.getPayOrderId();
		} catch (UnsupportedEncodingException e){}
		
		String deepLink = "weixin://app/" + paymethod.getAppId() + "/pay/?" + str1;
		
		return deepLink;//JacksonUtil.obj2json(map);
	}
}
